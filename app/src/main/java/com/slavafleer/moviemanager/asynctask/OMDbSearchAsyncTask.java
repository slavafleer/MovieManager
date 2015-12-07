package com.slavafleer.moviemanager.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
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
public class OMDbSearchAsyncTask extends AsyncTask<URL, Void, String> {

    private Activity mActivity;
    private ProgressBar mProgressBarSearch;
    private ArrayAdapter<Movie> adapter;
//    private ArrayList<String> mMovies = new ArrayList<>();
    private ArrayList<Movie> mMovies;
    private ListView mListViewMovies;

    public OMDbSearchAsyncTask(Activity activity, ArrayList<Movie> movies) {
        mActivity = activity;
        mMovies = movies;
    }


    @Override
    protected void onPreExecute() {
        mProgressBarSearch = (ProgressBar)mActivity.findViewById(R.id.progressBarSearch);
        mListViewMovies = (ListView)mActivity.findViewById(R.id.listViewSearchMovies);

        mProgressBarSearch.setVisibility(View.VISIBLE);

        adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, mMovies);
        mListViewMovies.setAdapter(adapter);
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
            JSONArray search = jsonObject.getJSONArray(Constants.KEY_OMDB_SEARCH);

            for(int i = 0; i < search.length(); i++) {
                String id = search.getJSONObject(i).getString(Constants.KEY_OMDB_ID);
                String title = search.getJSONObject(i).getString(Constants.KEY_OMDB_TITLE);
                mMovies.add(new Movie(id, title, "", ""));
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(mActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mProgressBarSearch.setVisibility(View.INVISIBLE);
    }
}
