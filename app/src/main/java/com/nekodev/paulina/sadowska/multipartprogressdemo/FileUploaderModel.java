package com.nekodev.paulina.sadowska.multipartprogressdemo;

import com.nekodev.paulina.sadowska.multipartprogressdemo.api.UploadsImService;

import java.io.File;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileUploaderModel implements FileUploaderContract.Model {

    private final UploadsImService service;


    public FileUploaderModel(UploadsImService service) {
        this.service = service;
    }

    @Override
    public Single<ResponseBody> uploadImage(String filePath) {
        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);

        return service
                .postImage(body);
    }
}
