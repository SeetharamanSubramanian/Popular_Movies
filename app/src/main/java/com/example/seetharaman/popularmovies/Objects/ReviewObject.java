package com.example.seetharaman.popularmovies.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Seetharaman on 12-02-2017.
 */

public class ReviewObject {

    @SerializedName(value = "id")
    String reviewId;

    @SerializedName(value = "author")
    String author;

    @SerializedName(value = "content")
    String review;

    @SerializedName(value = "url")
    String reviewUrl;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public ReviewObject(String author, String review, String reviewId, String reviewUrl) {
        this.author = author;
        this.review = review;
        this.reviewId = reviewId;
        this.reviewUrl = reviewUrl;
    }

    public ReviewObject() {
    }

    @Override
    public String toString() {
        return new Gson().toJson(new ReviewObject(this.author, this.review, this.reviewId, this.reviewUrl));
    }
}
