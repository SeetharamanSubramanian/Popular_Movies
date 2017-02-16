package com.example.seetharaman.popularmovies.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Seetharaman on 12-02-2017.
 */

public class TrailerObject {


    @SerializedName(value = "id")
    String trailerId;

    @SerializedName(value = "key")
    String trailerKey;

    @SerializedName(value = "name")
    String trailerName;

    @SerializedName(value = "iso_639_1")
    String language;

    @SerializedName(value = "iso_3166_1")
    String locale;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public TrailerObject(String language, String locale, String trailerId, String trailerKey, String trailerName) {
        this.language = language;
        this.locale = locale;
        this.trailerId = trailerId;
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
    }

    public TrailerObject() {
    }

    @Override
    public String toString() {
        return new Gson().toJson(new TrailerObject(this.language, this.locale, this.trailerId, this.trailerKey, this.trailerName));
    }
}
