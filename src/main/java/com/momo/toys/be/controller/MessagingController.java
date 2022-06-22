package com.momo.toys.be.controller;

import com.momo.toys.be.dto.Channel;
import com.momo.toys.be.dto.PrivateMessage;
import com.momo.toys.be.factory.KafkaConstants;
import com.momo.toys.be.model.KafkaMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@RestController
public class MessagingController{

//    @Autowired
//    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;
//
//    // TODO: not using at this time
//    @PostMapping(value = "/no-auth/message/send")
//    public void send(@RequestBody KafkaMessage message){
//        message.setTimestamp(Calendar.getInstance());
//        try{
//            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
//        }catch(InterruptedException | ExecutionException e){
//            throw new RuntimeException(e);
//        }
//    }

    @PutMapping(value = "/no-auth/message/channel")
    public ResponseEntity establishPrivateChatChannel(@RequestBody Channel channel){
        return ResponseEntity.status(HttpStatus.OK).body("hi");
    }

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/group")
//    public KafkaMessage broadcastGroupMessage(@Payload KafkaMessage message){
//        message.setTimestamp(Calendar.getInstance());
//        try{
//            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
//        }catch(InterruptedException | ExecutionException e){
//            throw new RuntimeException(e);
//        }
//
//        return message;
//    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/group")
    public KafkaMessage addUser(@Payload KafkaMessage message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/private.chat.sendMessage.{channelId}")
    @SendTo("/topic/private.chat.{channelId}")
    public ResponseEntity chatMessage(@DestinationVariable String channelId, PrivateMessage privateMessage){

        privateMessage.setTimestamp(Calendar.getInstance());

        return ResponseEntity.status(HttpStatus.OK).body(privateMessage);
    }
}
