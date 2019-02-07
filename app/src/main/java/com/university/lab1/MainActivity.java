package com.university.lab1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.university.lab1.dto.TranslationType;
import com.university.lab1.service.WordService;

public class MainActivity extends AppCompatActivity {
    private WordService wordService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wordService = new WordService(
                TranslationType.RUS_TO_ENG,
                this
        );
        setOnActionButtons();
    }

    private void setOnActionButtons() {
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(event -> {
            setContentView(R.layout.game);
            wordService.setViews(
                    new TextView[]{
                            findViewById(R.id.word1),
                            findViewById(R.id.word2),
                            findViewById(R.id.word3),
                            findViewById(R.id.word4)});
            wordService.setMainView(findViewById(R.id.word));
            try {
                wordService.nextWord();
            } catch (Exception e) {
                e.printStackTrace();
            }

            LinearLayout word1_layout = findViewById(R.id.word1_layout);
            word1_layout.setOnClickListener(event1 -> {
                try {
                    wordService.checkAnswer((TextView) word1_layout.getChildAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            LinearLayout word2_layout = findViewById(R.id.word2_layout);
            word2_layout.setOnClickListener(event2 -> {
                try {
                    wordService.checkAnswer((TextView) word2_layout.getChildAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            LinearLayout word3_layout = findViewById(R.id.word3_layout);
            word3_layout.setOnClickListener(event3 -> {
                try {
                    wordService.checkAnswer((TextView) word3_layout.getChildAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            LinearLayout word4_layout = findViewById(R.id.word4_layout);
            word4_layout.setOnClickListener(event4 -> {
                try {
                    wordService.checkAnswer((TextView) word4_layout.getChildAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wordService != null) {
            wordService.close();
        }
    }
}
