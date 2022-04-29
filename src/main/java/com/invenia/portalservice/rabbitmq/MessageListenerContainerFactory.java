package com.invenia.portalservice.rabbitmq;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageListenerContainerFactory {
    private final ConnectionFactory connectionFactory;

    public MessageListenerContainer createMessageListenerContainer(String queueName) {
        SimpleMessageListenerContainer mlc = new SimpleMessageListenerContainer(connectionFactory);
        mlc.addQueueNames(queueName);
        mlc.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return mlc;
    }
}
