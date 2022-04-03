package com.github.freetie.course.entity;

public class HttpException extends RuntimeException{
    int statusCode;
    String message;

    public HttpException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
