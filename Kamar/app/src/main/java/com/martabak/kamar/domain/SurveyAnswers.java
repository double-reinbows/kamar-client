package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A list of answers to a list of {@link SurveyQuestion}.
 */
public class SurveyAnswers extends Model {

    public final @SerializedName("guest_id") String guestId;

    public final List<SurveyAnswer> answers;

    public SurveyAnswers() {
        this.guestId = null;
        this.answers = null;
    }

    public SurveyAnswers(String guestId, List<SurveyAnswer> answers) {
        this.guestId = guestId;
        this.answers = answers;
    }
}