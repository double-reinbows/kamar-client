package com.martabak.kamar.service;

import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyQuestion;
import com.squareup.okhttp.Response;

import java.util.List;

import retrofit.http.*;
import rx.Observable;

/**
 * Provides feedback and survey related functionality.
 */
public interface FeedbackService extends Service {

    @POST("feedback")
    Observable<Response> createFeedback(@Body Feedback feedback);

    @GET("surveyQuestionsVIEW") // FIXME need to figure out how views will look
    Observable<List<SurveyQuestion>> getSurveyQuestions();

    @POST("surveyAnswers")
    Observable<Response> createSurveyAnswers(@Body List<SurveyAnswer> surveyAnswers);

}
