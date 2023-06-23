/**
 * Copyright 2004-2021 Solace Corporation. All rights reserved.
 *
 */
package com.solace.samples.javarto.snippets;

import java.nio.ByteBuffer;
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
import com.solacesystems.solclientj.core.resource.Queue;

public class HowToUseQueuePartitionKey {

    /* This method demonstrates how to set the queue partition key on a message
     * that does not already have a queue partition key set.
     */
    public void howToApplyQueuePartitionKey(MessageHandle messageHandle, String queuePartitionKey) throws SolclientException {
        assertNull(String.format("Expected to be passed a MessageHandle without a queue partition key, but instead "
                + "the queue partition key was set to `%s`",
                messageHandle.getQueuePartitionKey()),
            messageHandle.getQueuePartitionKey());
        messageHandle.setQueuePartitionKey(queueParitionKey);
        assertEquals(String.format("The configured queue partition key should have matched the specified queue partition key, "
            + "but instead the configured key was `%s`, and the specified key was `%s`.",
            messageHandle.getQueuePartitionKey(), queuePartitionKey),
        messageHandle.getQueuePartitionKey(), queuePartitionKey);
    }

    /* This method demonstrates how to set/overwrite the queue partition key on a message
     * that already has a queue partition key set.
     */
    public void howToApplyQueuePartitionKey(MessageHandle messageHandle, String queuePartitionKey) throws SolclientException {
        assertNotNull(String.format("Expected to be passed a MessageHandle with a queue partition key, but instead "
                + "the queue partition key was null"),
            messageHandle.getQueuePartitionKey());
        messageHandle.setQueuePartitionKey(queueParitionKey);
        assertEquals(String.format("The configured queue partition key should have matched the specified queue partition key, "
            + "but instead the configured key was `%s`, and the specified key was `%s`.",
            messageHandle.getQueuePartitionKey(), queuePartitionKey),
        messageHandle.getQueuePartitionKey(), queuePartitionKey);
    }

    /* This method demonstrates how to retrieve the queue partition key from a message
     * that already has the queue partition key set.
     */
    public String howToGetPresentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
        String queuePartitionKey = messageHandle.getQueuePartitionKey();
        assertNotNull(String.format("The queue partition key should not have been null."),
            queuePartitionKey);
        return queuePartitionKey;
    }

    /* This method demonstrates how to retrieve the queue partition key from a message
     * that does not have the queue partition key set.
     */
    public void howToGetAbsentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
        assertNull(String.format("The queue partition key should have been null, but instead was %s.",
                messageHandle.getQueuePartitionKey()),
            messageHandle.getQueuePartitionKey());
    }

    /* This method demonstrates how to delete the queue partition key from a message
     * that already has the queue partition key set.
     */
    public void howToDeletePresentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
        assertNotNull(String.format("Expected to be passed a MessageHandle with a present queue partition key, but "
                + "instead, the queue partition key was absent."),
            messageHandle.getQueuePartitionKey());
        messageHandle.deleteQueuePartitionKey();
        assertNull(String.format("Expected the call to `deleteQueuePartitionKey()` to succeed, causing the queue "
            + "partition key to be set to null, but instead the queue partition key after deletion is `%s`."),
            messageHandle.getQueuePartitionKey(),
        messageHandle.getQueuePartitionKey());
    }

    /* This method demonstrates how to delete the queue partition key from a message
     * that does not have the queue partition key set.
     */
    public void howToDeleteAbsentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
        assertNull(String.format("Expected to be passed a MessageHandle without a queue partition key, but "
            + "instead, the queue partition key was set to `%s`.",
            messageHandle.getQueuePartitionKey()),
        messageHandle.getQueuePartitionKey());
    }

}
