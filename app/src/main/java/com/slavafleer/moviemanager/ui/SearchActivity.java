package com.slavafleer.moviemanager.ui;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.slavafleer.moviemanager.R;
import com.slavafleer.moviemanager.asynctask.OMDbSearchAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends ListActivity {

    private EditText mEditTextSearchValue;
    // TODO: need to cancel recreate
//    private ArrayList<String> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditTextSearchValue = (EditText)findViewById(R.id.editTextSearchValue);
    }

    public void buttonGo_onClick(View view) {
        try {
            Uri uri = Uri.parse("http://www.omdbapi.com/?")
                    .buildUpon()
                    .appendQueryParameter("s", mEditTextSearchValue.getText().toString())
                    .build();
            URL url = new URL(uri.toString());

            OMDbSearchAsyncTask omDbSearchAsyncTask = new OMDbSearchAsyncTask(this);
            omDbSearchAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void buttonSearchCancel_onClick(View view) {
        finish();
    }
}
