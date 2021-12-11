package com.momo.toys.be.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.momo.toys.be.dto.Problem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonUtility {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Problem createProblem(String title, int status, String message) {

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setStatus(status);
        problem.setDetail(message);

        return problem;
    }

    public <T> List<T> loadDataList(Class<T> clazz, String filename) {
        JsonNode jsonNode = readJsonFromFile(filename);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode.toString(), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Error while converting list of objects [%s]", filename), e);
        }
    }

    public <T> T loadData(Class<T> clazz, String filename) {
        JsonNode jsonNode = readJsonFromFile(filename);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Error while converting objects [%s]", filename), e);
        }
    }

    private JsonNode readJsonFromFile(String filename) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, JsonNode.class);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Error while reading file [%s]", filename), e);
        }
    }
}
