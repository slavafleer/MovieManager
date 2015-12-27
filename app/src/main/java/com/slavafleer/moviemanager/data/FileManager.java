package com.slavafleer.moviemanager.data;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Methods for working with file where Movie List has saved.
 */
public class FileManager {

    private final static String TAG = FileManager.class.getSimpleName();

    public final static int RESULT_OK = 1;
    public final static int RESULT_ERROR = 2;

    private final static String END_OF_TEXT = "!1*8@2&7#3^6";

    // Create/recreate file.
    public static int saveFile(Activity activity, String fileName, ArrayList<Movie> movies) {

        try {
            String fullPathFileName = activity.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileWriter fileWriter = new FileWriter(fullPathFileName);

            for (int i = 0; i < movies.size(); i++) {
                fileWriter.write(movies.get(i).getId() + "\r\n");
                fileWriter.write(movies.get(i).getSubject() + "\r\n");
                fileWriter.write(movies.get(i).getBody() + "\r\n" + END_OF_TEXT + "\r\n");
                fileWriter.write(movies.get(i).getUrl() + "\r\n");
                fileWriter.write(movies.get(i).getRating() + "\r\n");
                fileWriter.write(movies.get(i).getIsWatched() + "\r\n");
            }

            fileWriter.close();

            return RESULT_OK;
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return RESULT_ERROR;
        }
    }

    // Add new movie data to old file.
    public static int addToFile(Activity activity, String fileName, Movie movie) {

        try {
            String fullPathFileName = activity.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileWriter fileWriter = new FileWriter(fullPathFileName, true);

            fileWriter.write(movie.getId() + "\r\n");
            fileWriter.write(movie.getSubject() + "\r\n");
            fileWriter.write(movie.getBody() + "\r\n" + END_OF_TEXT + "\r\n");
            fileWriter.write(movie.getUrl() + "\r\n");
            fileWriter.write(movie.getRating() + "\r\n");
            fileWriter.write(movie.getIsWatched() + "\r\n");

            fileWriter.close();

            return RESULT_OK;
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return RESULT_ERROR;
        }
    }

    // Read data from file.
    public static int loadFile(Activity activity, String fileName, ArrayList<Movie> movies) {

        try {
            String fullPathFileName = activity.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileReader fileReader = new FileReader(fullPathFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String oneLine = bufferedReader.readLine();
            while (oneLine != null) {
                String id = oneLine;
                String subject = bufferedReader.readLine();
                // Take care for multiply line text.
                String body = "";
                oneLine = bufferedReader.readLine();
                while (!oneLine.equals(END_OF_TEXT)) {
                    body += oneLine + "\n";
                    oneLine = bufferedReader.readLine();
                }
                String url = bufferedReader.readLine();
                float rating = Float.parseFloat(bufferedReader.readLine());
                boolean isWatched = Boolean.parseBoolean(bufferedReader.readLine());

                movies.add(new Movie(id, subject, body, url, rating, isWatched));

                oneLine = bufferedReader.readLine();
            }

            bufferedReader.close();
            fileReader.close();

            return RESULT_OK;
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return RESULT_ERROR;
        }
    }

    // Delete file.
    public static int deleteFile(Activity activity, String fileName) {
        try {
            String fullPathFileName = activity.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileWriter fileWriter = new FileWriter(fullPathFileName);
            fileWriter.flush();
            fileWriter.close();

            return RESULT_OK;
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return RESULT_ERROR;
        }
    }
}
