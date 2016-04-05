package com.brianmsurgenor.honoursproject.FoodDiary;

/**
 * Simple class to hold the foods used in the food entry section
 */
public class Food {

    private String name;
    private String category;
    private int picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

}
