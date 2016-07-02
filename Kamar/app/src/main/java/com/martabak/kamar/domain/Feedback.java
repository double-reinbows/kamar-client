package com.martabak.kamar.domain;


/**
 * Contains a customer's feedback
 */
public class Feedback extends Model {

    /**
     * The actual text feedback.
     */
    public final String text;

    /**
     * The stars rating out of 5.
     */
    public final Float rating;

    public Feedback() {
        this.text = null;
        this.rating = null;
    }

    public Feedback(String text, float rating) {
        this.text = text;
        this.rating = rating;
    }

}
