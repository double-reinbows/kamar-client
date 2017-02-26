package com.martabak.kamar.domain.managers;

import java.util.HashSet;
import java.util.Set;

public class Managers {

    private static Set<Manager> managers = new HashSet<>();

    public static void register(Manager m) {
        managers.add(m);
    }

    public static void clear() {
        for (Manager m : managers) {
            m.clear();
        }
    }

}
