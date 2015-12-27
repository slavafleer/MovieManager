package com.slavafleer.moviemanager.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.data.Movie;

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

    private Callbacks mCallbacks;
    private String mErrorMessage;


    public OMDbGetMovieAsyncTask(Callbacks callbacks {

        mCallbacks = callbacks;
    }

    // Find views in parent activity.
    protected void onPreExecute() {

        mCallbacks.onAboutToStart();
    }

    // Download rest of chosen movie data in new thread.
    protected String doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int httpResponseCode = connection.getResponseCode();

            if(httpResponseCode != HttpURLConnection.HTTP_OK) {
                mErrorMessage = "Error Code: " + httpResponseCode +
                        "\nError Message: " + connection.getResponseMessage();

                return null;
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

            try {
                JSONObject jsonObject = new JSONObject(result);
                String id = jsonObject.getString(Constants.KEY_OMDB_ID);
                String urlAsString = jsonObject.getString(Constants.KEY_OMDB_POSTER);
                if(urlAsString.equals("N/A")) {
                    urlAsString = "";
                }
                String body = jsonObject.getString(Constants.KEY_OMDB_PLOT);
                if(body.equals("N/A")) {
                    body = "";
                }
                String rating = jsonObject.getString(Constants.KEY_OMDB_METASCORE);
                if(rating.equals("N/A")) {
                    rating = "0";
                }

                Movie movie = new Movie()

                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_ID, mMovies.get(mPosition).getId());
                intent.putExtra(Constants.KEY_SUBJECT, mMovies.get(mPosition).getSubject());
                intent.putExtra(Constants.KEY_BODY, body);
                intent.putExtra(Constants.KEY_URL, urlAsString);
                intent.putExtra(Constants.KEY_RATING, Float.parseFloat(rating) / 10);
                mActivity.setResult(mActivity.RESULT_OK, intent);
            } catch (Exception e) {
                Toast.makeText(mActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            mProgressBarSearch.setVisibility(View.INVISIBLE);
            mActivity.finish();

            return movie;
        } catch (Exception e) {
            mErrorMessage = "Error: " + e.getMessage();

            return null;
        }
    }

    // Return to parent movie data after finishing downloading.
    protected void onPostExecute(Movie movie) {

    }

    // Interface for UI callbacks.
    public interface Callbacks {

        void onAboutToStart();
        void onSuccess(Movie movie);
        void onError(String errorMessage);
    }
}
