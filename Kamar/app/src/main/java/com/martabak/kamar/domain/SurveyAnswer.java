package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rei on 24/08/16.
 */
public class SurveyAnswer {

    @SerializedName("question_id") public final String questionId;

    public final Integer rating;

    public final String comment;

    public SurveyAnswer() {
        this.questionId = null;
        this.rating = null;
        this.comment = null;
    }


    public SurveyAnswer(String questionId, Integer rating, String comment) {
        this.questionId = questionId;
        this.rating = rating;
        this.comment = comment;
    }

}
