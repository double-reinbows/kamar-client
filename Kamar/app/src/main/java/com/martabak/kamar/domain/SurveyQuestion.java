package com.martabak.kamar.domain;

/**
 * A single survey question.
 */
public class SurveyQuestion extends Model {

    public final int order;

    public final boolean active;

    public final String question;

    public SurveyQuestion() {
        this.order = Integer.MAX_VALUE;
        this.active = false;
        this.question = null;
    }

    public SurveyQuestion(int order, boolean active, String question) {
        this.order = order;
        this.active = active;
        this.question = question;
    }

}
