/**
 * Copyright 2004-2021 Solace Corporation. All rights reserved.
 *
 */
package com.solace.samples.javarto.howtos;

import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.handle.MessageHandle;

public class HowToUseQueuePartitionKey {

    /* This method demonstrates how to set the queue partition key on a message
     * that does not already have a queue partition key set.
     */
    public void howToApplyQueuePartitionKey(MessageHandle messageHandle, String queuePartitionKey) throws SolclientException {
    	if (messageHandle.getQueuePartitionKey() != null) {
    		throw new AssertionError(String.format("Expected to be passed a MessageHandle without a queue partition key, "
    				+ "but instead the queue partition key was set to `%s`",
                    messageHandle.getQueuePartitionKey()));
    	}
        messageHandle.setQueuePartitionKey(queuePartitionKey);
        if (!messageHandle.getQueuePartitionKey().equals(queuePartitionKey)) {
    		throw new AssertionError(String.format("The configured queue partition key should have matched the specified queue partition key, "
    	            + "but instead the configured key was `%s`, and the specified key was `%s`.",
    	            messageHandle.getQueuePartitionKey(), queuePartitionKey));
        }
    }

    /* This method demonstrates how to set/overwrite the queue partition key on a message
     * that already has a queue partition key set.
     */
    public void howToOverwriteQueuePartitionKey(MessageHandle messageHandle, String queuePartitionKey) throws SolclientException {
    	if (messageHandle.getQueuePartitionKey() == null) {
    		throw new AssertionError("Expected to be passed a MessageHandle with a queue partition key, but instead "
                    + "the queue partition key was null");
    	}
        messageHandle.setQueuePartitionKey(queuePartitionKey);
        if (!messageHandle.getQueuePartitionKey().equals(queuePartitionKey)) {
        	throw new AssertionError(String.format("The configured queue partition key should have matched the specified queue partition key, "
                    + "but instead the configured key was `%s`, and the specified key was `%s`.",
                    messageHandle.getQueuePartitionKey(), queuePartitionKey));
        }
    }

    /* This method demonstrates how to retrieve the queue partition key from a message
     * that already has the queue partition key set.
     */
    public String howToGetPresentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
        String queuePartitionKey = messageHandle.getQueuePartitionKey();
        if (queuePartitionKey == null) {
        	throw new AssertionError("The queue partition key should not have been null.");
        }
        return queuePartitionKey;
    }

    /* This method demonstrates how to retrieve the queue partition key from a message
     * that does not have the queue partition key set.
     */
    public void howToGetAbsentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
    	if (messageHandle != null) {
    		throw new AssertionError(String.format("The queue partition key should have been null, but instead was %s.",
                    messageHandle.getQueuePartitionKey()));
    	}
    }

    /* This method demonstrates how to delete the queue partition key from a message
     * that already has the queue partition key set.
     */
    public void howToDeletePresentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
    	if (messageHandle.getQueuePartitionKey() == null) {
    		throw new AssertionError("Expected to be passed a MessageHandle with a present queue partition key, but "
                    + "instead, the queue partition key was absent.");
    	}
        messageHandle.deleteQueuePartitionKey();
        if (messageHandle.getQueuePartitionKey() != null) {
        	throw new AssertionError(String.format("Expected the call to `deleteQueuePartitionKey()` to succeed, causing the queue "
                    + "partition key to be set to null, but instead the queue partition key after deletion is `%s`.",
                    messageHandle.getQueuePartitionKey()));
        }
    }

    /* This method demonstrates how to delete the queue partition key from a message
     * that does not have the queue partition key set.
     */
    public void howToDeleteAbsentQueuePartitionKey(MessageHandle messageHandle) throws SolclientException {
    	if (messageHandle.getQueuePartitionKey() != null) {
    		throw new AssertionError(String.format("Expected to be passed a MessageHandle without a queue partition key, but "
    	            + "instead, the queue partition key was set to `%s`.",
    	            messageHandle.getQueuePartitionKey()));
    	}
    }

}
