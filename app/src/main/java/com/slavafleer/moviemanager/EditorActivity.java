package com.slavafleer.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class EditorActivity extends AppCompatActivity {

    private EditText mEditTextSubject;
    private EditText mEditTextBody;
    private EditText mEditTextUrl;
    private ImageView mImageViewUrl;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditTextSubject = (EditText)findViewById(R.id.editTextSubjectValue);
        mEditTextBody = (EditText)findViewById(R.id.editTextBodyValue);
        mEditTextUrl = (EditText)findViewById(R.id.editTextUrlValue);
        mImageViewUrl = (ImageView)findViewById(R.id.imageViewUrl);

        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.KEY_ID, 0);
        // If received properly id , continue receiving rest data.
        if(id != Constants.VALUE_NEW_MOVIE) {
            mEditTextSubject.setText(intent.getStringExtra(Constants.KEY_SUBJECT));
            mEditTextBody.setText(intent.getStringExtra(Constants.KEY_BODY));
            mEditTextUrl.setText(intent.getStringExtra(Constants.KEY_URL));
        }
    }

    // Transfer all data to Main Activity on OK click and close this activity.
    public void buttonEditorOk_onClick(View view) {
        Intent intent = new Intent();
        String subject = mEditTextSubject.getText().toString();
        if(subject.trim().equals("")) {
            Toast.makeText(this, R.string.editor_toast_no_subject, Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra(Constants.KEY_ID, id);
        intent.putExtra(Constants.KEY_SUBJECT, subject);
        intent.putExtra(Constants.KEY_BODY, mEditTextBody.getText().toString());
        intent.putExtra(Constants.KEY_URL, mEditTextUrl.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    // Close this activity on Cancel button.
    public void buttonEditorCancel_onClick(View view) {
        finish();
    }

    // Show Image from URL.
    public void buttonShow_onClick(View view) {
        try {
            DownloadingPictureByUrlAsyncTask downloadingPictureByUrlAsyncTask =
                new DownloadingPictureByUrlAsyncTask(this);
            String urlAsString = mEditTextUrl.getText().toString();
            URL url = new URL(urlAsString);
            downloadingPictureByUrlAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            mImageViewUrl.setImageResource(R.drawable.android_fragmentation);
            Toast.makeText(this, R.string.editor_toast_incorrect_url, Toast.LENGTH_LONG).show();
        }
    }
}
