package com.momo.toys.be.factory;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonHelper{

    private ObjectMapper mapper;

    public <T> T convertJsonFromString(String json, Class<T> tClass){
        if(this.mapper == null){
            this.mapper = new ObjectMapper();
        }

        try {
            return this.mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error while parsing json: "+e.getMessage());
        }
    }
}
