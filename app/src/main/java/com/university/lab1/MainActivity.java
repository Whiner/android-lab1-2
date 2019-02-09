package com.university.lab1;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.university.lab1.dto.TranslationType;
import com.university.lab1.exception.NotEnoughWordsException;
import com.university.lab1.exception.WordExistException;
import com.university.lab1.service.AddService;
import com.university.lab1.service.MyTimerTask;
import com.university.lab1.service.WordService;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private WordService wordService;
    private AddService addService;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wordService = new WordService(
                TranslationType.RUS_TO_ENG,
                this
        );
        addService = new AddService(this);
        initMainLayout();
    }

    private void initMainLayout() {
        updateAvailableWordsCount();
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(event -> {
            wordService.updateData();
            if (isAvailableWordsExist()) {
                setContentView(R.layout.game);
                wordService.setViews(
                        new TextView[]{
                                findViewById(R.id.word1),
                                findViewById(R.id.word2),
                                findViewById(R.id.word3),
                                findViewById(R.id.word4)}
                );
                wordService.setMainView(findViewById(R.id.word));
                nextWord();

                setLayoutListener(R.id.word1_layout);
                setLayoutListener(R.id.word2_layout);
                setLayoutListener(R.id.word3_layout);
                setLayoutListener(R.id.word4_layout);

                Button stopButton = findViewById(R.id.stopButton);
                stopButton.setOnClickListener(v -> setMainLayout());
            }
        });

        Button addNewWordButton = findViewById(R.id.addNewWordButton);
        addNewWordButton.setOnClickListener(v -> {
            setContentView(R.layout.add);
            Button addButton = findViewById(R.id.addButton);
            TextView russian = findViewById(R.id.russianWord);
            TextView english = findViewById(R.id.englishWord);
            addButton.setOnClickListener(v1 -> {
                addWord(russian.getText().toString(), english.getText().toString());
                russian.setText("");
                english.setText("");
            });

            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v1 -> {
                russian.setText("");
                english.setText("");
                setMainLayout();
            });
        });

        Button changeLangButton = findViewById(R.id.changeLangButton);
        TextView langTextBox = findViewById(R.id.langTextbox);
        langTextBox.setText(wordService.getType().getValue());
        changeLangButton.setOnClickListener(event -> langTextBox.setText(wordService.revertLang()));
    }

    private void setMainLayout() {
        setContentView(R.layout.main);
        initMainLayout();
    }

    private void addWord(String russian, String english) {
        try {
            addService.addWord(russian, english);
            Toast.makeText(this, "Добавлено!", Toast.LENGTH_SHORT).show();
        } catch (WordExistException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAvailableWordsExist() {
        try {
            wordService.checkAvailableWords();
            return true;
        } catch (NotEnoughWordsException e) {
            showExceptionDialog(e.getMessage());
            return false;
        }
    }

    private void setLayoutListener(int layout) {
        LinearLayout word_layout = findViewById(layout);
        word_layout.setOnClickListener(event -> {
            boolean right = wordService.checkAnswer(
                    (TextView) word_layout.getChildAt(0)
            );
            showResult(right);
            nextWord();
        });
    }

    private void showExceptionDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setNeutralButton("Хорошо", (dialog, which) -> this.setMainLayout())
                .create()
                .show();
    }

    private void updateAvailableWordsCount() {
        TextView wordsCount = findViewById(R.id.availableWordsCountTextView);
        wordsCount.setText(String.valueOf(wordService.getAvailableWordsCount()));
    }

    private void showMessage(String text, int color) {
        TextView message = findViewById(R.id.message);
        if (message != null) {
            MyTimerTask myTimerTask = new MyTimerTask(message, this);
            timer.schedule(myTimerTask, 1000);
            message.setText(text);
            message.setTextColor(color);
            message.setVisibility(View.VISIBLE);
        }
    }

    private void showResult(boolean success) {
        if (success) {
            showMessage("Правильно!", Color.GREEN);
        } else {
            showMessage("Не то(", Color.RED);
        }
    }

    private void nextWord() {
        try {
            wordService.nextWord();
        } catch (NotEnoughWordsException e) {
            showExceptionDialog(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importWords(MenuItem item) {

    }

    public void exportWords(MenuItem item) {

    }

    public void refreshArchive(MenuItem item) {
        wordService.refreshArchive();
        updateAvailableWordsCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wordService != null) {
            wordService.close();
        }
    }

}
