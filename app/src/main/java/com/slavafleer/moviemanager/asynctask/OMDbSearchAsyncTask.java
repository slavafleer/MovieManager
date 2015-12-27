package com.slavafleer.moviemanager.asynctask;

import android.os.AsyncTask;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This Async Task searches in OMDb data for wanted movie title
 * and returning all movies with positive answer (in Json).
 */
public class OMDbSearchAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

    private Callbacks mCallbacks;
    private int mHttpResponseCode;
    private String mErrorMessage = null;

    public OMDbSearchAsyncTask(Callbacks callbacks) {

        mCallbacks = callbacks;
    }


    // Find views in parent activity and show progress bar before downloading.
    // Initialise adapter for listView in parent activity.
    protected void onPreExecute() {

        mCallbacks.onAboutToStart();
    }

    // Find movies list in internet in new thread.
    protected ArrayList<Movie> doInBackground(URL... params) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = params[0];
            connection = (HttpURLConnection) url.openConnection();
            mHttpResponseCode = connection.getResponseCode();

            if (mHttpResponseCode != HttpURLConnection.HTTP_OK) {
                mErrorMessage = "Error Code: " + mHttpResponseCode +
                        "\nError Message: " + connection.getResponseMessage();

                return null;
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

            ArrayList<Movie> movies = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray search = jsonObject.getJSONArray(Constants.KEY_OMDB_SEARCH);

                for (int i = 0; i < search.length(); i++) {
                    String id = search.getJSONObject(i).getString(Constants.KEY_OMDB_ID);
                    String title = search.getJSONObject(i).getString(Constants.KEY_OMDB_TITLE);
                    movies.add(new Movie(id, title, "", ""));
                }

                return movies;

            } catch (JSONException e) {
                if (e.getMessage().equals("No value for Search")) {
                    mErrorMessage = "Enter correct movie tittle or part of it.";
                } else {
                    mErrorMessage = "Error: " + e.getMessage();
                }

                return null;
            }

        } catch (Exception e) {
            mErrorMessage = "Error: " + e.getMessage();

            return null;

        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {}
        }
    }

    // Read Json result and use received data for showing list of movies
    // in parent activity.
    protected void onPostExecute(ArrayList<Movie> movies) {

        if (mErrorMessage == null) {
            mCallbacks.onSuccess(movies);
        } else {
            mCallbacks.onError(mErrorMessage);
        }
    }

    // Interface for UI callbacks.
    public interface Callbacks {

        void onAboutToStart();

        void onSuccess(ArrayList<Movie> movies);

        void onError(String errorMessage);
    }
}
