package com.slavafleer.moviemanager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Slava on 04/12/2015.
 */
public class DownloadingPictureByUrlAsyncTask extends AsyncTask<URL, Void, Bitmap> {

    private Activity mActivity;
    private ImageView mImageViewUrl;
    private ProgressBar mProgressBarUrl;

    public DownloadingPictureByUrlAsyncTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        mImageViewUrl = (ImageView)mActivity.findViewById(R.id.imageViewUrl);
        mProgressBarUrl = (ProgressBar)mActivity.findViewById(R.id.progressBarUrl);

        mProgressBarUrl.setVisibility(View.VISIBLE);
    }

    @Override
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

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mImageViewUrl.setImageBitmap(bitmap);

        mProgressBarUrl.setVisibility(View.INVISIBLE);
    }
}
