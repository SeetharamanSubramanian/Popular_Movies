package com.example.seetharaman.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.R;
import com.example.seetharaman.popularmovies.fragments.ListFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Seetharaman on 16-02-2017.
 */

public class GridViewCursorAdapter extends CursorAdapter {

    private String size = "w500";

    public GridViewCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private static class ViewHolder{
        private final ImageView movieImage;
        private final TextView movieName;

        private ViewHolder(View view){
            movieImage = (ImageView)view.findViewById(R.id.iv_grid_poster);
            movieName = (TextView)view.findViewById(R.id.tv_title);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_movie, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.movieName.setText(cursor.getString(ListFragment.COL_MOVIE_NAME));

        // Build the movie image url
        String url = context.getString(R.string.TMDB_image_base_url)+size+"/"+cursor.getString(ListFragment.COL_MOVIE_IMAGE)+"?api_key="+ BuildConfig.TMDB_API_KEY;
        Picasso.with(context).load(url).into(viewHolder.movieImage);

    }
}
