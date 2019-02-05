package com.university.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.university.lab1.dto.TranslationType;
import com.university.lab1.service.DatabaseService;
import com.university.lab1.service.WordService;

public class MainActivity extends AppCompatActivity {
    private WordService wordService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordService = new WordService(TranslationType.RUS_TO_ENG, this);
        setContentView(R.layout.main);
        setOnActionButtons();
    }

    private void setOnActionButtons() {
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(event -> {
            setContentView(R.layout.game);
            TextView mainWord = findViewById(R.id.word);
            try {
                mainWord.setText(wordService.nextWord(
                        findViewById(R.id.word1),
                        findViewById(R.id.word2),
                        findViewById(R.id.word3),
                        findViewById(R.id.word4)
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Button stopButton = findViewById(R.id.stopButton);
            stopButton.setOnClickListener(v -> {
                setContentView(R.layout.main);
                setOnActionButtons();
            });
        });

        Button changeLangButton = findViewById(R.id.changeLangButton);
        TextView langTextBox = findViewById(R.id.langTextbox);
        langTextBox.setText(wordService.getType().getValue());
        changeLangButton.setOnClickListener(event -> langTextBox.setText(wordService.revertLang()));



    }


}
