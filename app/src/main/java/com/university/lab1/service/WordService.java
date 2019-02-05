package com.university.lab1.service;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private DatabaseService databaseService;

    private Map<LinearLayout, Word> answersVersions = new HashMap<>();
    private int answerIndex;

    Random random = new Random();

    public WordService(TranslationType type, Context context) {
        this.type = type;
        databaseService = new DatabaseService(context);
        words = databaseService.findAll();
    }



    public String nextWord(LinearLayout... layouts) throws Exception {
        int length = layouts.length;
        if (length > words.size()) {
            throw new Exception("Слишком мало слов или слишком много LinearLayout");
        }

        generateAnswerVersions(layouts);

        for (LinearLayout layout : layouts) {
            ImageView imageView = (ImageView) layout.getChildAt(0);
            TextView textView = (TextView) layout.getChildAt(1);

            if(type == TranslationType.RUS_TO_ENG){
                textView.setText(answersVersions.get(layout).getEnglishTranslate());
            } else {
                textView.setText(answersVersions.get(layout).getRussianTranslate());
            }
        }
        if(type == TranslationType.RUS_TO_ENG){
            return words.get(answerIndex).getRussianTranslate();
        } else {
            return words.get(answerIndex).getEnglishTranslate();
        }
    }

    private void generateAnswerVersions(LinearLayout... layouts) throws Exception {
        answersVersions.clear();
        int rightAnswer = random.nextInt(answersVersionsMaxCount);
        for (int i = 0; i < layouts.length; i++) {
            if (i == rightAnswer) {
                answerIndex = i;
            }
            answersVersions.put(layouts[i], getNextRandomAnswerVersion());
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
}
