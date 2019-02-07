package com.university.lab1.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.university.lab1.dto.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private SQLiteOpenHelperImpl sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String tableName = "words";

    public DatabaseService(Context context) {
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, tableName);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void insert(Word word) {
        sqLiteDatabase.insert(tableName, null, word.toContentValues());
    }

    public void delete(int id) {
        sqLiteDatabase.delete(tableName, "id = " + id, null);
    }

    public List<Word> findAll() {
        Cursor query = sqLiteDatabase.query(
                tableName,
                new String[]{"id", "russian", "english", "imageLocalPath", "isInArchive"},
                null,
                null,
                null,
                null,
                null
        );
        List<Word> words = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                Log.d("customdatabase", "selected id = " + query.getInt(0));
                Word word = new Word();
                word.setId(query.getInt(0));
                word.setRussianTranslate(query.getString(1));
                word.setEnglishTranslate(query.getString(2));
                word.setInArchive(query.getInt(4) != 0);
                words.add(word);
            } while (query.moveToNext());
        }

        query.close();
        return words;
    }

    public void close() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
            Log.d("customdatabase", "db closed");
        }
    }


}
