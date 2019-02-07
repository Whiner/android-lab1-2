package com.university.lab1.service;

import android.content.Context;
import android.widget.TextView;

import com.university.lab1.dto.TranslationType;
import com.university.lab1.dto.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Getter;

public class WordService {
    public static final String RUS_TO_ENG = "Русский -> Английский";
    public static final String ENG_TO_RUS = "Английский -> Русский";

    private int answersVersionsMaxCount = 4;

    @Getter
    private TranslationType type;
    private List<Word> words;
    private TextView[] views;
    private DatabaseService databaseService;

    private Map<TextView, Word> answersVersions = new HashMap<>();
    private TextView rightAnswer;

    Random random = new Random();

    private TextView mainView;

    public WordService(TranslationType type, Context context) {
        this.type = type;
        databaseService = new DatabaseService(context);
        words = databaseService.findAll();
    }

    public String nextWord() throws Exception {
        int length = views.length;
        rightAnswer = null;
        if (length > words.size()) {
            throw new Exception("Слишком мало слов или слишком много LinearLayout");
        }

        generateAnswerVersions();

        for (TextView textView : views) {
            if(type == TranslationType.RUS_TO_ENG){
                textView.setText(answersVersions.get(textView).getEnglishTranslate());
            } else {
                textView.setText(answersVersions.get(textView).getRussianTranslate());
            }
        }
        Word word = answersVersions.get(rightAnswer);
        if (word != null) {
            if (type == TranslationType.RUS_TO_ENG) {
                mainView.setText(word.getRussianTranslate());
                return word.getRussianTranslate();
            } else {
                mainView.setText(word.getEnglishTranslate());
                return word.getEnglishTranslate();
            }
        } else {
            throw new Exception("Ответ = null");
        }
    }

    private void generateAnswerVersions() throws Exception {
        answersVersions.clear();
        int rightAnswerIndex = random.nextInt(answersVersionsMaxCount);
        for (int i = 0; i < views.length; i++) {
            if (i == rightAnswerIndex) {
                rightAnswer = views[i];
            }
            answersVersions.put(views[i], getNextRandomAnswerVersion());
        }
    }

    private Word getNextRandomAnswerVersion() throws Exception {
        if (words.size() <= answersVersions.size() || answersVersions.size() >= answersVersionsMaxCount) {
            throw new Exception("Слов слишком мало");
        }
        Word word;

        do {
            word = words.get(random.nextInt(words.size()));
        } while (answersVersions.containsValue(word));
        return word;
    }


    public String revertLang() {
        switch (type) {
            case RUS_TO_ENG:
                this.type = TranslationType.ENG_TO_RUS;
                return ENG_TO_RUS;
            case ENG_TO_RUS:
                this.type = TranslationType.RUS_TO_ENG;
                return RUS_TO_ENG;
            default:
                return "";
        }
    }

    public boolean isRightAnswer(TextView textView) {
        if (!answersVersions.isEmpty() && rightAnswer != null) {
            return textView == rightAnswer;
        }
        return false;
    }

    public void checkAnswer(TextView textView) throws Exception {
        if (isRightAnswer(textView)) {
            nextWord();
        }
    }

    public void close() {
        databaseService.close();
    }

    public void setViews(TextView[] textViews) {
        this.views = textViews;
    }

    public void setMainView(TextView main) {
        this.mainView = main;
    }
}
