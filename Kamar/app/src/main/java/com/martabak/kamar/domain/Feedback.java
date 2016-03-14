package com.martabak.kamar.domain;


/**
 * Contains a customer's feedback
 */
public class Feedback extends Model {

    /**
     * The actual text feedback.
     */
    public final String text;

    public Feedback() {
        this.text = null;
    }

    public Feedback(String text) {
        this.text = text;
    }

}
