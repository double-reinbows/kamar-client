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
     * The section of the option, in English.
     */
    @SerializedName("section_en") public final String sectionEn;

    /**
     * The section of the option, in Indonesian Bahasa.
     */
    @SerializedName("section_in") public final String sectionIn;

    /**
     * The section of the option, in Chinese.
     */
    @SerializedName("section_zh") public final String sectionZh;

    /**
     * The section of the option, in Russian.
     */
    @SerializedName("section_ru") public final String sectionRu;

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
        this.sectionEn = null;
        this.sectionIn = null;
        this.sectionZh = null;
        this.sectionRu = null;
        this.questionEn = null;
        this.questionIn = null;
        this.questionZh = null;
        this.questionRu = null;
    }

    public SurveyQuestion(int order, boolean active, String sectionEn, String sectionIn,
                          String sectionZh, String sectionRu, String questionEn, String questionIn,
                          String questionZh, String questionRu) {
        this.order = order;
        this.active = active;
        this.sectionEn = sectionEn;
        this.sectionIn = sectionIn;
        this.sectionZh = sectionZh;
        this.sectionRu = sectionRu;
        this.questionEn = questionEn;
        this.questionIn = questionIn;
        this.questionZh = questionZh;
        this.questionRu = questionRu;
    }

    /**
     * @return The question in the appropriate language.
     */
    public String getSection() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.sectionEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.sectionIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.sectionZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.sectionRu;
        } else {
            Log.e(SurveyQuestion.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.sectionEn;
        }
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
