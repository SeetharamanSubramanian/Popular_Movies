package com.example.seetharaman.popularmovies.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Seetharaman on 12-02-2017.
 */

public class MovieObject {

    @SerializedName( value = "poster_path")
    String listImageUrl;

    @SerializedName( value = "overview")
    String description;

    @SerializedName( value = "release_date")
    String releaseDate;

    @SerializedName( value = "id")
    int movieId;

    @SerializedName(value = "title")
    String movieName;

    @SerializedName(value = "backdrop_path")
    String largeImageUrl;

    @SerializedName(value = "vote_average")
    float rating;

    String filter;

    boolean isFavourite;

    ArrayList<ReviewObject> reviews;

    ArrayList<TrailerObject> trailers;

    public MovieObject(String description, String filter, boolean isFavourite, String largeImageUrl, String listImageUrl, int movieId, String movieName, float rating, String releaseDate, ArrayList<ReviewObject> reviews, ArrayList<TrailerObject> trailers) {
        if(this.reviews == null)
            this.reviews = new ArrayList<>();
        if(this.trailers == null)
            this.trailers = new ArrayList<>();
        this.description = description;
        this.filter = filter;
        this.isFavourite = isFavourite;
        this.largeImageUrl = largeImageUrl;
        this.listImageUrl = listImageUrl;
        this.movieId = movieId;
        this.movieName = movieName;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.reviews = reviews;
        this.trailers = trailers;
    }

    public MovieObject() {
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<ReviewObject> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewObject> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<TrailerObject> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<TrailerObject> trailers) {
        this.trailers = trailers;
    }

    @Override
    public String toString() {
        return new Gson().toJson(new MovieObject(this.description, this.filter, this.isFavourite, this.largeImageUrl, this.listImageUrl, this.movieId, this.movieName, this.rating,this.releaseDate, this.reviews, this.trailers));
    }
}
