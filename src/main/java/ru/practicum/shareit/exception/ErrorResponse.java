package ru.practicum.shareit.exception;

public class ErrorResponse {
    private String code;
    private String error;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.error = message;
    }

}
