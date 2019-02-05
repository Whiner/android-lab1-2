package com.university.lab1.dto;

import com.university.lab1.service.WordService;

import lombok.Getter;

@Getter
public enum TranslationType {
    RUS_TO_ENG(WordService.RUS_TO_ENG), ENG_TO_RUS(WordService.ENG_TO_RUS);

    private String value;
    TranslationType(String value) {
        this.value = value;
    }
}
