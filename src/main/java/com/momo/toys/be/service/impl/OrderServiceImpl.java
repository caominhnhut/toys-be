package com.momo.toys.be.service.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.momo.toys.be.enumeration.MessageType;
import com.momo.toys.be.factory.KafkaConstants;
import com.momo.toys.be.model.KafkaMessage;
import com.momo.toys.be.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaDeliveryTemplate;

    @Autowired
    private Gson gson;

    @Override
    public String checkOut(String orderId){
        // TODO: send data to logictis
        // TODO: send data to delivery
        try{
            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setContent(orderId);
            kafkaMessage.setSender("TOY-BE");
            kafkaMessage.setMessageType(MessageType.JOIN);

            Message<KafkaMessage> message = MessageBuilder
                    .withPayload(kafkaMessage)
                    .setHeader(KafkaHeaders.TOPIC, KafkaConstants.KAFKA_TOY_DELIVERY_ITEM_TOPIC)
                    .build();

            SendResult<String, KafkaMessage> result = kafkaDeliveryTemplate.send(message).get();
            return result.getRecordMetadata().topic();
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
            return null;
        }
    }
}
