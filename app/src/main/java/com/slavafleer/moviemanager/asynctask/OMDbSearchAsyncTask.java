package com.slavafleer.moviemanager.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.slavafleer.moviemanager.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This Async Task searches in OMDb data for wanted movie title
 * and returning all movies with positive answer (in Json).
 */
public class OMDbSearchAsyncTask extends AsyncTask<URL, Void, String> {

    private Activity mActivity;
    private ProgressBar mProgressBarSearch;

    public OMDbSearchAsyncTask(Activity activity) {
        mActivity = activity;
    }


    @Override
    protected void onPreExecute() {
        mProgressBarSearch = (ProgressBar)mActivity.findViewById(R.id.progressBarSearch);
        mProgressBarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int httpResponseCode = connection.getResponseCode();

            if(httpResponseCode != HttpURLConnection.HTTP_OK) {
                return "Error: " + connection.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
