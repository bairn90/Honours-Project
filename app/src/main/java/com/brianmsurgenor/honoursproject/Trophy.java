package com.brianmsurgenor.honoursproject;

/**
 * Created by Brian on 09/11/2015.
 */
public class Trophy {

    private int _id;
    private String name;
    private String description;
    private int achieved;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }
}
