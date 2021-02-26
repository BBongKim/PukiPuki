package com.bong.simplynews;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class NewsItem implements Serializable, Comparable<NewsItem> {

    private String title;
    private String link="";
    private String description;
    private String time="";
    private long timeStamp;
    private String imageLink="";

    public NewsItem() {}

    public NewsItem(String title, String link, String description, String time, long timeStamp, String imageLink) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.time = time;
        this.timeStamp = timeStamp;
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public int compareTo(NewsItem o) {
        if(this.getTimeStamp() < o.getTimeStamp()) {
            return -1;
        } else if(this.getTimeStamp() > o.getTimeStamp()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean sameNews = false;

        if(obj != null && obj instanceof NewsItem) {
            sameNews = this.link.equals(((NewsItem) obj).getLink());
        }
        return sameNews;
    }
}
