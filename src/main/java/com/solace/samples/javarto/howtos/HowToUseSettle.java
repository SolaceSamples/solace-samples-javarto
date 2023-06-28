package com.solace.samples.javarto.howtos;

import com.solace.samples.javarto.features.common.AbstractSample;
import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.handle.FlowHandle;
import com.solacesystems.solclientj.core.handle.Handle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import com.solacesystems.solclientj.core.resource.Queue;

/**
 * Sampler for how to use the new settle API (manual message outcome settlement)
 */
public class HowToUseSettle {

    /**
     * Example of how to settle messages with MessageOutcome.ACCEPTED
     *
     * @param sessionHandle the session handle
     * @param queueToConsumeFrom queue to consume messages from
     */
    public static void settleMessageWithAcceptedOutcome(SessionHandle sessionHandle,
                                                        Queue queueToConsumeFrom) {

        FlowHandle flowHandle = Solclient.Allocator.newFlowHandle();

        /* Setting Flow Properties */
        int propsIndex = 0;
        String[] flowProps = new String[10];

        flowProps[propsIndex++] = FlowHandle.PROPERTIES.BIND_BLOCKING;
        flowProps[propsIndex++] = SolEnum.BooleanValue.ENABLE;

        /* AUTO ACKMODE will give settle() no effect since acks are automatically sent by the API.
         * Therefore, use CLIENT ACKMODE. */
        flowProps[propsIndex++] = FlowHandle.PROPERTIES.ACKMODE;
        flowProps[propsIndex++] = SolEnum.AckMode.CLIENT;

        /* NOTE: MessageOutcome.ACCEPTED is always supported, so no need to add flow property */

        sessionHandle.createFlowForHandle(flowHandle, flowProps, queueToConsumeFrom, null,
                new AbstractSample.MessageCallbackSample() {
                    @Override
                    public void onMessage(Handle handle) {
                        FlowHandle flowHandle = (FlowHandle) handle;
                        MessageHandle msgHandle = flowHandle.getRxMessage();
                        long msgId = msgHandle.getGuaranteedMessageId();

                        /* Attempt to process the message. */
                        /* Successfully processed the message, send an ACK using the ACCEPTED outcome */
                        flowHandle.settle(msgId, MessageOutcome.ACCEPTED);
                    }
                }, new AbstractSample.FlowEventCallbackSample());
    }

    /**
     * Example of how to configure the flow to support negative MessageOutcome.FAILED
     * and then settle messages with MessageOutcome.FAILED
     *
     * @param sessionHandle the session handle
     * @param queueToConsumeFrom queue to consume messages from
     */
    public static void settleMessageWithFailedOutcome(SessionHandle sessionHandle,
                                                      Queue queueToConsumeFrom) {

        FlowHandle flowHandle = Solclient.Allocator.newFlowHandle();

        /* Setting Flow Properties */
        int propsIndex = 0;
        String[] flowProps = new String[10];

        flowProps[propsIndex++] = FlowHandle.PROPERTIES.BIND_BLOCKING;
        flowProps[propsIndex++] = SolEnum.BooleanValue.ENABLE;

        /* AUTO ACKMODE will give settle() no effect since acks are automatically sent by the API.
         * Therefore, use CLIENT ACKMODE. */
        flowProps[propsIndex++] = FlowHandle.PROPERTIES.ACKMODE;
        flowProps[propsIndex++] = SolEnum.AckMode.CLIENT;

        /* Required property for flow to support MessageOutcome.FAILED */
        flowProps[propsIndex++] = FlowHandle.PROPERTIES.REQUIRED_OUTCOME_FAILED;
        flowProps[propsIndex++] = SolEnum.BooleanValue.ENABLE;

        sessionHandle.createFlowForHandle(flowHandle, flowProps, queueToConsumeFrom, null,
                new AbstractSample.MessageCallbackSample() {
                    @Override
                    public void onMessage(Handle handle) {
                        FlowHandle flowHandle = (FlowHandle) handle;
                        MessageHandle msgHandle = flowHandle.getRxMessage();
                        long msgId = msgHandle.getGuaranteedMessageId();

                        /* Attempt to process the message. */
                        /* Failed to process the message right now, send NACK using the FAILED outcome */
                        flowHandle.settle(msgId, MessageOutcome.FAILED);
                    }
                }, new AbstractSample.FlowEventCallbackSample());
    }

    /**
     * Example of how to configure the flow to support negative MessageOutcome.REJECTED
     * and then settle messages with MessageOutcome.REJECTED
     *
     * @param sessionHandle the session handle
     * @param queueToConsumeFrom queue to consume messages from
     */
    public static void settleMessageWithRejectedOutcome(SessionHandle sessionHandle,
                                                        Queue queueToConsumeFrom) {

        FlowHandle flowHandle = Solclient.Allocator.newFlowHandle();

        /* Setting Flow Properties */
        int propsIndex = 0;
        String[] flowProps = new String[10];

        flowProps[propsIndex++] = FlowHandle.PROPERTIES.BIND_BLOCKING;
        flowProps[propsIndex++] = SolEnum.BooleanValue.ENABLE;

        /* AUTO ACKMODE will give settle() no effect since acks are automatically sent by the API.
         * Therefore, use CLIENT ACKMODE. */
        flowProps[propsIndex++] = FlowHandle.PROPERTIES.ACKMODE;
        flowProps[propsIndex++] = SolEnum.AckMode.CLIENT;

        /* Required property for flow to support MessageOutcome.REJECTED */
        flowProps[propsIndex++] = FlowHandle.PROPERTIES.REQUIRED_OUTCOME_REJECTED;
        flowProps[propsIndex++] = SolEnum.BooleanValue.ENABLE;

        sessionHandle.createFlowForHandle(flowHandle, flowProps, queueToConsumeFrom, null,
                new AbstractSample.MessageCallbackSample() {
                    @Override
                    public void onMessage(Handle handle) {
                        FlowHandle flowHandle = (FlowHandle) handle;
                        MessageHandle msgHandle = flowHandle.getRxMessage();
                        long msgId = msgHandle.getGuaranteedMessageId();

                        /* Attempt to process the message. */
                        /* Reject the message and send NACK using the REJECTED outcome */
                        flowHandle.settle(msgId, MessageOutcome.REJECTED);
                    }
                }, new AbstractSample.FlowEventCallbackSample());
    }
}
