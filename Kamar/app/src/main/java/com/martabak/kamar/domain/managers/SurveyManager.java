package com.martabak.kamar.domain.managers;

import com.martabak.kamar.domain.SurveyQuestion;

import java.util.HashMap;
import java.util.List;

public class SurveyManager implements Manager {

    private static SurveyManager instance;

    //SurveyQuestion._id to Integer of 0-5 (0 if guest chose no rating)
    private HashMap<String, Integer> ratings = null;

    //SurveyQuestion.section to list of SurveyQuestions
    private HashMap<String, List<SurveyQuestion>> sectionMappings = null;

    private List<String> sections;

    private boolean prevSubmission = false;

    private SurveyManager() {}

    public static SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
            Managers.register(instance);
        }
        return instance;
    }

    @Override
    public void clear() {
        ratings = null;
        sectionMappings = null;
        sections = null;
        prevSubmission = false;
    }

    public HashMap<String, Integer> getRatings() { return ratings; }

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

    public void setSubmitted() { this.prevSubmission = true; }

    public void setSections(List<String> sections) { this.sections = sections; }

}
