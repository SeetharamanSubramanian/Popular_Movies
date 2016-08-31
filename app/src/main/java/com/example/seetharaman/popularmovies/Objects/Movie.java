package com.example.seetharaman.popularmovies.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Seetharaman on 29-08-2016.
 */
public class Movie implements Serializable{

    private static String POSTER_PATH = "poster_path";
    private static String ADULT = "adult";
    private static String OVERVIEW = "overview";
    private static String RELEASE_DATE = "release_date";
    private static String GENRE_IDS = "genre_ids";
    private static String ID = "id";
    private static String ORIGINAL_TITLE = "original_title";
    private static String ORIGINAL_LANGUAGE = "original_language";
    private static String TITLE = "title";
    private static String BACKDROP_PATH = "backdrop_path";
    private static String POPULARITY = "popularity";
    private static String VOTE_COUNT = "vote_count";
    private static String VIDEO = "video";
    private static String VOTE_AVERAGE = "vote_average";



    String poster_path;
    Boolean isAdult;
    String overview;
    String releaseDate;
    int[] genreIds;
    int id;
    String originalTitle;
    String originalLanguage;
    String title;
    String backdropPath;
    float popularity;
    int voteCount;
    Boolean video;
    float voteAverage;

    public Boolean getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(Boolean isAdult) {
        this.isAdult = isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public static Movie getfromJSON(JSONObject movieJSON){
        Movie movie = new Movie();
        try {
            movie.setPoster_path(movieJSON.getString(POSTER_PATH));
            movie.setIsAdult(movieJSON.getBoolean(ADULT));
            movie.setOverview(movieJSON.getString(OVERVIEW));
            movie.setReleaseDate(movieJSON.getString(RELEASE_DATE));
            movie.setId(movieJSON.getInt(ID));
            movie.setOriginalTitle(movieJSON.getString(ORIGINAL_TITLE));
            movie.setOriginalLanguage(movieJSON.getString(ORIGINAL_LANGUAGE));
            movie.setTitle(movieJSON.getString(TITLE));
            movie.setBackdropPath(movieJSON.getString(BACKDROP_PATH));
            movie.setPopularity(movieJSON.getLong(POPULARITY));
            movie.setVoteCount(movieJSON.getInt(VOTE_COUNT));
            movie.setVideo(movieJSON.getBoolean(VIDEO));
            movie.setVoteAverage(movieJSON.getLong(VOTE_AVERAGE));
            JSONArray genreIds = movieJSON.getJSONArray(GENRE_IDS);
            int[] tempids = new int[genreIds.length()];
            for (int i = 0;i<genreIds.length(); i++)
                tempids[i] = genreIds.getInt(i);
            movie.setGenreIds(tempids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

}
