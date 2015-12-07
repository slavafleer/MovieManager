package com.slavafleer.moviemanager.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbGetMovieAsyncTask;
import com.slavafleer.moviemanager.asynctask.OMDbSearchAsyncTask;
import com.slavafleer.moviemanager.data.Movie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText mEditTextSearchValue;
    private ListView mListViewSearchMovies;
    private ArrayList<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditTextSearchValue = (EditText)findViewById(R.id.editTextSearchValue);
        mListViewSearchMovies = (ListView)findViewById(R.id.listViewSearchMovies);

        mListViewSearchMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
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

    public void buttonGo_onClick(View view) {
        try {
            Uri uri = Uri.parse("http://www.omdbapi.com/?")
                    .buildUpon()
                    .appendQueryParameter("s", mEditTextSearchValue.getText().toString())
                    .build();
            URL url = new URL(uri.toString());

            OMDbSearchAsyncTask omdbSearchAsyncTask = new OMDbSearchAsyncTask(this, mMovies);
            omdbSearchAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void buttonSearchCancel_onClick(View view) {
        finish();
    }
}
