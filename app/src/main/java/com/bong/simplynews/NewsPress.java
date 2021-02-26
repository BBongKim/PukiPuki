package com.bong.simplynews;

public class NewsPress{
    String name;
    int imageID;
    boolean isSelected;

    public NewsPress(String name, int imageID, boolean isSelected) {
        this.name = name;
        this.imageID = imageID;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
