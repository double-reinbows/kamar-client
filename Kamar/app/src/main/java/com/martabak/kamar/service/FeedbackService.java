package com.martabak.kamar.service;

import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.domain.PostResponse;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides feedback and survey related functionality.
 */
public interface FeedbackService {

    @POST("feedback")
    Observable<PostResponse> createFeedback(@Body Feedback feedback);

    @GET("surveyQuestionsVIEW") // FIXME need to figure out how views will look
    Observable<List<SurveyQuestion>> getSurveyQuestions();

    @POST("surveyAnswers")
    Observable<PostResponse> createSurveyAnswers(@Body List<SurveyAnswer> surveyAnswers);

}
