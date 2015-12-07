package com.slavafleer.moviemanager.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.data.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This Async Task searches in OMDb data for wanted movie by id
 * and returning all data for this movie(in Json).
 */
public class OMDbGetMovieAsyncTask extends AsyncTask<URL, Void, String>  {

    private Activity mActivity;
    private ArrayList<Movie> mMovies;
    private ProgressBar mProgressBarSearch;

    public OMDbGetMovieAsyncTask(Activity activity, ArrayList<Movie> movies) {
        mActivity = activity;
        mMovies = movies;
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
                return "Error Code: " + httpResponseCode +
                        "\nError Message: " + connection.getResponseMessage();
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";

            String oneLine = bufferedReader.readLine();

            while(oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String title = jsonObject.getString("Title");
            String plot = jsonObject.getString("Plot");
        } catch (JSONException e) {
            Toast.makeText(mActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mProgressBarSearch.setVisibility(View.INVISIBLE);
    }

}
