package com.brianmsurgenor.honoursproject.Trophy;

import java.util.LinkedList;

/**
 * Created by Brian on 09/11/2015.
 */
public class Trophies {

    private LinkedList<String> trophyNames = new LinkedList<>();
    private LinkedList<String> trophyDescriptions = new LinkedList<>();

    public Trophies() {
        trophyNames.add("Trophy 1");
        trophyDescriptions.add("This is a trophy");

        trophyNames.add("Trophy 2");
        trophyDescriptions.add("This is a trophy");

        trophyNames.add("Trophy 3");
        trophyDescriptions.add("This is a trophy");

        trophyNames.add("Trophy 4");
        trophyDescriptions.add("This is a trophy");

        trophyNames.add("Trophy 5");
        trophyDescriptions.add("This is a trophy");
    }

    public LinkedList<String> getTrophyNames() {
        return trophyNames;
    }

    public LinkedList<String> getTrophyDescriptions() {
        return trophyDescriptions;
    }
}
