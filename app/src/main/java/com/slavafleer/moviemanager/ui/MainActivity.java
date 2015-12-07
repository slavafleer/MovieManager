/**
 * Movie Manager
 * Application for managing home movie library.
 * This app can show user's movies list on main screen,
 * searching for movie description on internet or
 * filling movie description by user himself.
 */

// TODO: Think if saving file must be as AsyncTask or cause it not time eater live it in main thread.

package com.slavafleer.moviemanager.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.data.FileManager;
import com.slavafleer.moviemanager.data.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_EDITOR = 1;
    private final static int REQUEST_SEARCH = 2;

    private final static String FILE_NAME = "movieslist.txt";

    // Movies Collection.
    private ArrayList<Movie> mMoviesList = new ArrayList<>();

    private ListView mListViewMovies;
    private TextView mEmptyMainMovieList;
    private ArrayAdapter<Movie> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading movie list from the file, if failed - reset the array list.
        if(FileManager.loadFile(this, FILE_NAME, mMoviesList) == FileManager.RESULT_ERROR) {
            mMoviesList = new ArrayList<>();
        }

        mListViewMovies = (ListView)findViewById(R.id.listViewMainMovies);
        mEmptyMainMovieList = (TextView)findViewById(R.id.emptyMainMovieList);

        // ListView adapter initialising.
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMoviesList);
        mListViewMovies.setAdapter(mArrayAdapter);

        // On Item click transferring to Editor Screen.
        mListViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMoviesList.get(position);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(Constants.KEY_ID, movie.getId());
                intent.putExtra(Constants.KEY_SUBJECT, movie.getSubject());
                intent.putExtra(Constants.KEY_BODY, movie.getBody());
                intent.putExtra(Constants.KEY_URL, movie.getUrl());
                intent.putExtra(Constants.KEY_POSITION, position);
                startActivityForResult(intent, REQUEST_EDITOR);
            }
        });

        mListViewMovies.setEmptyView(mEmptyMainMovieList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String id = data.getStringExtra(Constants.KEY_ID);
        String subject = data.getStringExtra(Constants.KEY_SUBJECT);
        String body = data.getStringExtra(Constants.KEY_BODY);
        String url = data.getStringExtra(Constants.KEY_URL);

        // When returning from Editor Screen, add/update a movie in the list.
        if (requestCode == REQUEST_EDITOR) {
            if(id.equals(Constants.VALUE_NEW_MOVIE)) {
                // New Movie in list.
                Movie movie = new Movie(subject, body, url);
                mMoviesList.add(movie);

                // Add to file.
                FileManager.addToFile(this, FILE_NAME, movie);
            } else {
                int position = data.getIntExtra(Constants.KEY_POSITION, -1);
                // Update the movie in list at id position.
                Movie movie = mMoviesList.get(position);
                movie.setSubject(subject);
                movie.setBody(body);
                movie.setUrl(url);

                // Rewrite the file.
                FileManager.saveFile(this, FILE_NAME, mMoviesList);
            }
        }
        // On returning from Search screen, add new movie in the list.
        else if(requestCode == REQUEST_SEARCH) {
            // New Movie in list.
            Movie movie = new Movie(id, subject, body, url);
            mMoviesList.add(movie);

            // Add to file.
            FileManager.addToFile(this, FILE_NAME, movie);
        }

        mArrayAdapter.notifyDataSetChanged();
    }

    // PlusIcon on click actions.
    public void imageViewPlusIcon_onClick(View view) {
        // Open Alert Dialog for choosing adding new movie mode: manual or via internet.
        openDialogForPlusIcon();
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
