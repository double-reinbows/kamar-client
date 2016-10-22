package com.martabak.kamar.domain.managers;

import com.martabak.kamar.domain.SurveyQuestion;

import java.util.HashMap;
import java.util.List;

/**
 * Keeps the housekeeping variables in memory to be passed between HousekeepingActivity,
 * HousekeepingFragmen.
 */
public class SurveyManager {

    private static SurveyManager instance;

    //_id to SurveyQuestion
    private HashMap<String, SurveyQuestion> questions = null;

    //SurveyQuestion._id to Integer of 0-5 (0 if guest chose no rating)
    private HashMap<String, Integer> ratings = null;

    private HashMap<String, List<String>> sectionMappings = null;


    private Boolean prevSubmission = Boolean.FALSE;


    private SurveyManager() {}

    public static SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
        }
        return instance;
    }

    /**
     * @return The current ic_restaurant order, if it has been set.
     */
    public HashMap<String, SurveyQuestion> getQuestions() {
        return questions;
    }
    /**
     * @return
     */
    public HashMap<String, Integer> getRatings() { return ratings; }
    /**
     * @return
     */
    public HashMap<String, List<String>> getMappings() { return sectionMappings; }

    public Boolean getPrevSubmission() { return prevSubmission; }


    /**
     * Set the current ic_restaurant order.
     * @param questions The current ic_restaurant order.
     */
    public void setQuestions(HashMap<String, SurveyQuestion> questions) {
        this.questions = questions;
    }
    /**
     * Set the current ic_restaurant order.
     * @param ratings The current ic_restaurant order.
     */
    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }
    /**
     * Set the current ic_restaurant order.
     * @param mappings The current ic_restaurant order.
     */
    public void setMapping(HashMap<String, List<String>> mappings) {
        this.sectionMappings = mappings;
    }

    public void setPrevSubmission(Boolean prevSubmission) { this.prevSubmission = prevSubmission; }


}
