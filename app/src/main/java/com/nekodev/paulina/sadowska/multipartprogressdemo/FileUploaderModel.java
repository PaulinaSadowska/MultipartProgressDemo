package com.nekodev.paulina.sadowska.multipartprogressdemo;

import com.nekodev.paulina.sadowska.multipartprogressdemo.api.UploadsImService;
import com.nekodev.paulina.sadowska.multipartprogressdemo.api.response.CountingRequestBody;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
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

    FileUploaderModel(UploadsImService service) {
        this.service = service;
    }

    @Override
    public Single<ResponseBody> uploadImageWithoutProgress(String filePath) {
        return service.postImage(createMultipartBody(filePath));
    }

    private MultipartBody.Part createMultipartBody(String filePath) {
        File file = new File(filePath);
        RequestBody requestBody = createRequestBody(file);
        return MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
    }

    private RequestBody createRequestBody(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    @Override
    public Flowable<Double> uploadImage(String filePath) {
        return Flowable.create(emitter -> {
            try {
                ResponseBody response = service.postImage(createMultipartBody(filePath, emitter)).blockingGet();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.tryOnError(e);
            }
        }, BackpressureStrategy.LATEST);
    }

    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("upload", file.getName(), createCountingRequestBody(file, emitter));
    }

    private RequestBody createCountingRequestBody(File file, FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBody(file);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}
