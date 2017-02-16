package com.example.seetharaman.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.seetharaman.popularmovies.data.PopularMoviesContract.*;

/**
 * Created by Seetharaman on 09-02-2017.
 */

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "weather.db";

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "+ MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_FILTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IS_FAVOURITE + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TRAILERS + " TEXT, " +
                MovieEntry.COLUMN_REVIEWS + " TEXT" +
//                " UNIQUE (" + MovieEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE" +
                ");";

//        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +
//                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                FavouritesEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
//                FavouritesEntry.COLUMN_REVIEWS + " TEXT, " +
//                FavouritesEntry.COLUMN_TRAILERS + " TEXT," +
//
//                " FOREIGN KEY (" + FavouritesEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
//                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + "), " +
//
//                " UNIQUE (" + FavouritesEntry.COLUMN_MOVIE_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
