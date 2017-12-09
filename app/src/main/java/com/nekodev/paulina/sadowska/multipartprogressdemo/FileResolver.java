package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

import io.reactivex.annotations.Nullable;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

class FileResolver {

    private final ContentResolver contentResolver;

    FileResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Nullable
    String getFilePath(Uri selectedImage) {
        if (selectedImage == null) {
            return null;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
}
