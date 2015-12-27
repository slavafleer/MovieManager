package com.slavafleer.moviemanager.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.Helper;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbGetMovieAsyncTask;
import com.slavafleer.moviemanager.asynctask.OMDbSearchAsyncTask;
import com.slavafleer.moviemanager.data.Movie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Search activity for displaying list of founded movies by search words
 * and updating the Movie collection by new data on choosing wanted movie.
 */
public class SearchActivity extends AppCompatActivity
        implements OMDbSearchAsyncTask.Callbacks {

    private EditText mEditTextSearchValue;
    private ListView mListViewSearchMovies;
    private ArrayList<Movie> mMovies = new ArrayList<>();

    private ProgressBar mProgressBarSearch;
    private ArrayAdapter<Movie> adapter;

    // Initialize views and listeners.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditTextSearchValue = (EditText) findViewById(R.id.editTextSearchValue);
        mListViewSearchMovies = (ListView) findViewById(R.id.listViewSearchMovies);
        mProgressBarSearch = (ProgressBar) findViewById(R.id.progressBarSearch);

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMovies);
//        mListViewSearchMovies.setAdapter(adapter);

        // Do new internet request for movie plot by concrete movie.
        mListViewSearchMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Uri uri = Uri.parse("http://www.omdbapi.com/?")
                            .buildUpon()
                            .appendQueryParameter("i", mMovies.get(position).getId())
                            .build();
                    URL url = new URL(uri.toString());

                    OMDbGetMovieAsyncTask omDbGetMovieAsyncTask = new OMDbGetMovieAsyncTask(
                            SearchActivity.this, mMovies, position);
                    omDbGetMovieAsyncTask.execute(url);
                } catch (MalformedURLException e) {
                    Toast.makeText(SearchActivity.this, "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Search in internet for movies with user entered words.
    public void buttonGo_onClick(View view) {
        if (Helper.isNetworkAvailable(SearchActivity.this)) {
            try {
                Uri uri = Uri.parse("http://www.omdbapi.com/?")
                        .buildUpon()
                        .appendQueryParameter("s", mEditTextSearchValue.getText().toString().trim())
                        .build();
                URL url = new URL(uri.toString());

                OMDbSearchAsyncTask omdbSearchAsyncTask = new OMDbSearchAsyncTask(this);
                omdbSearchAsyncTask.execute(url);

                // Hide soft keyboard after searching.
                // Check if no view has focus:
                View focusedView = this.getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (MalformedURLException e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SearchActivity.this, R.string.no_internet_connection_warning, Toast.LENGTH_LONG).show();
        }
    }

    // Close activity on Cancel button click.
    public void buttonSearchCancel_onClick(View view) {
        finish();
    }

    // Actions while OMDbSearchAsyncTask is created:
    //
    // Do it before new thread creating.
    public void onAboutToStart() {

        mProgressBarSearch.setVisibility(View.VISIBLE);
    }

    // Bring founded movies to ListView.
    public void onSuccess(ArrayList<Movie> movies) {

        mMovies = movies;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMovies);
        mListViewSearchMovies.setAdapter(adapter);

        mProgressBarSearch.setVisibility(View.INVISIBLE);
    }

    // Show error toast on async task error.
    public void onError(String errorMessage) {

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

        mProgressBarSearch.setVisibility(View.INVISIBLE);
    }
}
