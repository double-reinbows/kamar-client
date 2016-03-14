package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

/**
 * A single answer to a {@link SurveyQuestion}.
 */
public class SurveyAnswer extends Model {

    @SerializedName("question_id") private String questionId;

    private String answer;

    public SurveyAnswer() {}

    public SurveyAnswer(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

}