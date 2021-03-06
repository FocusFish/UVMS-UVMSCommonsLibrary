/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package fish.focus.uvms.commons.message.impl;

import fish.focus.uvms.commons.message.api.Fault;
import fish.focus.uvms.commons.message.api.MessageConstants;
import fish.focus.uvms.commons.message.context.MappedDiagnosticContext;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public abstract class AbstractProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProducer.class);

    @Inject
    @JMSConnectionFactory("java:/JmsXA")
    JMSContext context;

    public abstract Destination getDestination();

    public String sendModuleMessage(final String text, final Destination replyTo) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.PERSISTENT, 0L);
    }

    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, props, DeliveryMode.PERSISTENT, 0L);
    }

    public String sendModuleMessageNonPersistent(final String text, final Destination replyTo, final long timeToLiveInMillis) throws JMSException {
        return sendModuleMessageWithProps(text, replyTo, null, DeliveryMode.NON_PERSISTENT, timeToLiveInMillis);
    }

    public String sendModuleMessageWithProps(final String text, final Destination replyTo, Map<String, String> props, final int jmsDeliveryMode, final long timeToLiveInMillis) throws JMSException {

        TextMessage message = context.createTextMessage();
        if (props != null && props.size() > 0) {
            for (Map.Entry<String, String> entry : props.entrySet()) {
                message.setStringProperty(entry.getKey(), entry.getValue());
            }
        }
        MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);
        message.setJMSReplyTo(replyTo);
        message.setText(text);
        context.createProducer()
                .setDeliveryMode(jmsDeliveryMode)
                .setTimeToLive(timeToLiveInMillis)
                .send(getDestination(), message);
        return message.getJMSMessageID();
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text) throws JMSException {
        sendResponseMessageToSender(message, text, Message.DEFAULT_TIME_TO_LIVE);
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive) throws JMSException {
        sendResponseMessageToSender(message, text, timeToLive, DeliveryMode.PERSISTENT);
    }

    public void sendResponseMessageToSender(final TextMessage message, final String text, long timeToLive, int deliveryMode) throws JMSException {

        TextMessage responseMessage = context.createTextMessage(text);
        responseMessage.setJMSCorrelationID(message.getJMSMessageID());
        MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(responseMessage);
        context.createProducer()
                .setDeliveryMode(deliveryMode)
                .setTimeToLive(timeToLive)
                .send(message.getJMSReplyTo(), responseMessage);
    }

    public void sendFault(final TextMessage message, Fault fault) {
        try {
            String text = JAXBUtils.marshallJaxBObjectToString(fault);
            TextMessage response = context.createTextMessage(text);
            MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(response);
            context.createProducer()
                    .send(message.getJMSReplyTo(), response);
        } catch (JAXBException | JMSException e) {
            LOGGER.warn("Could not send fault message.", e);
        }
    }

    public String sendMessageWithSpecificIds(String messageToSend,  Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws JMSException {
        final TextMessage message = context.createTextMessage(messageToSend);
        if (jmsMessageID != null && jmsMessageID.length() > 0) {
            message.setJMSMessageID(jmsMessageID);
        }
        if (jmsCorrelationID != null && jmsCorrelationID.length() > 0) {
            message.setJMSCorrelationID(jmsCorrelationID);
        }
        message.setJMSReplyTo(replyTo);
        MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);

        context.createProducer()
                .setDeliveryMode(DeliveryMode.PERSISTENT)
                .send(getDestination(), message);
        return message.getJMSMessageID();
    }

    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws JMSException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, Message.DEFAULT_TIME_TO_LIVE);
    }

    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis) throws JMSException {
        return sendMessageToSpecificQueue(messageToSend, destination, replyTo, timeToLiveInMillis, DeliveryMode.PERSISTENT);
    }


    public String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo, long timeToLiveInMillis, int deliveryMode) throws JMSException {

        final TextMessage message = context.createTextMessage(messageToSend);
        message.setJMSReplyTo(replyTo);

        MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);

        context.createProducer()
                .setTimeToLive(timeToLiveInMillis)
                .setDeliveryMode(deliveryMode)
                .send(destination, message);

        return message.getJMSMessageID();
    }

    public String sendMessageToSpecificQueueWithFunction(String messageToSend, Destination destination, Destination replyTo, String function, String grouping) throws JMSException {

        TextMessage message = context.createTextMessage(messageToSend);
        message.setJMSReplyTo(replyTo);
        message.setStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY, function);
        message.setStringProperty(MessageConstants.JMS_MESSAGE_GROUP, grouping);
        MappedDiagnosticContext.addThreadMappedDiagnosticContextToMessageProperties(message);

        context.createProducer()
                .send(destination, message);

        return message.getJMSMessageID();
    }
}
