package com.example.seetharaman.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.Objects.MovieObject;
import com.example.seetharaman.popularmovies.R;
import com.example.seetharaman.popularmovies.data.PopularMoviesContract;
import com.example.seetharaman.popularmovies.data.PopularMoviesDbHelper;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.example.seetharaman.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Seetharaman on 12-02-2017.
 */

public class PopMoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private String LOG_TAG = PopMoviesSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60*60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private String[] deleteProjection = {
            MovieEntry.COLUMN_ID,
            MovieEntry.COLUMN_IS_FAVOURITE
    };

    private int DELETE_INDEX_ID = 0;
    private int DELETE_INDEX_IS_FAVOURITE = 1;
    private ArrayList<String> favouriteMoviesIds;

    public PopMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        favouriteMoviesIds = new ArrayList<>();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "Sync started");

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        try{
            // Construct the URL for the TMDB query

            final String TMDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";

            // Getting popular movies list here
            String sortByPopular = "popular";
            String BASE_URL = TMDB_BASE_URL+sortByPopular+"?";

            // Add the API Key to the URI and get the URL
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.TMDB_API_KEY)
                    .build();

            URL requestUrl = new URL(builtUri.toString());
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();
            Response response = client.newCall(request).execute();

            // Getting top rated movies list here
            String sortByTopRated = "top_rated";
            BASE_URL = TMDB_BASE_URL+sortByTopRated+"?";

            // Add the API Key to the URI and get the URL
            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.TMDB_API_KEY)
                    .build();

            requestUrl = new URL(builtUri.toString());
            Request secondRequest = new Request.Builder()
                    .url(requestUrl)
                    .build();
            Response secondResponse = client.newCall(secondRequest).execute();

            if(response.isSuccessful() && secondResponse.isSuccessful()) {
                clearExistingTable();
                storePopularMovies(response.body().string());
                storeTopRatedMovies(secondResponse.body().string());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Method to clear the existing database, keeping only the favourite movies
     */
    private void clearExistingTable(){

        if(favouriteMoviesIds != null)
            favouriteMoviesIds.clear();
        else
            favouriteMoviesIds = new ArrayList<>();

        Cursor cursor = getContext().getContentResolver().query(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                deleteProjection,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            do {
                if(cursor.getInt(DELETE_INDEX_IS_FAVOURITE) > 0){
                    favouriteMoviesIds.add(Integer.toString(cursor.getInt(DELETE_INDEX_ID)));
                }
            }while(cursor.moveToNext());
        }
        getContext().getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                MovieEntry.COLUMN_ID + " != ?",
                favouriteMoviesIds.toArray(new String[favouriteMoviesIds.size()])
        );
    }

    /**
     * Method to store the popular movies to the database
     */
    private void storePopularMovies(String responseString){

        Vector<ContentValues> insertRowscVVector = new Vector<>();

        try {
            JSONObject responseJson = new JSONObject(responseString);
            JSONArray moviesJsonArray = responseJson.getJSONArray("results");
            for(int i = 0; i < moviesJsonArray.length(); i++){
                MovieObject movieObject = new Gson().fromJson(moviesJsonArray.get(i).toString(), MovieObject.class);
                if(favouriteMoviesIds.contains(Integer.toString(movieObject.getMovieId()))){
                    ContentValues values = (MovieEntry.getContentValuesForUpdate(movieObject, "popular", true));
                    getContext().getContentResolver().update(
                            MovieEntry.CONTENT_URI,
                            values,
                            MovieEntry.COLUMN_ID + " = ?",
                            new String[] {Integer.toString(movieObject.getMovieId())}
                    );
                }
                else
                    insertRowscVVector.add(MovieEntry.getContentValuesForInsert(movieObject, "popular", false));
            }
            getContext().getContentResolver().bulkInsert(
                    MovieEntry.CONTENT_URI,
                    insertRowscVVector.toArray(new ContentValues[insertRowscVVector.size()])
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to store the popular movies to the database
     */
    private void storeTopRatedMovies(String responseString){

        Vector<ContentValues> insertRowscVVector = new Vector<>();

        try {
            JSONObject responseJson = new JSONObject(responseString);
            JSONArray moviesJsonArray = responseJson.getJSONArray("results");
            for(int i = 0; i < moviesJsonArray.length(); i++){
                MovieObject movieObject = new Gson().fromJson(moviesJsonArray.get(i).toString(), MovieObject.class);
                if(favouriteMoviesIds.contains(Integer.toString(movieObject.getMovieId()))){
                    ContentValues values = (MovieEntry.getContentValuesForUpdate(movieObject, "top_rated", true));
                    getContext().getContentResolver().update(
                            MovieEntry.CONTENT_URI,
                            values,
                            MovieEntry.COLUMN_ID + " = ?",
                            new String[] {Integer.toString(movieObject.getMovieId())}
                    );
                }
                else
                    insertRowscVVector.add(MovieEntry.getContentValuesForInsert(movieObject, "top_rated", false));
            }
            getContext().getContentResolver().bulkInsert(
                    MovieEntry.CONTENT_URI,
                    insertRowscVVector.toArray(new ContentValues[insertRowscVVector.size()])
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PopMoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
