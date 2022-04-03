package com.github.freetie.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freetie.course.entity.HttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(value = HttpException.class)
    public void defaultHttpExceptionHandler(HttpServletResponse response, HttpException httpException) throws Exception {
        response.setStatus(httpException.getStatusCode());
        HashMap<String, String> jsonObject = new HashMap<>();
        jsonObject.put("message", httpException.getMessage());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(jsonObject));
        response.getOutputStream().flush();
    }
}
