package com.slavafleer.moviemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctasks.OpenPosterOnFullScreenAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Show poster on full screen.
 */
public class PosterActivity extends AppCompatActivity {

    private ImageView mImageViewPoster;

    // Find views and get image from parent activity.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        mImageViewPoster = (ImageView) findViewById(R.id.imageViewFullScreenPoster);

        Intent intent = getIntent();
        String urlAsString = intent.getStringExtra(Constants.KEY_URL);
        try {
            OpenPosterOnFullScreenAsyncTask openPosterOnFullScreenAsyncTask =
                    new OpenPosterOnFullScreenAsyncTask(this);
            URL url = new URL(urlAsString);
            openPosterOnFullScreenAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            mImageViewPoster.setImageResource(R.drawable.android_fragmentation);
        }

        // Enable Home Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Return to parent activity while Home button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return  true;
    }

    // Close Poster Activity on image touch.
    public void imageViewFullScreenPoster_onClick(View view) {
        finish();
    }
}
