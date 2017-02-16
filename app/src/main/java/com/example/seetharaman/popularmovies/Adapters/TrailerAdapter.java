package com.example.seetharaman.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seetharaman.popularmovies.Objects.TrailerObject;
import com.example.seetharaman.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by Seetharaman on 16-02-2017.
 */

public class TrailerAdapter extends BaseAdapter {

    private ArrayList<TrailerObject> mTrailers;
    Context mContext;

    public TrailerAdapter(ArrayList<TrailerObject> trailers, Context context) {
        mTrailers = trailers;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int i) {
        return mTrailers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder{
        private TextView trailerName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mViewHolder = null;

        if(view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, viewGroup, false);
            mViewHolder.trailerName = (TextView)view.findViewById(R.id.tv_trailer_name);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.trailerName.setText(mTrailers.get(i).getTrailerName());

        return view;
    }
}
