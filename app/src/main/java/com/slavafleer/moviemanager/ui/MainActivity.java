/**
 * Movie Manager
 * Application for managing home movie library.
 * This app can show user's movies list on main screen,
 * searching for movie description on internet or
 * filling movie description by user himself.
 */

// TODO: need to comment all methods.
// TODO: checking for offline mode and giving possibility working in offline mode.
// TODO: care for N/A strings from OMDB API.

package com.slavafleer.moviemanager.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.adapters.MainListAdapter;
import com.slavafleer.moviemanager.data.FileManager;
import com.slavafleer.moviemanager.data.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_EDITOR = 1;
    private final static int REQUEST_SEARCH = 2;

    private final static String FILE_NAME = "movieslist.txt";

    // Movies Collection.
    private ArrayList<Movie> mMovies = new ArrayList<>();

    private ListView mListViewMovies;
    private TextView mEmptyMainMovieList;
    private MainListAdapter mMainListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading movie list from the file, if failed - reset the array list.
        if(FileManager.loadFile(this, FILE_NAME, mMovies) == FileManager.RESULT_ERROR) {
            mMovies = new ArrayList<>();
        }

        mListViewMovies = (ListView)findViewById(R.id.listViewMainMovies);
        mEmptyMainMovieList = (TextView)findViewById(R.id.emptyMainMovieList);

        // ListView adapter initialising.
        mMainListAdapter = new MainListAdapter(this, mMovies);
        mListViewMovies.setAdapter(mMainListAdapter);

        // On Item click transferring to Editor Screen.
        mListViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transferToEditorActivity(position);
            }
        });

        // On Item Long click, open menu for edit and delete options.
        mListViewMovies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Delete the movie from list and file.
                            case R.id.action_delete:
                                // Open confirmation dialog.
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(R.string.dialog_delete_movie_title)
                                        .setMessage(R.string.dialog_delete_all_message)
                                        .setPositiveButton(R.string.dialog_delete_movie_positive_button_label,
                                                new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mMovies.remove(position);
                                                FileManager.saveFile(MainActivity.this, FILE_NAME, mMovies);
                                                mMainListAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.button_cancel_label, null)
                                        .create()
                                        .show();

                                break;

                            case R.id.action_edit:
                                transferToEditorActivity(position);
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.inflate(R.menu.menu_item);
                popupMenu.show();

                return true;
            }
        });

        mListViewMovies.setEmptyView(mEmptyMainMovieList);
    }

    private void transferToEditorActivity(int position) {
        Movie movie = mMovies.get(position);
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra(Constants.KEY_ID, movie.getId());
        intent.putExtra(Constants.KEY_SUBJECT, movie.getSubject());
        intent.putExtra(Constants.KEY_BODY, movie.getBody());
        intent.putExtra(Constants.KEY_URL, movie.getUrl());
        intent.putExtra(Constants.KEY_RATING, movie.getRating());
        intent.putExtra(Constants.KEY_IS_WATCHED, movie.getIsWatched());
        intent.putExtra(Constants.KEY_POSITION, position);
        startActivityForResult(intent, REQUEST_EDITOR);
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
        float rating = data.getFloatExtra(Constants.KEY_RATING, 0);
        boolean isWatched = data.getBooleanExtra(Constants.KEY_IS_WATCHED, false);

        // When returning from Editor Screen, add/update a movie in the list.
        if (requestCode == REQUEST_EDITOR) {
            if(id.equals(Constants.VALUE_NEW_MOVIE)) {
                // New Movie in list.
                Movie movie = new Movie(subject, body, url);
                mMovies.add(movie);

                // Add to file.
                FileManager.addToFile(this, FILE_NAME, movie);
            } else {
                int position = data.getIntExtra(Constants.KEY_POSITION, -1);
                // Update the movie in list at id position.
                Movie movie = mMovies.get(position);
                movie.setSubject(subject);
                movie.setBody(body);
                movie.setUrl(url);
                movie.setRating(rating);
                movie.setIsWatched(isWatched);

                // Rewrite the file.
                FileManager.saveFile(this, FILE_NAME, mMovies);
            }
        }
        // On returning from Search screen, add new movie in the list.
        else if(requestCode == REQUEST_SEARCH) {
            // New Movie in list.
            Movie movie = new Movie(id, subject, body, url);
            movie.setRating(rating);
            mMovies.add(movie);

            // Add to file.
            FileManager.addToFile(this, FILE_NAME, movie);
        }

        mMainListAdapter.notifyDataSetChanged();
    }

    // PlusIcon on click actions.
    public void imageViewPlusIcon_onClick(View view) {
/*        // Open Alert Dialog for choosing adding new movie mode: manual or via internet.
        openDialogForPlusIcon();*/

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_plus_button);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_plus_manual:
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        intent.putExtra(Constants.KEY_ID, Constants.VALUE_NEW_MOVIE);
                        startActivityForResult(intent, REQUEST_EDITOR);
                        break;

                    case R.id.action_plus_search:
                        Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
                        startActivityForResult(intentSearch, REQUEST_SEARCH);
                        break;
                }

                return true;
            }
        });
        popupMenu.show();
    }

//    // Open Alert Dialog on Plus Icon clicking for choosing
//    // adding new movie mode: manual or via internet.
//    private void openDialogForPlusIcon() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.main_dialog_plus_icon_title)
//                .setMessage(R.string.main_dialog_plus_icon_message)
//                .setPositiveButton(R.string.main_dialog_plus_icon_positive_button_label,
//                        new DialogInterface.OnClickListener() {
//                    // On manual -  open Editor Screen.
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
//                        intent.putExtra(Constants.KEY_ID, Constants.VALUE_NEW_MOVIE);
//                        startActivityForResult(intent, REQUEST_EDITOR);
//                    }
//                })
//                .setNegativeButton(R.string.main_dialog_plus_icon_negative_button_label,
//                        new DialogInterface.OnClickListener() {
//                    // On search - open search screen.
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                        startActivityForResult(intent, REQUEST_SEARCH);
//                    }
//                })
//                .setNeutralButton(R.string.main_dialog_plus_icon_neutral_button_label, null)
//                .create();
//        dialog.setCancelable(false);
//        dialog.show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Main menu options.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Delete all movies data.
            case R.id.action_delete_all:
                // Re-ask by alert dialog for confirmation.
                new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_delete_all_title)
                    .setMessage(R.string.dialog_delete_all_message)
                    .setPositiveButton(R.string.dialog_delete_all_positive_button_label,
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileManager.deleteFile(MainActivity.this, FILE_NAME);
                            mMovies.clear();
                            mMainListAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.button_cancel_label, null)
                    .create()
                    .show();
                break;

            // Exit from app.
            case R.id.action_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
