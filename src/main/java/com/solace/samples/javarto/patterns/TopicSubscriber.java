/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.solace.samples.javarto.patterns;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.Handle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.MessageSupport;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import com.solacesystems.solclientj.core.resource.Topic;

/**
 * 
 * TopicPublisher.java
 * 
 * This sample demonstrates:
 * <ul>
 * <li>Publishing a direct message to a topic.
 * </ul>
 * 
 * <p>
 * This sample shows the basics of creating a context, creating a session,
 * connecting a session, and publishing a direct message to a topic. This is
 * meant to be a very basic example, so there are minimal session properties and
 * a message handler that simply prints any received message to the screen.
 * 
 * <p>
 * Common code to perform some of the most common actions, are explicitly
 * included in this sample to emphasize the most basic building blocks of any
 * application.
 * 
 * @author Dishant Langayan
 */
public class TopicSubscriber {
    public static void main(String[] args) throws SolclientException, IOException {
        if (args.length < 3) {  // Check command line arguments
            System.out.println("Usage: TopicSubscriber <host:port> <message-vpn> <client-username> [password]");
            System.exit(-1);
        }

        String host = args[0];
        String vpnName = args[1];
        String username = args[2];
        String password = null;
        if (args.length > 3) {
            password = args[3];
        }
        System.out.println("TopicSubscriber initializing...");


        // Initialize the API first
        System.out.println(" Initializing the Java RTO Messaging API...");
        int rc = Solclient.init(new String[0]);
        assertReturnCode("Solclient.init()", rc, SolEnum.ReturnCode.OK);

        // Create the context
        System.out.println(" Creating a context ...");
        final ContextHandle contextHandle = Solclient.Allocator.newContextHandle();
        rc = Solclient.createContextForHandle(contextHandle, new String[0]);
        assertReturnCode("Solclient.createContextForHandle()", rc, SolEnum.ReturnCode.OK);

        // Create the Session
        System.out.println(" Creating a session ...");
        // [Session] -> create the session properties
        ArrayList<String> sessionProperties = new ArrayList<String>();
        sessionProperties.add(SessionHandle.PROPERTIES.HOST);
        sessionProperties.add(host);
        sessionProperties.add(SessionHandle.PROPERTIES.USERNAME);
        sessionProperties.add(username);
        if (password != null) {
            sessionProperties.add(SessionHandle.PROPERTIES.PASSWORD);
            sessionProperties.add(password);
        }
        sessionProperties.add(SessionHandle.PROPERTIES.VPN_NAME);
        sessionProperties.add(vpnName);
        String[] props = new String[sessionProperties.size()];
        
        // Get the binary attachment from the msg
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);  // assume no messages bigger than 1k
        byte[] bufferArray = new byte[buffer.capacity()];

        // [Session] -> define a message callback to receive messages
        MessageCallback messageCallback = new MessageCallback() {
            @Override
            public void onMessage(Handle handle) {
                try {
                    // Get the received msg from the handle
                    MessageSupport messageSupport = (MessageSupport) handle;
                    MessageHandle rxMessage = messageSupport.getRxMessage();

                    // Get the binary attachment from the msg
                    buffer.clear();
                    rxMessage.getBinaryAttachment(buffer);
                    buffer.flip();
                    buffer.get(bufferArray, 0, buffer.remaining());

                    System.out.println("Received a message with content: " + new String(bufferArray));

                    // Display the contents of a message in human-readable form
//                    System.out.println(" Complete message dump: ");
//                    System.out.println(rxMessage.dump(SolEnum.MessageDumpMode.FULL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        // [Session] -> define a session event callback to events such as
        // connect/disconnect events
        SessionEventCallback sessionEventCallback = new SessionEventCallback() {

            @Override
            public void onEvent(SessionHandle sessionHandle) {
                System.out.println(" Received SessionEvent:" + sessionHandle.getSessionEvent());
            }
        };

        // [Session] -> create a session handle and the actual session
        final SessionHandle sessionHandle = Solclient.Allocator.newSessionHandle();
        rc = contextHandle.createSessionForHandle(sessionHandle, sessionProperties.toArray(props), messageCallback,
                sessionEventCallback);
        assertReturnCode("contextHandle.createSession()", rc, SolEnum.ReturnCode.OK);

        // [Session] -> finally connect the session
        System.out.println(" Connecting session ...");
        rc = sessionHandle.connect();
        assertReturnCode("sessionHandle.connect()", rc, SolEnum.ReturnCode.OK);

        // Subscribe to the destination to receive messages
        Topic topic = Solclient.Allocator.newTopic("tutorial/topic");
        System.out.println(" Subscribing to topic: " + topic.getName());
        rc = sessionHandle.subscribe(topic, SolEnum.SubscribeFlags.WAIT_FOR_CONFIRM, 0);
        assertReturnCode("sessionHandle.subscribe()", rc, SolEnum.ReturnCode.OK);

        System.out.println("Connected, and running. Press [ENTER] to quit.");
        try {
            while (System.in.available() == 0) {
                Thread.sleep(1000);  // wait 1 second
            }
        } catch (InterruptedException e) {
            // Thread.sleep() interrupted... probably getting shut down
        }
        System.out.println("Exiting.");

        // Cleanup!
        // [Cleanup] -> disconnect session
        sessionHandle.disconnect();
        sessionHandle.destroy();

        // [Cleanup] -> destroy the context
        contextHandle.destroy();
    }

    /**
     * Helper method to validate return codes.
     * 
     * @param operation
     * @param returnCode
     * @param rc
     * @throws IllegalStateException
     */
    private static void assertReturnCode(String operation, int returnCode, int... rc) throws IllegalStateException {
        boolean oneRCMatched = false;
        for (int i = 0; i < rc.length; i++) {
            if (rc[i] == returnCode) {
                oneRCMatched = true;
                break;
            }
        }
        if (!oneRCMatched) {
            throw new IllegalStateException(String.format("'%s' returned unexpected returnCode %d:%s", operation,
                    returnCode, SolEnum.ReturnCode.toString(returnCode)));
        }
    }
}
