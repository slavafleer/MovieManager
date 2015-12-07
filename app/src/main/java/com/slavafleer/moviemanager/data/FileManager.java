package com.slavafleer.moviemanager.data;

import android.app.Activity;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Slava on 07/12/2015.
 */
public class FileManager {

    private final static String TAG = FileManager.class.getSimpleName();

    public final static int RESULT_OK = 1;
    public final static int RESULT_ERROR = 2;

    public static int saveFile(Activity activity, String fileName, ArrayList<Movie> movies) {

        try {
            String fullPathFileName = activity.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileWriter fileWriter = new FileWriter(fullPathFileName);

            for(int i = 0; i < movies.size(); i++) {

            }

            return RESULT_OK;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return RESULT_ERROR;
        }

    }
}
