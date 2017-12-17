package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.net.Uri;

import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

interface FileUploaderContract {
    interface View {
        void showThumbnail(Uri selectedImage);

        void showErrorMessage(String message);

        void uploadCompleted();

        void setUploadProgress(int progress);
    }

    interface Presenter {
        void onImageSelected(Uri selectedImage);

        void onImageSelectedWithoutShowProgress(Uri selectedImage);

        void cancel();
    }

    interface Model {
        Flowable<Double> uploadImage(String filePath);

        Single<ResponseBody> uploadImageWithoutProgress(String filePath);
    }
}
