package com.university.lab1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEnoughWordsException extends Exception {
    public NotEnoughWordsException(String message) {
        super(message);
    }

}
