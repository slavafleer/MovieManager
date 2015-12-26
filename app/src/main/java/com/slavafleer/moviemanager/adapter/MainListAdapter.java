package com.slavafleer.moviemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbImageDownloaderAsyncTask;
import com.slavafleer.moviemanager.data.Movie;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Custom adapter for list view in Main Activity.
 */
public class MainListAdapter extends ArrayAdapter<Movie>
        implements OMDbImageDownloaderAsyncTask.Callbacks {

    private Activity mActivity;
    private ArrayList<Movie> mMovies;
    private LayoutInflater mLayoutInflater;
    private ProgressBar mProgressBar;
    private ImageView mImageViewPoster;

    public MainListAdapter(Activity activity, ArrayList<Movie> movies) {
        super(activity, 0, movies);

        mActivity = activity;
        mMovies = movies;

        mLayoutInflater = (LayoutInflater)activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    // This function will be called for each item in the list, during the list creation
    // and filling all Views with relevant data.
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = mMovies.get(position);
        View itemLayout = mLayoutInflater.inflate(R.layout.adapter_main_listview_item, null);
        TextView textViewTitle = (TextView) itemLayout.findViewById(
                R.id.textViewMainListItemTitle);
        textViewTitle.setText(movie.getSubject());

        // Choose color by Rating.
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
//        textViewTitle.setBackgroundColor(color);
        LinearLayout linearLayoutRoot = (LinearLayout)
                itemLayout.findViewById(R.id.linearLaoutItemRoot);
        linearLayoutRoot.setBackgroundColor(color);

        RatingBar ratingBar = (RatingBar) itemLayout.findViewById(R.id.ratingBarItem);
        /** Rating Bar Bug!!!
         * Next code prevents bug of filling whole star even if rating data is integer.
         * It happens on some "old" apis as 4.2.2 and 4.3, maybe more.
         * On api 5.* it's not needed, cause have no such bug.
         * The prevent this bug, need to rewrite rating data again with some change.
         * It must be on same star!!!
         * */
        float ratingFloat = rating / 2f;
        if((ratingFloat * 10) % 10 != 0 ) {
            ratingBar.setRating(ratingFloat + 0.5f);
        }
        ratingBar.setRating(ratingFloat);

        boolean isWatched = movie.getIsWatched();
        ImageView imageViewIsWatched = (ImageView)
                itemLayout.findViewById(R.id.imageViewItemIsWatched);
        if(isWatched) {
            imageViewIsWatched.setVisibility(View.VISIBLE);
        } else {
            imageViewIsWatched.setVisibility(View.INVISIBLE);
        }

        // Getting poster from internet.
        mImageViewPoster = (ImageView) itemLayout.findViewById(R.id.imageViewItemPoster);
        mProgressBar = (ProgressBar) itemLayout.findViewById(R.id.progressBarItem);
        OMDbImageDownloaderAsyncTask omDbImageDownloaderAsyncTask =
                new OMDbImageDownloaderAsyncTask(this);

        try {
            URL url = new URL(movie.getUrl());
            omDbImageDownloaderAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            mImageViewPoster.setImageResource(R.drawable.movie_clapper_icon);
        }

        return itemLayout;
    }

    // Actions while OMDbImageDownloaderAsyncTask is created.
    // Do it before new thread creating.
    public void onAboutToStart() {

        // Show Progress bar while downloading.
        mProgressBar.setVisibility(View.VISIBLE);
    }

    // Set image in layout and hide progress bar.
    public void onImageDownloadSuccess(Bitmap bitmap) {

        mImageViewPoster.setImageBitmap(bitmap);

        // Hide progress bar.
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    // Set different images on errors from OMDbImageDownloaderAsyncTask.
    public void onImageDownloadError(int httpStatusCode, String errorMessage) {

        if (httpStatusCode != HttpURLConnection.HTTP_OK) {
            mImageViewPoster.setImageResource(R.drawable.movie_clapper_icon);
        }

        // Hide progress bar.
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
