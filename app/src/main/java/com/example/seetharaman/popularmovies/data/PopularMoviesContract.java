package com.example.seetharaman.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.seetharaman.popularmovies.Objects.MovieObject;

/**
 * Created by Seetharaman on 09-02-2017.
 */

public class PopularMoviesContract {


    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.
    public static final String CONTENT_AUTHORITY = "com.example.seetharaman.popularmovies.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVOURITE = "favourite";

    // Inner class that defines the table contents of the movies table
    public static final class MovieEntry{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        private static String LOG_TAG = MovieEntry.class.getSimpleName();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movies";

        // Columns of the table

        // Integer primary key
        public static final String COLUMN_ID = "_id";

        // String to store the movie name
        public static final String COLUMN_TITLE = "title";

        // String to store the movie description
        public static final String COLUMN_OVERVIEW = "overview";

        // String to store the movie release date
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // String to store the url associated with the thumbnail poster
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // String to store the url for the large poster
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        // String to store the float vote average
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // String to store the filter associated with the movie
        public static final String COLUMN_FILTER = "filter";

        // String to store the boolean whether the movie is a favourite or not
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";

        // String to store the movie reviews in JSON format
        public static final String COLUMN_REVIEWS = "reviews";

        // String to store the various movie trailer urls
        public static final String COLUMN_TRAILERS = "trailers";

        public static String getMovieIdFromUri(Uri uri){
//            Log.d(LOG_TAG, "Uri path segments: " + uri.getPathSegments().toString());
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieUri(long id){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }

        public static ContentValues getContentValuesForUpdate(MovieObject movie, String filter, boolean isFavourite){
            ContentValues values = new ContentValues();

            values.put(COLUMN_BACKDROP_PATH, movie.getLargeImageUrl());
            values.put(COLUMN_FILTER, filter);
            values.put(COLUMN_IS_FAVOURITE, isFavourite? 1:0);
            values.put(COLUMN_OVERVIEW, movie.getDescription());
            values.put(COLUMN_POSTER_PATH, movie.getListImageUrl());
            values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(COLUMN_TITLE, movie.getMovieName());
            values.put(COLUMN_VOTE_AVERAGE, Float.toString(movie.getRating()));

            return values;
        }

        public static ContentValues getContentValuesForInsert(MovieObject movie, String filter, boolean isFavourite){
            ContentValues values = new ContentValues();

            values.put(COLUMN_BACKDROP_PATH, movie.getLargeImageUrl());
            values.put(COLUMN_FILTER, filter);
            values.put(COLUMN_IS_FAVOURITE, isFavourite? 1:0);
            values.put(COLUMN_OVERVIEW, movie.getDescription());
            values.put(COLUMN_POSTER_PATH, movie.getListImageUrl());
            values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(COLUMN_TITLE, movie.getMovieName());
            values.put(COLUMN_VOTE_AVERAGE, Float.toString(movie.getRating()));
            values.put(COLUMN_ID, movie.getMovieId());
            values.put(COLUMN_REVIEWS, "NULL");
            values.put(COLUMN_TRAILERS, "NULL");

            return values;
        }

    }

//    // Inner class that defines the table contents of the favourites table
//    public static final class FavouritesEntry implements BaseColumns{
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
//
//        // Table name
//        public static final String TABLE_NAME = "favourites";
//
//        public static final String COLUMN_MOVIE_KEY = "movie_id";
//        public static final String COLUMN_REVIEWS = "reviews";
//        public static final String COLUMN_TRAILERS = "trailers";
//
//    }

}
