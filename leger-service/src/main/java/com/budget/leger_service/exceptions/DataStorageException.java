package com.budget.leger_service.exceptions;

public class DataStorageException extends RuntimeException{
    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    // Можно добавить и конструктор только с сообщением, если нужно
    public DataStorageException(String message) {
        super(message);
    }
}
