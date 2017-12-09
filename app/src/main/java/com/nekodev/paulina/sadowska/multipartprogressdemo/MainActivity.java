package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.multipartprogressdemo.api.UploadsImServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class MainActivity extends AppCompatActivity implements FileUploaderContract.View {

    public static final int PICK_IMAGE = 100;
    private static final String MEDIA_TYPE_IMAGE = "image/*";

    @BindView(R.id.preview_image)
    View preview;

    @BindView(R.id.progress_text)
    TextView progress;

    FileUploaderPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new FileUploaderPresenter(this,
                new FileUploaderModel(UploadsImServiceGenerator.createService()),
                new FileResolver(getContentResolver()));
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
            presenter.imageSelected(data.getData());
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadCompleted() {
        Toast.makeText(this, R.string.upload_completed, Toast.LENGTH_SHORT).show();
    }
}
