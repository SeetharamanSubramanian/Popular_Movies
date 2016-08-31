package com.example.seetharaman.popularmovies.Activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.Objects.Movie;
import com.example.seetharaman.popularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static String MOVIE_EXTRA = "movie";
    private static String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the movie clicked on from the intent
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(MOVIE_EXTRA);

        // Set the movie name
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(movie.getTitle());

        // Set the backdrop image
        ImageView iv_main = (ImageView) findViewById(R.id.iv_main);
        String size = "w780";
        String url = getString(R.string.TMDB_image_base_url)+size+"/"+movie.getBackdropPath()+"?api_key="+ BuildConfig.TMDB_API_KEY;
        Picasso.with(this).load(url).into(iv_main);

        // Set the release date
        TextView tv_releaseDate = (TextView)findViewById(R.id.tv_release_date_text);
        tv_releaseDate.setText(movie.getReleaseDate());

        // Set the movie rating
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setRating(movie.getVoteAverage()/2);

        // Set the plot description
        TextView plotDescription = (TextView)findViewById(R.id.tv_plot_synopsis_text);
        plotDescription.setText(movie.getOverview());

    }
}
