package com.alzpal.dashboardactivity.memorygames.avatars;

public class Avatar {
    private final int imageResourceId;
    private String name;

    public Avatar(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    // Optional getter for name
    public String getName() {
        return name;
    }

    // Optional setter for name
    public void setName(String name) {
        this.name = name;
    }
}
