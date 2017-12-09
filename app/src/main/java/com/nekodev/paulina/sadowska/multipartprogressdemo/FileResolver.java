package com.nekodev.paulina.sadowska.multipartprogressdemo;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileResolver {

    private static final String FILE_PATH_EMPTY = "";
    private final ContentResolver contentResolver;

    public FileResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public String getFilePath(Uri selectedImage) {
        if (selectedImage == null) {
            return FILE_PATH_EMPTY;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        if (cursor == null) {
            return FILE_PATH_EMPTY;
        }

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
}
