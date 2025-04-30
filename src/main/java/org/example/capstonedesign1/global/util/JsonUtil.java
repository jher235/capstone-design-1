package org.example.capstonedesign1.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.capstonedesign1.global.exception.InternalServerException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseClass(Class<T> tClass, String content){
        try {
            return objectMapper.readValue(content, tClass);
        } catch (JsonMappingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        }
    }

    public static <T> String convertToJson(Class<T> tClass){
        try {
            return objectMapper.writeValueAsString(tClass);
        } catch (JsonMappingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        }
    }

    public static <T> String convertToJson(List<T> objects){
        try {
            return objectMapper.writeValueAsString(objects);
        } catch (JsonMappingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.JSON_PARSING_FAILED, e.getMessage());
        }
    }
}
