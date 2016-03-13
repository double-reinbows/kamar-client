package com.martabak.kamar.domain;

/**
 * A single survey question.
 */
public class SurveyQuestion extends Model {

    public final Integer order;

    public final String question;

    public SurveyQuestion() {}

    public SurveyQuestion(Integer order, String question) {
        this.order = order;
        this.question = question;
    }

}
