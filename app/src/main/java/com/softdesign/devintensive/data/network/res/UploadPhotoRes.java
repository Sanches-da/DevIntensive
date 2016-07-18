package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadPhotoRes {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("err")
    @Expose
    public String err;

    public Boolean getSuccess() {
        return success;
    }

    public String getErr() {
        return err;
    }
}