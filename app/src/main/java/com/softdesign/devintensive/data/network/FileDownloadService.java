package com.softdesign.devintensive.data.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;

public interface FileDownloadService {
    @Multipart
    @GET("")
    Call<ResponseBody> download();
}
