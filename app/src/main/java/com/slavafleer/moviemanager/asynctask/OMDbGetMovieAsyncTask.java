package com.slavafleer.moviemanager.asynctask;

import android.app.Activity;
import android.content.Intent;
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
public class OMDbGetMovieAsyncTask extends AsyncTask<URL, Void, String> {

    private Activity mActivity;
    private ArrayList<Movie> mMovies;
    private int mPosition;
    private ProgressBar mProgressBarSearch;

    public OMDbGetMovieAsyncTask(Activity activity, ArrayList<Movie> movies, int position) {
        mActivity = activity;
        mMovies = movies;
        mPosition = position;
    }

    // Find views in parent activity.
    protected void onPreExecute() {
        mProgressBarSearch = (ProgressBar) mActivity.findViewById(R.id.progressBarSearch);
        mProgressBarSearch.setVisibility(View.VISIBLE);
    }

    // Download rest of chosen movie data in new thread.
    protected String doInBackground(URL... params) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = params[0];
            connection = (HttpURLConnection) url.openConnection();
            int httpResponseCode = connection.getResponseCode();

            if (httpResponseCode != HttpURLConnection.HTTP_OK) {
                return "Error Code: " + httpResponseCode +
                        "\nError Message: " + connection.getResponseMessage();
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";

            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
            }
        }
    }

    // Return to parent movie data after finishing downloading.
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String url = jsonObject.getString(Constants.KEY_OMDB_POSTER);
            if (url.equals("N/A")) {
                url = "";
            }
            String body = jsonObject.getString(Constants.KEY_OMDB_PLOT);
            if (body.equals("N/A")) {
                body = "";
            }
            String rating = jsonObject.getString(Constants.KEY_OMDB_METASCORE);
            if (rating.equals("N/A")) {
                rating = "0";
            }

            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_ID, mMovies.get(mPosition).getId());
            intent.putExtra(Constants.KEY_SUBJECT, mMovies.get(mPosition).getSubject());
            intent.putExtra(Constants.KEY_BODY, body);
            intent.putExtra(Constants.KEY_URL, url);
            intent.putExtra(Constants.KEY_RATING, Float.parseFloat(rating) / 10);
            mActivity.setResult(mActivity.RESULT_OK, intent);
        } catch (Exception e) {
            Toast.makeText(mActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        mProgressBarSearch.setVisibility(View.INVISIBLE);
        mActivity.finish();
    }

}
