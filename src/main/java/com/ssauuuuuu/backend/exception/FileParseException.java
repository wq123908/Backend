package com.ssauuuuuu.backend.exception;

import java.io.IOException;

/**
 * @version 1.0
 * @Author admin
 * @Date 2025/7/7 18:03
 */
public class FileParseException extends RuntimeException {
    public FileParseException(String message, IOException e) {
        super(message);
    }

    public FileParseException(String message) {
        super(message);
    }
}
