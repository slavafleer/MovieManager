package com.slavafleer.moviemanager.asynctasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slavafleer.moviemanager.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask for downloading image by URL.
 */
public class DownloadingPictureByUrlAsyncTask extends AsyncTask<URL, Void, Bitmap> {

    private Activity mActivity;
    private ImageView mImageViewUrl;
    private ProgressBar mProgressBarUrl;

    public DownloadingPictureByUrlAsyncTask(Activity activity) {
        mActivity = activity;
    }

    // Find views in parent activity before open new thread.
    protected void onPreExecute() {
        mImageViewUrl = (ImageView) mActivity.findViewById(R.id.imageViewPoster);
        mProgressBarUrl = (ProgressBar) mActivity.findViewById(R.id.progressBarPosterDownloading);

        // Show Progress bar while downloading.
        mProgressBarUrl.setVisibility(View.VISIBLE);
    }

    // Request for image by url in new thread and download it.
    protected Bitmap doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int httpStatusCode = connection.getResponseCode();
            // In case of communication error...
            if (httpStatusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.page_not_found);
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                Toast.makeText(mActivity, "Error: " + httpStatusCode, Toast.LENGTH_LONG).show();
                return BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.android_error);
            }

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Downloaded Image from given URL.
            return bitmap;
        } catch (Exception e) {
            // Finding image in drawable.
            Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
                    R.drawable.android_error);
            return bitmap;
        }
    }

    // Show image after finishing download.
    protected void onPostExecute(Bitmap bitmap) {
        mImageViewUrl.setImageBitmap(bitmap);

        // Hide progress bar.
        mProgressBarUrl.setVisibility(View.INVISIBLE);
    }
}
