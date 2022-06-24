package com.momo.toys.be.factory;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.toys.be.model.KafkaMessage;

public class KafkaMessageDeserializer implements Deserializer<KafkaMessage>{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KafkaMessage deserialize(String s, byte[] data){
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing KafkaMessage...");
            return objectMapper.readValue(new String(data, "UTF-8"), KafkaMessage.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to KafkaMessage");
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey){
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public void close(){
        Deserializer.super.close();
    }
}
