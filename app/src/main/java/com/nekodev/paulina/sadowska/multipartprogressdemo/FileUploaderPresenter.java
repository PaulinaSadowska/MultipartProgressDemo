package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.net.Uri;
import android.text.TextUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

class FileUploaderPresenter implements FileUploaderContract.Presenter {

    private final FileResolver fileResolver;
    private final FileUploaderContract.View view;
    private final FileUploaderContract.Model model;

    FileUploaderPresenter(FileUploaderContract.View view, FileUploaderContract.Model model, FileResolver fileResolver) {
        this.view = view;
        this.model = model;
        this.fileResolver = fileResolver;
    }

    @Override
    public void imageSelected(Uri selectedImage) {
        String filePath = fileResolver.getFilePath(selectedImage);
        if (TextUtils.isEmpty(filePath)) {
            view.showErrorMessage("incorrect file uri");
            return;
        }
        view.showThumbnail(selectedImage);
        model.uploadImage(filePath)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            view.uploadCompleted();
                        },
                        error -> view.showErrorMessage(error.getMessage())
                );


    }
}
