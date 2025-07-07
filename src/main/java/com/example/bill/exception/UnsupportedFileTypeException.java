package com.example.bill.exception;

/**
 * @version 1.0
 * @Author admin
 * @Date 2025/7/7 18:04
 */
public class UnsupportedFileTypeException extends RuntimeException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
