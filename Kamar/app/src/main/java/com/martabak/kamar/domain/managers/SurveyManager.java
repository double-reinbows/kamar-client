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

    //SurveyQuestion._id to Integer of 0-5 (0 if guest chose no rating)
    private HashMap<String, Integer> ratings = null;

    //SurveyQuestion.section to list of SurveyQuestions
    private HashMap<String, List<SurveyQuestion>> sectionMappings = null;

    private List<String> sections;

    private Boolean prevSubmission = Boolean.FALSE;


    private SurveyManager() {}

    public static SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
        }
        return instance;
    }

    /**
     * @return
     */
    public HashMap<String, Integer> getRatings() { return ratings; }
    /**
     * @return
     */
    public HashMap<String, List<SurveyQuestion>> getMappings() { return sectionMappings; }

    public Boolean getPrevSubmission() { return prevSubmission; }

    public List<String> getSections() { return sections; }


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
    public void setMapping(HashMap<String, List<SurveyQuestion>> mappings) {
        this.sectionMappings = mappings;
    }

    public void setPrevSubmission(Boolean prevSubmission) { this.prevSubmission = prevSubmission; }

    public void setSections(List<String> sections) { this.sections = sections; }

}
