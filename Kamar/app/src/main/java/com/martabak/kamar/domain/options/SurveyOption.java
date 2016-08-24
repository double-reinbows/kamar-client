package com.martabak.kamar.domain.options;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * A Survey Option (Question) {@link Option}.
 */
public class SurveyOption extends Option {

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

    public SurveyOption() {
        this.questionEn = null;
        this.questionIn = null;
        this.questionZh = null;
        this.questionRu = null;
    }

    public SurveyOption(String nameEn, String nameIn, String nameZh, String nameRu, String questionEn,
                        String questionIn, String questionZh, String questionRu, Integer order) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order);
        this.questionEn = questionEn;
        this.questionIn = questionIn;
        this.questionZh = questionZh;
        this.questionRu = questionRu;
    }

    public SurveyOption(String _id, String _rev, String nameEn, String nameIn, String nameZh, String nameRu,
                        String questionEn, String questionIn, String questionZh, String questionRu,
                        Integer order) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order);
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
            Log.e(SurveyOption.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.questionEn;
        }
    }

}
