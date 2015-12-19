package com.slavafleer.moviemanager.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbImageDownloaderAsyncTask;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Show poster on full screen.
 */
public class PosterActivity extends AppCompatActivity
        implements OMDbImageDownloaderAsyncTask.Callbacks {

    private ImageView mImageViewPoster;
    private ProgressBar mProgressBarPoster;

    // Find views and get image from parent activity.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        mImageViewPoster = (ImageView) findViewById(R.id.imageViewFullScreenPoster);
        mProgressBarPoster = (ProgressBar)findViewById(R.id.progressBarPoster);

        Intent intent = getIntent();
        String urlAsString = intent.getStringExtra(Constants.KEY_URL);
        try {
            OMDbImageDownloaderAsyncTask omDbImageDownloaderAsyncTask =
                    new OMDbImageDownloaderAsyncTask(this);
            URL url = new URL(urlAsString);
            omDbImageDownloaderAsyncTask.execute(url);
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

    //Actions while OMDbImageDownloaderAsyncTask is created.
    // Do it before new thread creating.
    public void onAboutToStart() {

        // Show Progress bar while downloading.
        mProgressBarPoster.setVisibility(View.VISIBLE);
    }

    // Set image in layout and hide progress bar.
    public void onImageDownloadSuccess(Bitmap bitmap) {

        mImageViewPoster.setImageBitmap(bitmap);

        // Hide progress bar.
        mProgressBarPoster.setVisibility(View.INVISIBLE);
    }

    // Set different images on errors from OMDbImageDownloaderAsyncTask.
    public void onImageDownloadError(int httpStatusCode, String errorMessage) {

        if (httpStatusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            mImageViewPoster.setImageResource(R.drawable.page_not_found);
        } else if (httpStatusCode != HttpURLConnection.HTTP_OK || errorMessage != null) {
            Toast.makeText(this, "Error " + httpStatusCode +
                    ": " + errorMessage, Toast.LENGTH_LONG).show();
            mImageViewPoster.setImageResource(R.drawable.android_error);
        }

        // Hide progress bar.
        mProgressBarPoster.setVisibility(View.INVISIBLE);
    }
}
