package com.example.seetharaman.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seetharaman.popularmovies.Adapters.ReviewAdapter;
import com.example.seetharaman.popularmovies.Adapters.TrailerAdapter;
import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.Objects.Movie;
import com.example.seetharaman.popularmovies.Objects.ReviewObject;
import com.example.seetharaman.popularmovies.Objects.TrailerObject;
import com.example.seetharaman.popularmovies.R;
import com.example.seetharaman.popularmovies.Utilities;
import com.example.seetharaman.popularmovies.data.PopularMoviesContract;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Seetharaman on 04-02-2017.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView tv_title;
    ImageView iv_main;
    TextView tv_releaseDate;
    RatingBar ratingBar;
    TextView plotDescription;
    FloatingActionButton fab_favourite;
    ListView lv_trailers, lv_reviews;
    TextView tv_review;

    private Uri mUri;
    public static final String DETAIL_URI = "URI";
    private static final int MOVIE_LOADER = 0;
    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    private int mMovieId = -1;

    private static final String[] DETAIL_COLUMNS = {
            PopularMoviesContract.MovieEntry.COLUMN_ID,
            PopularMoviesContract.MovieEntry.COLUMN_TITLE,
            PopularMoviesContract.MovieEntry.COLUMN_IS_FAVOURITE,
            PopularMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
            PopularMoviesContract.MovieEntry.COLUMN_OVERVIEW,
            PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            PopularMoviesContract.MovieEntry.COLUMN_REVIEWS,
            PopularMoviesContract.MovieEntry.COLUMN_TRAILERS
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_IS_FAVOURITE = 2;
    public static final int COL_MOVIE_BACKDROP_PATH = 3;
    public static final int COL_MOVIE_OVERVIEW = 4;
    public static final int COL_MOVIE_RELEASE_DATE = 5;
    public static final int COL_MOVIE_VOTE_AVERAGE = 6;
    public static final int COL_MOVIE_REVIEWS = 7;
    public static final int COL_MOVIE_TRAILERS = 8;

    Boolean isFavourite = false;
    private String mTrailers;
    private String mReviews;

    public DetailFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            Log.d(LOG_TAG, "Incoming Uri = "+mUri.toString());
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        tv_title = (TextView)rootView.findViewById(R.id.tv_title);
        iv_main = (ImageView) rootView.findViewById(R.id.iv_main);
        tv_releaseDate = (TextView)rootView.findViewById(R.id.tv_release_date_text);
        ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
        plotDescription = (TextView)rootView.findViewById(R.id.tv_plot_synopsis_text);
        fab_favourite = (FloatingActionButton)rootView.findViewById(R.id.fab_favourite);
        lv_trailers = (ListView)rootView.findViewById(R.id.lv_trailers);
        lv_trailers.setVisibility(View.GONE);
        tv_review = (TextView)rootView.findViewById(R.id.tv_reviews);
        tv_review.setVisibility(View.GONE);
        lv_reviews = (ListView)rootView.findViewById(R.id.lv_reviews);
        lv_reviews.setVisibility(View.GONE);

        if(isFavourite){
            fab_favourite.setImageResource(R.drawable.ic_star_white_24px);
        }
        else
            fab_favourite.setImageResource(R.drawable.ic_star_border_white_24px);

        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavourite){
                    fab_favourite.setImageResource(R.drawable.ic_star_border_white_24px);
                    fab_favourite.animate().rotationBy(72).setDuration(250).start();
                    isFavourite = false;
                }
                else{
                    fab_favourite.setImageResource(R.drawable.ic_star_white_24px);
                    fab_favourite.animate().rotationBy(72).setDuration(250).start();
                    isFavourite = true;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void restartLoader(Uri movieUri){
        mUri = movieUri;
        if(getLoaderManager().hasRunningLoaders() && getLoaderManager().getLoader(MOVIE_LOADER) != null)
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        else
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
//            Log.d(LOG_TAG, "Cursor data: "+ data.toString());
            // Set the movie name
            tv_title.setText(data.getString(COL_MOVIE_TITLE));

            // Set the backdrop image
            String size = "w780";
            String url = getString(R.string.TMDB_image_base_url)+size+"/"+data.getString(COL_MOVIE_BACKDROP_PATH)+"?api_key="+ BuildConfig.TMDB_API_KEY;
            Picasso.with(getContext()).load(url).into(iv_main);

            // Set the release date
            tv_releaseDate.setText(data.getString(COL_MOVIE_RELEASE_DATE));

            // Set the movie rating
            ratingBar.setRating(Float.parseFloat(data.getString(COL_MOVIE_VOTE_AVERAGE))/2);

            // Set the plot description
            plotDescription.setText(data.getString(COL_MOVIE_OVERVIEW));

            // Set the favourite icon
            if(data.getInt(COL_MOVIE_IS_FAVOURITE) > 0){
                isFavourite = true;
                fab_favourite.setImageResource(R.drawable.ic_star_white_24px);
            }
            else{
                isFavourite = false;
                fab_favourite.setImageResource(R.drawable.ic_star_border_white_24px);
            }

            // Store the movie ID
            mMovieId = data.getInt(COL_MOVIE_ID);

            if(!data.getString(COL_MOVIE_TRAILERS).equalsIgnoreCase("null")){
                Log.d(LOG_TAG, "Trailers found");
                showMovieTrailers(getMovieTrailersFromJSON(data.getString(COL_MOVIE_TRAILERS)));
            }
            else{
                Log.d(LOG_TAG, "No trailers found");
                if(Utilities.hasInternet(getContext()))
                    new FetchTrailersTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    Toast.makeText(getContext(), "Connect to the internet to see movie trailers", Toast.LENGTH_SHORT).show();
            }

            if(!data.getString(COL_MOVIE_REVIEWS).equalsIgnoreCase("null")){
                Log.d(LOG_TAG, "Reviews found");
                showMovieReviews(getMovieReviewsFromJSON(data.getString(COL_MOVIE_REVIEWS)));
            }
            else{
                Log.d(LOG_TAG, "No reviews found");
                if(Utilities.hasInternet(getContext()))
                    new FetchReviewsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    Toast.makeText(getContext(), "Connect to the internet to see movie reviews", Toast.LENGTH_SHORT).show();
            }

        }
        else
            Log.d(LOG_TAG, "Cursor returned empty");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onPause() {

        // Update the database on the state of the favourite button
        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.MovieEntry.COLUMN_IS_FAVOURITE, isFavourite?1:0);
        if(mTrailers != null)
            values.put(PopularMoviesContract.MovieEntry.COLUMN_TRAILERS, mTrailers);
        if(mReviews != null)
            values.put(PopularMoviesContract.MovieEntry.COLUMN_REVIEWS, mReviews);
        String where = PopularMoviesContract.MovieEntry.COLUMN_ID + " = ?";
        String whereArgs[] = new String[]{Integer.toString(mMovieId)};

        getContext().getContentResolver().update(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                values,
                where,
                whereArgs
        );

        super.onPause();
    }

    private class FetchTrailersTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            try{
                // Construct the URL for the TMDB query

                final String TMDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";
                final String movieId = Integer.toString(mMovieId);
                String BASE_URL = TMDB_BASE_URL+movieId+"/videos?";

                // Add the API Key to the URI and get the URL
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.TMDB_API_KEY)
                        .build();

                URL requestUrl = new URL(builtUri.toString());
                Request request = new Request.Builder()
                        .url(requestUrl)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful())
                    return response.body().string();
                else
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject responseJson = new JSONObject(response);
                JSONArray trailers = responseJson.getJSONArray("results");
                if(trailers.length() != 0) {
                    mTrailers = trailers.toString();
                    showMovieTrailers(getMovieTrailersFromJSON(trailers.toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private ArrayList<TrailerObject> getMovieTrailersFromJSON(String trailersJSONArray){
        ArrayList<TrailerObject> mMovieTrailers = new ArrayList<>();
        try {
            JSONArray trailers = new JSONArray(trailersJSONArray);
            for(int i = 0; i < trailers.length(); i++){
                TrailerObject trailer = new Gson().fromJson(trailers.get(i).toString(), TrailerObject.class);
                mMovieTrailers.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovieTrailers;
    }

    private void showMovieTrailers(ArrayList<TrailerObject> trailers){
        TrailerAdapter trailerAdapter = new TrailerAdapter(trailers, getActivity());
        lv_trailers.setAdapter(trailerAdapter);
        lv_trailers.setVisibility(View.VISIBLE);
        Utilities.setListViewHeightBasedOnChildren(lv_trailers);
        lv_trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TrailerObject trailer = (TrailerObject)adapterView.getItemAtPosition(position);
//                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                if(YouTubeIntents.isYouTubeInstalled(getContext()) && YouTubeIntents.canResolvePlayVideoIntent(getContext())){
                    Intent intent = YouTubeIntents.createPlayVideoIntent(getContext(), trailer.getTrailerKey());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(), "YouTube not installed or configured correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FetchReviewsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            try {
                // Construct the URL for the TMDB query

                final String TMDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";
                final String movieId = Integer.toString(mMovieId);
                String BASE_URL = TMDB_BASE_URL + movieId + "/reviews?";

                // Add the API Key to the URI and get the URL
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.TMDB_API_KEY)
                        .build();

                URL requestUrl = new URL(builtUri.toString());
                Request request = new Request.Builder()
                        .url(requestUrl)
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful())
                    return response.body().string();
                else
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject responseJson = new JSONObject(response);
                JSONArray reviews = responseJson.getJSONArray("results");
                if(reviews.length() != 0) {
                    mReviews = reviews.toString();
                    showMovieReviews(getMovieReviewsFromJSON(reviews.toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private ArrayList<ReviewObject> getMovieReviewsFromJSON(String trailersJSONArray){
        ArrayList<ReviewObject> mMovieReviews = new ArrayList<>();
        try {
            JSONArray trailers = new JSONArray(trailersJSONArray);
            for(int i = 0; i < trailers.length(); i++){
                ReviewObject trailer = new Gson().fromJson(trailers.get(i).toString(), ReviewObject.class);
                mMovieReviews.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovieReviews;
    }

    private void showMovieReviews(ArrayList<ReviewObject> reviews){
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, getContext());
        lv_reviews.setAdapter(reviewAdapter);
        tv_review.setVisibility(View.VISIBLE);
        lv_reviews.setVisibility(View.VISIBLE);
    }

}
