package com.caio.overwatch_tracker.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final LocalDateTime dateTime;
    private final Integer statusCode;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(LocalDateTime dateTime, Integer statusCode, String error, String message, String path) {
        this.dateTime = dateTime;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
