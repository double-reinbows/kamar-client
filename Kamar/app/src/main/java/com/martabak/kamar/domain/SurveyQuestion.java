package com.martabak.kamar.domain;

/**
 * A single survey question.
 */
public class SurveyQuestion extends Model {

    public final Integer order;

    public final String question;

    public SurveyQuestion() {
        this.order = null;
        this.question = null;
    }

    public SurveyQuestion(Integer order, String question) {
        this.order = order;
        this.question = question;
    }

}
