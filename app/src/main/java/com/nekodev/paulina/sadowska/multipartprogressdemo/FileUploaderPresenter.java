package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

class FileUploaderPresenter implements FileUploaderContract.Presenter {

    private final FileResolver fileResolver;
    private final FileUploaderContract.Model model;
    private final FileUploaderContract.View view;

    private Disposable photoUploadDisposable;

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
        photoUploadDisposable = model.uploadImage(filePath)
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
        photoUploadDisposable = model.uploadImageWithoutProgress(filePath)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> view.uploadCompleted(),
                        error -> view.showErrorMessage(error.getMessage())
                );
    }

    @Override
    public void cancel() {
        if (photoUploadDisposable != null && !photoUploadDisposable.isDisposed()) {
            photoUploadDisposable.dispose();
        }
    }

    @Override
    public void testErrorHandling() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(flowableFromCallable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(billingClient -> view.uploadCompleted(),
                        e -> view.showErrorMessage(e.getMessage())));
        final Handler handler = new Handler();
        handler.postDelayed(compositeDisposable::dispose, 1000);
    }

    private Flowable<Integer> createFlowable() {
        return Flowable.create(emitter -> {
            Thread.sleep(10000);
            emitter.onError(new Throwable("error: "));
        }, BackpressureStrategy.BUFFER);
    }

    private Flowable<Integer> flowableFromCallable() {
        return Flowable.fromCallable(() -> {
            throw new Exception("error 2: ");
        });
    }
}
