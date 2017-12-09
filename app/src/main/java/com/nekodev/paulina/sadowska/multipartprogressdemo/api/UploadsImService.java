package com.nekodev.paulina.sadowska.multipartprogressdemo.api;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public interface UploadsImService {
    @Multipart
    @POST("/api")
    Single<ResponseBody> postImage(@Part MultipartBody.Part image);
}
