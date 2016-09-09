package com.martabak.kamar.domain.managers;

import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.domain.options.HousekeepingOption;

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

    private Boolean flag = Boolean.FALSE;

    private String currSection = null;

    private SurveyManager() {}

    public static SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
        }
        return instance;
    }

    /**
     * @return The current restaurant order, if it has been set.
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

    public Boolean getFlag() { return flag; }

    public String getCurrSection() { return currSection; }

    /**
     * Set the current restaurant order.
     * @param questions The current restaurant order.
     */
    public void setQuestions(HashMap<String, SurveyQuestion> questions) {
        this.questions = questions;
    }
    /**
     * Set the current restaurant order.
     * @param ratings The current restaurant order.
     */
    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }
    /**
     * Set the current restaurant order.
     * @param mappings The current restaurant order.
     */
    public void setMapping(HashMap<String, List<String>> mappings) {
        this.sectionMappings = mappings;
    }

    public void setFlag(Boolean flag) { this.flag = flag; }

    public void setCurrSection(String currSection) {this.currSection = currSection; }

}
