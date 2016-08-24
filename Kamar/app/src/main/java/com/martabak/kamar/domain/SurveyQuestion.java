package com.martabak.kamar.domain;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * A single survey question.
 */
public class SurveyQuestion extends Model {

    public final int order;

    public final boolean active;

    /**
     * The question of the option, in English.
     */
    @SerializedName("question_en") public final String questionEn;

    /**
     * The question of the option, in Indonesian Bahasa.
     */
    @SerializedName("question_in") public final String questionIn;

    /**
     * The question of the option, in Chinese.
     */
    @SerializedName("question_zh") public final String questionZh;

    /**
     * The question of the option, in Russian.
     */
    @SerializedName("question_ru") public final String questionRu;

    public SurveyQuestion() {
        this.order = Integer.MAX_VALUE;
        this.active = false;
        this.questionEn = null;
        this.questionIn = null;
        this.questionZh = null;
        this.questionRu = null;
    }

    public SurveyQuestion(int order, boolean active, String questionEn, String questionIn,
                          String questionZh, String questionRu) {
        this.order = order;
        this.active = active;
        this.questionEn = questionEn;
        this.questionIn = questionIn;
        this.questionZh = questionZh;
        this.questionRu = questionRu;
    }

    /**
     * @return The question in the appropriate language.
     */
    public String getQuestion() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.questionEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.questionIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.questionZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.questionRu;
        } else {
            Log.e(SurveyQuestion.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.questionEn;
        }
    }

}
