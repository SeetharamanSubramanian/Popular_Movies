package com.example.seetharaman.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seetharaman.popularmovies.Objects.ReviewObject;
import com.example.seetharaman.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by Seetharaman on 16-02-2017.
 */

public class ReviewAdapter extends BaseAdapter {

    private ArrayList<ReviewObject> mReviews;
    Context mContext;

    public ReviewAdapter(ArrayList<ReviewObject> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder{
        private TextView reviewAuthor;
        private TextView reviewBody;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mViewHolder = null;

        if(view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_review, viewGroup, false);
            mViewHolder.reviewAuthor = (TextView)view.findViewById(R.id.tv_author);
            mViewHolder.reviewBody = (TextView)view.findViewById(R.id.tv_review_text);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.reviewAuthor.setText(mReviews.get(i).getAuthor());
        mViewHolder.reviewBody.setText(mReviews.get(i).getReview());

        return view;
    }

}
