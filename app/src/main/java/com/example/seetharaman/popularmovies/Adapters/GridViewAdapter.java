package com.example.seetharaman.popularmovies.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seetharaman.popularmovies.BuildConfig;
import com.example.seetharaman.popularmovies.Objects.Movie;
import com.example.seetharaman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Seetharaman on 29-08-2016.
 */
public class GridViewAdapter extends BaseAdapter {

    private static String LOG_TAG = GridViewAdapter.class.getSimpleName();
    ArrayList<Movie> mMovies;
    Context mContext;
    String size = "w500";

    public GridViewAdapter(ArrayList<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        private TextView title;
        private ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mViewHolder = null;

        if(view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_movie, viewGroup, false);
            mViewHolder.imageView = (ImageView) view.findViewById(R.id.iv_grid_poster);
            mViewHolder.title = (TextView)view.findViewById(R.id.tv_title);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        String url = mContext.getString(R.string.TMDB_image_base_url)+size+"/"+mMovies.get(i).getPoster_path()+"?api_key="+ BuildConfig.TMDB_API_KEY;
        Picasso.with(mContext).load(url).into(mViewHolder.imageView);
        mViewHolder.title.setText(mMovies.get(i).getTitle());

        return view;
    }
}
