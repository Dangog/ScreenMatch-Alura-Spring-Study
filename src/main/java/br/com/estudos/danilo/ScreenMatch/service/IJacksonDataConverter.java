package br.com.estudos.danilo.ScreenMatch.service;

public interface IJacksonDataConverter {
    <T> T getData(String content, Class<T> genericClass);
}
