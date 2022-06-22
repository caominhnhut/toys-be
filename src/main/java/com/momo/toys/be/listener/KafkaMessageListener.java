package com.momo.toys.be.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.momo.toys.be.enumeration.MessageType;
import com.momo.toys.be.factory.KafkaConstants;
import com.momo.toys.be.model.KafkaMessage;

@Component
public class KafkaMessageListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageListener.class);

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        LOGGER.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            LOGGER.info("User Disconnected : " + username);

            KafkaMessage chatMessage = new KafkaMessage();
            chatMessage.setMessageType(MessageType.LEAVE);
            chatMessage.setSender(username);

            messageSendingOperations.convertAndSend("/topic/group", chatMessage);
        }
    }

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void listen(KafkaMessage message){
        System.out.println("Sending via kafka listener...");
        //messageSendingOperations.convertAndSend("/topic/group", message);
    }
}
