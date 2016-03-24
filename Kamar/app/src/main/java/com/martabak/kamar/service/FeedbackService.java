package com.martabak.kamar.service;

import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides feedback and survey related functionality.
 */
public interface FeedbackService {

    /**
     * The base server url.
     */
    String BASE_URL = "http://192.168.178.24:5984";

    @POST("feedback")
    Observable<Response> createFeedback(@Body Feedback feedback);

    @GET("surveyQuestionsVIEW") // FIXME need to figure out how views will look
    Observable<List<SurveyQuestion>> getSurveyQuestions();

    @POST("surveyAnswers")
    Observable<Response> createSurveyAnswers(@Body List<SurveyAnswer> surveyAnswers);

}
