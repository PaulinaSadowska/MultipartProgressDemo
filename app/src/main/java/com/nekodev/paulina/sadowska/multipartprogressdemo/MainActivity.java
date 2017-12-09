package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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


                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null)
                    return;

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(filePath);

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

                UploadsImServiceGenerator
                        .createService()
                        .postImage(body)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            Toast.makeText(this, "result " + result.string(), Toast.LENGTH_SHORT).show();
                        }, error -> {
                            Toast.makeText(this, "error " + error.getCause().getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}
