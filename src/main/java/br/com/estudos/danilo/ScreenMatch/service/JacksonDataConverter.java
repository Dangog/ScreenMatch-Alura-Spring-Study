package br.com.estudos.danilo.ScreenMatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDataConverter implements IJacksonDataConverter {

    private ObjectMapper om = new ObjectMapper();
    @Override
    public <T> T getData(String content, Class<T> genericClass) {
        try {
            return om.readValue(content,genericClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
