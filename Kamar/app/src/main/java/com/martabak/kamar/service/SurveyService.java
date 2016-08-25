package com.martabak.kamar.service;

import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides feedback and survey related functionality.
 */
public interface SurveyService {

    @GET("survey_question/_design/survey_question/_view/survey_questions")
    Observable<ViewResponse<SurveyQuestion>> getSurveyQuestions();

    @POST("survey_answer")
    Observable<PostResponse> createSurveyAnswers(@Body SurveyAnswers surveyAnswers);

}
