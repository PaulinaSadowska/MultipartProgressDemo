package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    private static final String MEDIA_TYPE_IMAGE = "image/*";

    @BindView(R.id.preview_image)
    View preview;

    @BindView(R.id.progress_text)
    TextView progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.upload_button)
    void onUploadClicked() {
        Intent intent = new Intent();
        intent.setType(MEDIA_TYPE_IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                Toast.makeText(this, "selectedImage " + selectedImage.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
