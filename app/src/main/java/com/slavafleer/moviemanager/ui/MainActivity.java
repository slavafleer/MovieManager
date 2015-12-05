/**
 * Movie Manager
 * Application for managing home movie library.
 * This app can show user's movies list on main screen,
 * searching for movie description on internet or
 * filling movie description by user himself.
 */

package com.slavafleer.moviemanager.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.data.Movie;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    private final static int REQUEST_EDITOR = 1;
    private final static int REQUEST_SEARCH = 2;

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

        // When returning from Editor Screen, add/update a movie in the list.
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
        // On returning from Search screen, add new movie in the list.
        else if(requestCode == REQUEST_SEARCH) {

        }
    }

    // PlusIcon on click actions.
    public void imageViewPlusIcon_onClick(View view) {
        // Open Alert Dialog for choosing adding new movie mode: manual or via internet.
        openDialogForPlusIcon();
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

    // Open Alert Dialog on Plus Icon clicking for choosing
    // adding new movie mode: manual or via internet.
    private void openDialogForPlusIcon() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.main_dialog_plus_icon_title)
                .setMessage(R.string.main_dialog_plus_icon_message)
                .setPositiveButton(R.string.main_dialog_plus_icon_positive_button_label,
                        new DialogInterface.OnClickListener() {
                    // On manual -  open Editor Screen.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        intent.putExtra(Constants.KEY_ID, Constants.VALUE_NEW_MOVIE);
                        startActivityForResult(intent, REQUEST_EDITOR);
                    }
                })
                .setNegativeButton(R.string.main_dialog_plus_icon_negative_button_label,
                        new DialogInterface.OnClickListener() {
                    // On search - open search screen.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivityForResult(intent, REQUEST_SEARCH);
                    }
                })
                .setNeutralButton(R.string.main_dialog_plus_icon_neutral_button_label, null)
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
