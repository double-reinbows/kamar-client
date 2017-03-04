package com.martabak.kamar.domain.managers;

import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Keeps the housekeeping variables in memory to be passed between HousekeepingActivity,
 * HousekeepingFragmen.
 */
public class HousekeepingManager implements Manager {

    private static HousekeepingManager instance;

    private List<HousekeepingOption> hkOptions = null;

    private HashMap<String, Integer> hkOrder = null;

    private List<String> sections = null;

    private HousekeepingManager() {}

    public static HousekeepingManager getInstance() {
        if (instance == null) {
            instance = new HousekeepingManager();
            Managers.register(instance);
        }
        return instance;
    }

    public void clear() {
        hkOptions = null;
        hkOrder = null;
        sections = null;
    }

    /**
     * @return The current ic_restaurant order, if it has been set.
     */
    public List<HousekeepingOption> getOptions() {
        return hkOptions;
    }
    /**
     * @return
     */
    public HashMap<String, Integer> getOrder() { return hkOrder; }
    /**
     * @return
     */
    public List<String> getSections() { return sections; }

    /**
     * Set the current ic_restaurant order.
     * @param options The current ic_restaurant order.
     */
    public void setHkOptions(List<HousekeepingOption> options) {
        this.hkOptions= options;
    }
    /**
     * Set the current ic_restaurant order.
     * @param order The current ic_restaurant order.
     */
    public void setOrder(HashMap<String, Integer> order) {
        this.hkOrder = order;
    }
    /**
     * Set the current ic_restaurant order.
     * @param sections The current ic_restaurant order.
     */
    public void setSections(List<String> sections) {
        this.sections = sections;
    }

}
