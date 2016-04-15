package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link FeedbackService}.
 */
public class FeedbackServer extends Server {

    /**
     * The singleton instance.
     */
    private static FeedbackServer instance;

    /**
     * The service api conf.
     */
    private FeedbackService service;

    /**
     * Constructor.
     */
    private FeedbackServer(Context c) {
        super(c);
        service = createService(FeedbackService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static FeedbackServer getInstance(Context c) {
        if (instance == null)
            instance = new FeedbackServer(c);
        return instance;
    }

    /**
     * Submit some feedback.
     * @param feedback The feedback model to be created.
     * @return Whether or not the feedback was successfully created.
     */
    public Observable<Boolean> createFeedback(Feedback feedback) {
        return service.createFeedback(feedback)
                .map(new Func1<PostResponse, Boolean>() {
                    @Override public Boolean call(PostResponse response) {
                        return  response.ok;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Submit some survey answers.
     * @param surveyAnswers The survey answers model to be created.
     * @return Whether or not the survey answers was successfully created.
     */
    public Observable<Boolean> createSurveyAnswers(List<SurveyAnswer> surveyAnswers) {
        return service.createSurveyAnswers(surveyAnswers)
                .map(new Func1<PostResponse, Boolean>() {
                    @Override public Boolean call(PostResponse response) {
                        return response.ok;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return The current list of active survey questions, in the correct order.
     */
    public Observable<List<SurveyQuestion>> getSurveyQuestions() {
        return service.getSurveyQuestions()
                .flatMap(new Func1<ViewResponse<SurveyQuestion>, Observable<List<SurveyQuestion>>>() {
                    @Override public Observable<List<SurveyQuestion>> call(ViewResponse<SurveyQuestion> response) {
                        List<SurveyQuestion> toReturn = new ArrayList<>(response.total_rows);
                        for (ViewResponse<SurveyQuestion>.ViewResult<SurveyQuestion> i : response.rows) {
                            toReturn.add(i.value);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
