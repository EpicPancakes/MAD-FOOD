package com.example.onlyfoods;

import java.util.Date;

public class ReviewGP {



    private String name;
    private String reviewMsg;
    private Date date;


    private int image;

    public ReviewGP(String name, String reviewMsg, Date date, int image) {
        this.name = name;
        this.reviewMsg = reviewMsg;
        this.date = date;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewMsg() {
        return reviewMsg;
    }

    public void setReviewMsg(String reviewMsg) {
        this.reviewMsg = reviewMsg;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
