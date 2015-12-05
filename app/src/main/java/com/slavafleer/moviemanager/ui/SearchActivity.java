package com.slavafleer.moviemanager.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.slavafleer.moviemanager.R;

import java.util.ArrayList;

public class SearchActivity extends ListActivity {

    private EditText mEditTextSearchValue;
    private ArrayList<String> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditTextSearchValue = (EditText)findViewById(R.id.editTextSearchValue);
    }

    public void buttonGo_onClick(View view) {
    }

    public void buttonSearchCancel_onClick(View view) {
        finish();
    }
}
