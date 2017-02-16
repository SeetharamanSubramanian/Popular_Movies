package com.example.seetharaman.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

/**
 * Created by Seetharaman on 09-02-2017.
 */

public class PopularMoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private PopularMoviesDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
//    static final int FAVOURITE = 300;

//    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;
//
//    static{
//        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();
//
//        //This is an inner join which looks like
//        //weather INNER JOIN location ON weather.location_id = location._id
//        sWeatherByLocationSettingQueryBuilder.setTables(
//                PopularMoviesContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
//                        PopularMoviesContract.FavouritesEntry.TABLE_NAME +
//                        " ON " + PopularMoviesContract.FavouritesEntry.TABLE_NAME +
//                        "." + PopularMoviesContract.FavouritesEntry.COLUMN_MOVIE_KEY +
//                        " = " + PopularMoviesContract.MovieEntry.TABLE_NAME +
//                        "." + PopularMoviesContract.MovieEntry.COLUMN_ID);
//    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch(match){
            case MOVIE:
                return PopularMoviesContract.MovieEntry.CONTENT_TYPE;

            case MOVIE_WITH_ID:
                return PopularMoviesContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch(sUriMatcher.match(uri)){

            case MOVIE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_WITH_ID:
                String mSelection = PopularMoviesContract.MovieEntry.COLUMN_ID + " = ?";
                String movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);
                String mSelectionArgs[] = {movieId};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE:
                long id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 )
                    returnUri = PopularMoviesContract.MovieEntry.buildMovieUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
