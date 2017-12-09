package com.nekodev.paulina.sadowska.multipartprogressdemo;

import com.nekodev.paulina.sadowska.multipartprogressdemo.api.UploadsImService;
import com.nekodev.paulina.sadowska.multipartprogressdemo.api.response.CountingRequestBody;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileUploaderModel implements FileUploaderContract.Model {

    private final UploadsImService service;


    public FileUploaderModel(UploadsImService service) {
        this.service = service;
    }

    @Override
    public Flowable<Double> uploadImage(String filePath) {
        return Flowable.create(emitter -> service.postImage(createMultipartBody(filePath, emitter))
                .subscribe(result ->
                        emitter.onComplete(), emitter::onError), BackpressureStrategy.LATEST);
    }

    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("upload", file.getName(), createRequestBody(file, emitter));

    }

    private RequestBody createRequestBody(File file, FlowableEmitter<Double> emitter) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return new CountingRequestBody(requestFile, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}
