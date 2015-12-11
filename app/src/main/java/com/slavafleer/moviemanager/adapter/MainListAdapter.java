package com.slavafleer.moviemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.data.Movie;

import java.util.ArrayList;

/**
 * Custom adapter for list view in Main Activity.
 * Created for using "ellipsize" on titles of movies.
 */
public class MainListAdapter extends ArrayAdapter<Movie> {

    private Activity mActivity;
    private ArrayList<Movie> mMovies;
    private LayoutInflater mLayoutInflater;

    public MainListAdapter(Activity activity, ArrayList<Movie> movies) {
        super(activity, 0, movies);

        mActivity = activity;
        mMovies = movies;

        mLayoutInflater = (LayoutInflater)activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    // This function will be called for each item in the list, during the list creation:
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = mMovies.get(position);
        View itemLayout = mLayoutInflater.inflate(R.layout.adapter_main_listview_item, null);
        TextView textViewTitle = (TextView) itemLayout.findViewById(
                R.id.textViewMainListItemTitle);
        textViewTitle.setText(movie.getSubject());

        int rating = (int)movie.getRating();
        int color;
        switch(rating) {
            case 1:
                color = ContextCompat.getColor(mActivity, R.color.colorRating1);
                break;
            case 2:
                color = ContextCompat.getColor(mActivity, R.color.colorRating2);
                break;
            case 3:
                color = ContextCompat.getColor(mActivity, R.color.colorRating3);
                break;
            case 4:
                color = ContextCompat.getColor(mActivity, R.color.colorRating4);
                break;
            case 5:
                color = ContextCompat.getColor(mActivity, R.color.colorRating5);
                break;
            case 6:
                color = ContextCompat.getColor(mActivity, R.color.colorRating6);
                break;
            case 7:
                color = ContextCompat.getColor(mActivity, R.color.colorRating7);
                break;
            case 8:
                color = ContextCompat.getColor(mActivity, R.color.colorRating8);
                break;
            case 9:
                color = ContextCompat.getColor(mActivity, R.color.colorRating9);
                break;
            case 10:
                color = ContextCompat.getColor(mActivity, R.color.colorRating10);
                break;
            default:
                color = ContextCompat.getColor(mActivity, R.color.colorRatingTransparent);
                break;
        }
        textViewTitle.setBackgroundColor(color);
        return itemLayout;
    }
}
