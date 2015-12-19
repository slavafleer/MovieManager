package com.slavafleer.moviemanager.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.Constants;
import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbImageDownloaderAsyncTask;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity for editing movie data.
 */

public class EditorActivity extends AppCompatActivity
        implements OMDbImageDownloaderAsyncTask.Callbacks {

    private EditText mEditTextSubject;
    private EditText mEditTextBody;
    private EditText mEditTextUrl;
    private ImageView mImageViewUrl;
    private ProgressBar mProgressBarUrl;
    private RatingBar mRatingBar;
    private CheckBox mCheckBoxWatched;

    private String mId;
    private int mPosition;
    private String mSubject;
    private String mBody;

    // Initialise activities views and fill them with data from Main activity.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditTextSubject = (EditText)findViewById(R.id.editTextSubjectValue);
        mEditTextBody = (EditText)findViewById(R.id.editTextBodyValue);
        mEditTextUrl = (EditText)findViewById(R.id.editTextUrlValue);
        mImageViewUrl = (ImageView)findViewById(R.id.imageViewPoster);
        mProgressBarUrl = (ProgressBar)findViewById(R.id.progressBarPosterDownloading);
        mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
        mCheckBoxWatched = (CheckBox)findViewById(R.id.checkBoxWatched);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(Constants.KEY_POSITION, -1);
        mId = intent.getStringExtra(Constants.KEY_ID);
        // If received properly mId , continue receiving rest of data.
        if(!mId.equals(Constants.VALUE_NEW_MOVIE)) {
            mSubject = intent.getStringExtra(Constants.KEY_SUBJECT);
            mEditTextSubject.setText(mSubject);
            mBody = intent.getStringExtra(Constants.KEY_BODY);
            mEditTextBody.setText(mBody);
            String url = intent.getStringExtra(Constants.KEY_URL);
            mEditTextUrl.setText(url);

            /** Rating Bar Bug!!!
             * Next code prevents bug of filling whole star even if rating data is integer.
             * It happens on some "old" apis as 4.2.2 and 4.3, maybe more.
             * On api 5.* it's not needed, cause have no such bug.
             * The prevent this bug, need to rewrite rating data again with some change.
             * It must be on same star!!!
             * */
            float rating = intent.getFloatExtra(Constants.KEY_RATING, 0) / 2f;
            if((rating * 10) % 10 != 0 ) {
                mRatingBar.setRating(rating + 0.5f);
            }
            mRatingBar.setRating(rating);

            mCheckBoxWatched.setChecked(intent.getBooleanExtra(Constants.KEY_IS_WATCHED, false));

            // Show picture automatic.
            if(!url.equals("")) {
                showImageFromUrl(url);
            }
        }
    }

    // Transfer all data back to Main Activity on OK click and close this activity.
    public void buttonEditorOk_onClick(View view) {
        Intent intent = new Intent();
        mSubject = mEditTextSubject.getText().toString().trim();
        if(mSubject.equals("")) {
            Toast.makeText(this, R.string.editor_toast_no_subject, Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra(Constants.KEY_ID, mId);
        intent.putExtra(Constants.KEY_SUBJECT, mSubject);
        intent.putExtra(Constants.KEY_BODY, mEditTextBody.getText().toString().trim());
        intent.putExtra(Constants.KEY_URL, mEditTextUrl.getText().toString().trim());
        intent.putExtra(Constants.KEY_POSITION, mPosition);
        intent.putExtra(Constants.KEY_RATING, mRatingBar.getRating() * 2);
        intent.putExtra(Constants.KEY_IS_WATCHED, mCheckBoxWatched.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }

    // Close this activity on Cancel button.
    public void buttonEditorCancel_onClick(View view) {
        finish();
    }

    // Show Image from URL on click of Show button.
    public void buttonShow_onClick(View view) {
        showImageFromUrl(mEditTextUrl.getText().toString());
    }

    // Download Image from internet by URL.
    private void showImageFromUrl(String urlAsString) {
        try {
            OMDbImageDownloaderAsyncTask downloadingPictureByUrlAsyncTask =
                new OMDbImageDownloaderAsyncTask(this);
            URL url = new URL(urlAsString);
            downloadingPictureByUrlAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            mImageViewUrl.setImageResource(R.drawable.android_fragmentation);
//            Toast.makeText(this, R.string.editor_toast_incorrect_url, Toast.LENGTH_LONG).show();
        }
    }

    // Create menu with share icon.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Return true to display menu
        return true;
    }


    // Open sharing options while choosing Share icon.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, mSubject + "\n\n" + mBody);
                intent.setType("text/plain");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Open new activity with image on full screen
    public void imageViewPoster_onClick(View view) {
        String url = mEditTextUrl.getText().toString().trim();
        if(!url.equals("")) {
            Intent intent = new Intent(this, PosterActivity.class);
            intent.putExtra(Constants.KEY_URL, mEditTextUrl.getText().toString().trim());
            startActivity(intent);
        }
    }

    //Actions while OMDbImageDownloaderAsyncTask is created.
    // Do it before new thread creating.
    public void onAboutToStart() {

        // Show Progress bar while downloading.
        mProgressBarUrl.setVisibility(View.VISIBLE);
    }

    // Set image in layout and hide progress bar.
    public void onImageDownloadSuccess(Bitmap bitmap) {

        mImageViewUrl.setImageBitmap(bitmap);

        // Hide progress bar.
        mProgressBarUrl.setVisibility(View.INVISIBLE);
    }

    // Set different images on errors from OMDbImageDownloaderAsyncTask.
    public void onImageDownloadError(int httpStatusCode, String errorMessage) {

        if (httpStatusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            mImageViewUrl.setImageResource(R.drawable.page_not_found);
        } else if (httpStatusCode != HttpURLConnection.HTTP_OK || errorMessage != null) {
            Toast.makeText(this, "Error " + httpStatusCode +
                    ": " + errorMessage, Toast.LENGTH_LONG).show();
            mImageViewUrl.setImageResource(R.drawable.android_error);
        }

        // Hide progress bar.
        mProgressBarUrl.setVisibility(View.INVISIBLE);
    }
}
