/**
 * Movie Manager
 * Application for managing home movie library.
 * This app can show user's movies list on main screen,
 * searching for movie description on internet or
 * filling movie description by user himself.
 */

package com.slavafleer.moviemanager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    private final static int REQUEST_EDITOR = 1;

    // Movies Collection.
    private static ArrayList<Movie> mMoviesList = new ArrayList<>();

    private ImageView mImageViewSettingsIcon;
    private ImageView mImageViewPlusIcon;
    private ArrayAdapter<Movie> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageViewSettingsIcon = (ImageView) findViewById(R.id.imageViewSettingsIcon);
        mImageViewPlusIcon = (ImageView) findViewById(R.id.imageViewPlusIcon);

        // ListView adapter initialising.
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMoviesList);
        setListAdapter(mArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_EDITOR) {
            int id = data.getIntExtra(Constants.KEY_ID, 0);
            String subject = data.getStringExtra(Constants.KEY_SUBJECT);
            String body = data.getStringExtra(Constants.KEY_BODY);
            String url = data.getStringExtra(Constants.KEY_URL);
            if(id == Constants.VALUE_NEW_MOVIE) {
                // New Movie in list.
                mMoviesList.add(new Movie(subject, body, url));
            } else {
                // Update the movie in list at id position.
                Movie movie = mMoviesList.get(--id); // First id in Movie List is 1.
                movie.setSubject(subject);
                movie.setBody(body);
                movie.setUrl(url);
            }
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    // PlusIcon on click actions.
    public void imageViewPlusIcon_onClick(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(Constants.KEY_ID, Constants.VALUE_NEW_MOVIE);
        startActivityForResult(intent, REQUEST_EDITOR);
    }

    // On Item click transferring to Editor Screen.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Movie movie = mMoviesList.get(position);
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(Constants.KEY_ID, movie.get_id());
        intent.putExtra(Constants.KEY_SUBJECT, movie.getSubject());
        intent.putExtra(Constants.KEY_BODY, movie.getBody());
        intent.putExtra(Constants.KEY_URL, movie.getUrl());
        startActivityForResult(intent, REQUEST_EDITOR);
    }

}
