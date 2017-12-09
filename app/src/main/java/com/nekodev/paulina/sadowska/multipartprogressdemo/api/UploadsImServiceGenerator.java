package com.nekodev.paulina.sadowska.multipartprogressdemo.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class UploadsImServiceGenerator {

    private static final String UPLOAD_IM_ADDRESS = "http://uploads.im/";

    public static UploadsImService createService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        return new Retrofit
                .Builder()
                .baseUrl(UPLOAD_IM_ADDRESS)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UploadsImService.class);
    }
}
