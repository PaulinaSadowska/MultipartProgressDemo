package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.net.Uri;

import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public interface FileUploaderContract {
    interface View {
        void showThumbnail(Uri selectedimage);

        void showErrorMessage(String message);

        void uploadCompleted();
    }

    interface Presenter {
        void imageSelected(Uri selectedImage);
    }

    interface Model {
        Single<ResponseBody> uploadImage(String filePath);
    }
}
