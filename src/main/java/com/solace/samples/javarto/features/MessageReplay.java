/**
 * Copyright 2004-2021 Solace Corporation. All rights reserved.
 *
 */
package com.solace.samples.javarto.features;

import java.util.logging.Level;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.FlowEventCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.FlowHandle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import com.solacesystems.solclientj.core.resource.Destination;
import com.solacesystems.solclientj.core.resource.Endpoint;
import com.solacesystems.solclientj.core.resource.Queue;

/**
 * 
 * MessageReplay.java
 * 
 * This sample shows how to use the message replay feature. It demonstrates:
 * <ul>
 * <li>Binding to a Queue initiating a replay
 * <li>Handling the replay related events
 * </ul>
 * 
 * This sample should be ran against a messageVPN with a message replay log provisioned.
 * 
 * <strong>This sample illustrates the ease of use of concepts, and may not be
 * GC-free.<br>
 * See Perf* samples for GC-free examples. </strong>
 */
public class MessageReplay extends AbstractSample {

	private ContextHandle contextHandle = Solclient.Allocator
			.newContextHandle();
	private SessionHandle sessionHandle = Solclient.Allocator
			.newSessionHandle();
	private MessageHandle txMessageHandle = Solclient.Allocator
			.newMessageHandle();
	private FlowHandle flowHandle = Solclient.Allocator.newFlowHandle();

	@Override
	protected void printUsage(boolean secureSession) {
		String usage = ArgumentsParser.getCommonUsage(secureSession);
		usage += "This sample:\n";
		usage += "\t[-q queue]\t Guaranteed Message Queue.\n";
		System.out.println(usage);
		finish(1);
	}

	/**
	 * This is the main method of the sample
	 */
	@Override
	protected void run(String[] args, SessionConfiguration config,Level logLevel)
			throws SolclientException {

		try {

			String queueName = config.getArgBag().get("-q");

			// Init
			print(" Initializing the Java RTO Messaging API...");
			int rc = Solclient.init(new String[0]);
			assertReturnCode("Solclient.init()", rc, SolEnum.ReturnCode.OK);
			
			// Set a log level (not necessary as there is a default)
			Solclient.setLogLevel(logLevel);

			// Context
			print(" Creating a context ...");
			rc = Solclient.createContextForHandle(contextHandle, new String[0]);
			assertReturnCode("Solclient.createContext()", rc,
					SolEnum.ReturnCode.OK);

			// Session
			print(" Creating a session ...");
			int spareRoom = 2;
			String[] sessionProps = getSessionProps(config, spareRoom);
			int sessionPropsIndex = sessionProps.length - spareRoom;

			/*************************************************************************
			 * Enable Topic Dispatch on the Session.
			 *************************************************************************/
			sessionProps[sessionPropsIndex++] = SessionHandle.PROPERTIES.TOPIC_DISPATCH;
			sessionProps[sessionPropsIndex++] = SolEnum.BooleanValue.ENABLE;

			SessionEventCallback sessionEventCallback = getDefaultSessionEventCallback();
			MessageCallbackSample messageCallback = getMessageCallback(false);

			rc = contextHandle.createSessionForHandle(sessionHandle,
					sessionProps, messageCallback, sessionEventCallback);
			assertReturnCode("contextHandle.createSession()", rc,
					SolEnum.ReturnCode.OK);

			// Connect
			print(" Connecting session ...");
			rc = sessionHandle.connect();
			assertReturnCode("sessionHandle.connect()", rc,
					SolEnum.ReturnCode.OK);

			/*************************************************************************
			 * Create a Flow
			 *************************************************************************/

			// Flow Properties
			int flowProps = 0;
			String[] flowProperties = new String[14];

			flowProperties[flowProps++] = FlowHandle.PROPERTIES.BIND_BLOCKING;
			flowProperties[flowProps++] = SolEnum.BooleanValue.ENABLE;

			flowProperties[flowProps++] = FlowHandle.PROPERTIES.ACKMODE;
			flowProperties[flowProps++] = SolEnum.AckMode.AUTO;

			FlowEventCallback flowEventCallback = getDefaultFlowEventCallback();
			MessageCallbackSample flowMessageCallback = new MessageCallbackSample(
					"Flow");

			Queue queue = Solclient.Allocator.newQueue(queueName, null);

			print("Creating flow Queue Name [" + queue.getName() + "]");

			rc = sessionHandle.createFlowForHandle(flowHandle, flowProperties,
					queue, null, flowMessageCallback, flowEventCallback);

			assertReturnCode("sessionHandle.createFlowForHandle", rc,
					SolEnum.ReturnCode.OK);

			/*************************************************************************
			 * Publish
			 *************************************************************************/

			int maxMessages = 3;

			print("Publishing [" + maxMessages + "] messages to Queue ["
					+ queue.getName() + "]");

			Destination destination = queue;

			for (int publishCount = 0; publishCount < maxMessages; publishCount++) {

				print("Sending message " + publishCount);
				common_publishMessage(sessionHandle, txMessageHandle,
						destination, SolEnum.MessageDeliveryMode.PERSISTENT);

			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/*************************************************************************
			 * Create new flow with replay
			 *************************************************************************/

			print("Destroying flow.");

			flowHandle.destroy();

			print("Creating flow again, this time with replay.");

			flowProperties[flowProps++] = FlowHandle.PROPERTIES.REPLAY_START_LOCATION;
			flowProperties[flowProps++] = SolEnum.ReplayStartLocation.BEGINNING;
			// The MAX_RECONNECT_TRIES property controls the the new auto-reconnect feature.
			// It allows the flow to auto-reconnect when a replay is started from the broker CLI.
			// A pair of RECONNECTING-RECONNECETED flow events arrive when this happens.
			// The feature is on by default with infinite retries, this is how it can be turned off:

            		//flowProps[propIndexB++] = FlowHandle.PROPERTIES.MAX_RECONNECT_TRIES;
            		//flowProps[propIndexB++] = 0;

			try {
				rc = sessionHandle.createFlowForHandle(flowHandle, flowProperties,
						queue, null, flowMessageCallback, flowEventCallback);
			} catch (SolclientException e) {
				print("Replay flow creation failed. Make sure the message vpn has a replay log provisioned on the broker.");
				throw e;
			}

			assertReturnCode("sessionHandle.createFlowForHandle", rc,
					SolEnum.ReturnCode.OK);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			print("Test Passed");


		} catch (Throwable t) {
			error("An error has occurred ", t);
		}

	}

	/**
	 * Invoked when the sample finishes
	 */
	@Override
	protected void finish(int status) {

		/*************************************************************************
		 * Cleanup
		 *************************************************************************/

		finish_DestroyHandle(flowHandle, "flowHandle");

		finish_DestroyHandle(txMessageHandle, "messageHandle");

		finish_Disconnect(sessionHandle);

		finish_DestroyHandle(sessionHandle, "sessionHandle");

		finish_DestroyHandle(contextHandle, "contextHandle");

		finish_Solclient();

	}

/**
     * Boilerplate, calls {@link #run(String[])
     * @param args
     */
	public static void main(String[] args) {
		MessageReplay sample = new MessageReplay();
		sample.run(args);
	}

}
