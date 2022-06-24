package com.momo.toys.be.factory;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.toys.be.model.KafkaMessage;

public class KafkaMessageSerializer implements Serializer<KafkaMessage>{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, KafkaMessage kafkaMessage){
        if(kafkaMessage == null){
            System.err.println("Kafka message is null");
            return null;
        }
        System.out.println("Serializing KafkaMessage");
        try{
            return objectMapper.writeValueAsBytes(kafkaMessage);
        }catch(JsonProcessingException e){
            throw new SerializationException("Error when serializing KafkaMessage to byte[]");
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey){
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public void close(){
        Serializer.super.close();
    }
}
