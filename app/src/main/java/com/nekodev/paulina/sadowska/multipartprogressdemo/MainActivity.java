package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.multipartprogressdemo.api.UploadsImServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class MainActivity extends AppCompatActivity implements FileUploaderContract.View, EasyPermissions.PermissionCallbacks {

    private static final int PICK_IMAGE = 100;
    private static final int RC_READ_FILE = 999;

    @BindView(R.id.preview_image)
    ImageView preview;

    @BindView(R.id.progress_text)
    TextView progressText;

    @BindView(R.id.preview_white_layer)
    View whiteLayer;

    private FileUploaderPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializePresenter();
        methodRequiresReadPermission();
    }

    private void initializePresenter() {
        presenter = new FileUploaderPresenter(
                this,
                new FileResolver(getContentResolver()),
                new FileUploaderModel(UploadsImServiceGenerator.createService())
        );
    }

    @OnClick(R.id.upload_button)
    void onUploadClicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE);
    }

    @OnClick(R.id.cancel_button)
    void onCancelClicked() {
        presenter.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            presenter.onImageSelected(data.getData());
        }
    }

    @Override
    public void showThumbnail(Uri selectedImage) {
        whiteLayer.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(selectedImage)
                .into(preview);
    }

    @Override
    public void showErrorMessage(String message) {
        progressText.setText(R.string.upload_error);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadCompleted() {
        whiteLayer.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        Toast.makeText(this, R.string.upload_completed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUploadProgress(int progress) {
        progressText.setText(progress + "%");
    }

    @AfterPermissionGranted(RC_READ_FILE)
    private void methodRequiresReadPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_read_storage),
                    RC_READ_FILE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        finish();
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
    }
}
