package com.university.lab1.dto;

import android.content.ContentValues;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Word {
    private int id;
    private String russianTranslate;
    private String englishTranslate;
    private String imageLocalPath;
    private boolean isInArchive;

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("russian", russianTranslate);
        contentValues.put("english", englishTranslate);
        contentValues.put("imageLocalPath", imageLocalPath);
        contentValues.put("isInArchive", isInArchive);
        return contentValues;
    }

    public Word(String russianTranslate, String englishTranslate, String imageLocalPath, boolean isInArchive) {
        this.russianTranslate = russianTranslate;
        this.englishTranslate = englishTranslate;
        this.imageLocalPath = imageLocalPath;
        this.isInArchive = isInArchive;
    }
}
