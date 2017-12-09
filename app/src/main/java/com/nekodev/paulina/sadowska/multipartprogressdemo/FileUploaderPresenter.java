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
    private final FileUploaderContract.Model model;
    private final FileUploaderContract.View view;

    FileUploaderPresenter(FileUploaderContract.View view, FileResolver fileResolver, FileUploaderContract.Model model) {
        this.view = view;
        this.model = model;
        this.fileResolver = fileResolver;
    }

    @Override
    public void onImageSelected(Uri selectedImage) {
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
                        progress -> view.setUploadProgress((int) (100 * progress)),
                        error -> view.showErrorMessage(error.getMessage()),
                        view::uploadCompleted
                );
    }

    @Override
    public void onImageSelectedWithoutShowProgress(Uri selectedImage) {
        String filePath = fileResolver.getFilePath(selectedImage);
        if (TextUtils.isEmpty(filePath)) {
            view.showErrorMessage("incorrect file uri");
            return;
        }
        view.showThumbnail(selectedImage);
        model.uploadImageWithoutProgress(filePath)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> view.uploadCompleted(),
                        error -> view.showErrorMessage(error.getMessage())
                );
    }
}
