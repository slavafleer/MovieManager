package com.slavafleer.moviemanager.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask for downloading image by URL.
 */
public class OMDbImageDownloaderAsyncTask extends AsyncTask<URL, Void, Bitmap> {

    private Callbacks mCallbacks;
    private int mHttpStatusCode;
    private String mErrorMessage = null;

    public OMDbImageDownloaderAsyncTask(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    // Find views in parent activity before open new thread.
    protected void onPreExecute() {

        mCallbacks.onAboutToStart();
    }

    // Request for image by url in new thread and download it.
    protected Bitmap doInBackground(URL... params) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = params[0];
            connection = (HttpURLConnection) url.openConnection();

            mHttpStatusCode = connection.getResponseCode();

            // In case of communication error...
            if (mHttpStatusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Downloaded Image from given URL.
            return bitmap;
        } catch (Exception e) {
            mErrorMessage = e.getMessage();
            return null;
        } finally {
            try {
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {}

        }
    }

    // Return to UI thread image or error message.
    protected void onPostExecute(Bitmap bitmap) {

        if(mErrorMessage == null) {
            mCallbacks.onImageDownloadSuccess(bitmap);
        } else {
            mCallbacks.onImageDownloadError(mHttpStatusCode, mErrorMessage);
        }
    }

    // Interface for UI callbacks.
    public interface Callbacks {

        void onAboutToStart();
        void onImageDownloadSuccess(Bitmap bitmap);
        void onImageDownloadError(int httpStatusCode, String errorMessage);
    }
}
