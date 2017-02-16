package com.example.seetharaman.popularmovies.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.seetharaman.popularmovies.Activities.DetailActivity;
import com.example.seetharaman.popularmovies.Activities.MainActivity;
import com.example.seetharaman.popularmovies.Adapters.GridViewAdapter;
import com.example.seetharaman.popularmovies.Adapters.GridViewCursorAdapter;
import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.Objects.Movie;
import com.example.seetharaman.popularmovies.R;
import com.example.seetharaman.popularmovies.Utilities;
import com.example.seetharaman.popularmovies.data.PopularMoviesContract;
import com.example.seetharaman.popularmovies.sync.PopMoviesSyncAdapter;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Seetharaman on 04-02-2017.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public ArrayList<Movie> mMovies = new ArrayList<>();
    GridView gridView;
    GridViewCursorAdapter mMovieAdapter;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private static final int MOVIE_LOADER = 0;

    private String LOG_TAG = ListFragment.class.getSimpleName();

    private int mMovieFilter;
    private final int FILTER_POPULAR = 0;
    private final int FILTER_TOP_RATED = 1;
    private final int FILTER_FAVOURITES = 2;

    private final String[] projection = {
            PopularMoviesContract.MovieEntry.COLUMN_ID,
            PopularMoviesContract.MovieEntry.COLUMN_TITLE,
            PopularMoviesContract.MovieEntry.COLUMN_POSTER_PATH
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_NAME = 1;
    public static final int COL_MOVIE_IMAGE = 2;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri movieUri);

//        public void onLoadCompleted(Uri movieUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        mMovieAdapter = new GridViewCursorAdapter(getActivity(), null,0);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(view.getContext(), DetailActivity.class);
//                intent.putExtra(DetailActivity.MOVIE_EXTRA, mMovies.get(i));
//                startActivity(intent);
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                if(cursor != null) {
                    ((Callback) getActivity()).onItemSelected(PopularMoviesContract.MovieEntry.buildMovieUri(cursor.getInt(COL_MOVIE_ID)));
                }
                mPosition = position;
                Log.d(LOG_TAG, "position set: " + mPosition);
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            Log.d(LOG_TAG, "recovered position: "+mPosition);
        }
        else if(savedInstanceState == null){
            Log.d(LOG_TAG, "Saved instance state null");
        }
        else
            Log.d(LOG_TAG, "key "+SELECTED_KEY + " not found");

        mMovieFilter = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.pref_filter_key), Integer.parseInt(getString(R.string.pref_filter_default)));

        return rootView;

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            Log.d(LOG_TAG, "recovered position later: "+mPosition);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if phone has a network connection
//        if(Utilities.hasInternet(getActivity())) {
//            getMoviesFromDB();
////            PopMoviesSyncAdapter.syncImmediately(getActivity());
//        }
//        else{
//
//            // Create a dialog to allow the user to navigate to settings menu
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(R.string.dialog_error_internet_title);
//            builder.setMessage(R.string.msg_error_internet);
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Launch the settings menu
//                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                }
//            });
//            builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Close the app
//                    getActivity().finish();
//                }
//            });
//            builder.show();
//        }
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu__fragment_list, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
            Log.d(LOG_TAG, "Saved position: "+mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_filter){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.action_filter));
            builder.setSingleChoiceItems(R.array.pref_filter_list, mMovieFilter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setMovieListFilter(i);
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMovieListFilter(int selection){
        mMovieFilter = selection;
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection;
        String[] selectionArgs;

        switch (mMovieFilter){
            case FILTER_POPULAR:
                selection = PopularMoviesContract.MovieEntry.COLUMN_FILTER + " = ?";
                selectionArgs = new String[]{"popular"};
                return new CursorLoader(getActivity(),
                        PopularMoviesContract.MovieEntry.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null
                );

            case FILTER_TOP_RATED:
                selection = PopularMoviesContract.MovieEntry.COLUMN_FILTER + " = ?";
                selectionArgs = new String[]{"top_rated"};
                return new CursorLoader(
                        getActivity(),
                        PopularMoviesContract.MovieEntry.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null
                );

            case FILTER_FAVOURITES:
                selection = PopularMoviesContract.MovieEntry.COLUMN_IS_FAVOURITE + " = ?";
                selectionArgs = new String[]{"1"};
                return new CursorLoader(
                        getActivity(),
                        PopularMoviesContract.MovieEntry.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null
                );

            default:throw new UnsupportedOperationException(" Unknown selection");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst())
            PopMoviesSyncAdapter.syncImmediately(getContext());
        else {
            mMovieAdapter.swapCursor(data);
        }
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
        Log.d(LOG_TAG, "Current position = "+mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onPause() {

        // Store the filter preference in the shared preferences
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(getString(R.string.pref_filter_key), mMovieFilter).apply();

        super.onPause();
    }
}
