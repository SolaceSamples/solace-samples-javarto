/**
 * Copyright 2004-2021 Solace Corporation. All rights reserved.
 *
 */
package com.solace.samples.javarto.features;

import java.nio.ByteBuffer;
import java.util.logging.Level;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import com.solacesystems.solclientj.core.resource.Topic;

/**
 * 
 * PayloadCompression.java
 * 
 * This sample demonstrates:
 * <ul>
 * <li>Set the compression level on the payload
 * <li>Subscribing to a topic for direct messages.
 * <li>Publishing direct messages to a topic.
 * <li>Receiving messages with a message handler.
 * <li>Check to see if the received message is compressed
 * </ul>
 * 
 * <p>
 * This sample shows the basics of creating a context, creating a session,
 * connecting a session, subscribing to a topic, and publishing direct messages
 * to a topic, set the payload compression level. 
 * This is meant to be a very basic example, so there are minimal
 * session properties and a message handler that simply prints any received
 * message to the screen.
 * 
 * <p>
 * Although other samples make use of common code to perform some of the most
 * common actions, many of those common methods are explicitly included in this
 * sample to emphasize the most basic building blocks of any application.
 * 
 * <strong>This sample illustrates the ease of use of concepts, and may not be
 * GC-free.<br>
 * See Perf* samples for GC-free examples. </strong>
 */
public class PayloadCompression extends AbstractSample {

	private boolean keepRxMsgs = false;
	private SessionHandle sessionHandle = Solclient.Allocator
			.newSessionHandle();
	private ContextHandle contextHandle = Solclient.Allocator
			.newContextHandle();
	private MessageHandle messageHandle = Solclient.Allocator
			.newMessageHandle();
	private Topic topic = Solclient.Allocator
			.newTopic(SampleUtils.SAMPLE_TOPIC);

	private ByteBuffer content = ByteBuffer.allocateDirect(50000);

	MessageCallbackSample messageCallback;

	@Override
	protected void printUsage(boolean secureSession) {
		String usage = ArgumentsParser.getCommonUsage(secureSession);
		System.out.println(usage);
	}

	/**
	 * This is the main method of the sample
	 */
	@Override
	protected void run(String[] args, SessionConfiguration config, Level logLevel) throws SolclientException {

        // Init
        print(" Initializing the Java RTO Messaging API...");
        int rc = Solclient.init(new String[0]);
        assertReturnCode("Solclient.init()", rc, SolEnum.ReturnCode.OK);

        // Set a log level (not necessary as there is a default)
        Solclient.setLogLevel(logLevel);

        // Context
        print(" Creating a context ...");
        rc = Solclient.createContextForHandle(contextHandle, new String[0]);
        assertReturnCode("Solclient.createContext()", rc, SolEnum.ReturnCode.OK);

        // Session
        print(" Creating a session ...");
        String[] sessionProps = getSessionProps(config, 2);

        // Setting the compression level
        sessionProps[sessionProps.length - 2] = SessionHandle.PROPERTIES.PAYLOAD_COMPRESSION_LEVEL;
        sessionProps[sessionProps.length - 1] = "9";

        messageCallback = getMessageCallback(keepRxMsgs);
        SessionEventCallback sessionEventCallback = getDefaultSessionEventCallback();
        rc = contextHandle.createSessionForHandle(sessionHandle, sessionProps,
                messageCallback, sessionEventCallback);
        assertReturnCode("contextHandle.createSession()", rc,
                SolEnum.ReturnCode.OK);

        // Connect
        print(" Connecting session ...");
        rc = sessionHandle.connect();
        assertReturnCode("sessionHandle.connect()", rc, SolEnum.ReturnCode.OK);

        // Subscribe
        print(" Adding subscription ...");
        rc = sessionHandle.subscribe(topic,
                SolEnum.SubscribeFlags.WAIT_FOR_CONFIRM, 0);
        assertReturnCode("sessionHandle.subscribe()", rc, SolEnum.ReturnCode.OK);

        // Allocate the message
        rc = Solclient.createMessageForHandle(messageHandle);
        assertReturnCode("Solclient.createMessage()", rc, SolEnum.ReturnCode.OK);
        messageHandle.setDestination(topic);

        // Set content and destination on the message
        messageHandle.setBinaryAttachment(content);

        // Send it
        rc = sessionHandle.send(messageHandle);
        assertReturnCode("sessionHandle.send()", rc, SolEnum.ReturnCode.OK,
                SolEnum.ReturnCode.IN_PROGRESS);

        // Wait
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long[] rxStats = new long[SolEnum.StatRx.getEnumCount()];
        long[] txStats = new long[SolEnum.StatTx.getEnumCount()];

        // Get the updated stats
        sessionHandle.getTxStats(txStats);
        sessionHandle.getRxStats(rxStats);

        long totalBytesSent = txStats[SolEnum.StatTx.TOTAL_DATA_BYTES];
        long totalBytesReceived = rxStats[SolEnum.StatRx.TOTAL_DATA_BYTES];

        System.out.println("Original Message size: " + content.capacity());
        System.out.println("Total Bytes got sent: " + totalBytesSent);
        System.out.println("Total Bytes got received: " + totalBytesReceived);

    }

	/**
	 * Invoked when the sample finishes
	 */
	@Override
	protected void finish(int status) {

		/*************************************************************************
		 * Cleanup
		 *************************************************************************/

		if (messageCallback != null) {
			try {
				messageCallback.destroy();
			} catch (Throwable t) {
				error("Unable to call destroy on messageCallback ", t);
			}
		}

		finish_DestroyHandle(messageHandle, "messageHandle");

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
		PayloadCompression sample = new PayloadCompression();
		sample.run(args);
	}

}
