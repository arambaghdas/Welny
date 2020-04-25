package com.massage.welny.model;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("/api/users")
    Call<ResponseBody> loginInit(@Body JsonObject loginObject);

    @POST("/api/users")
    Call<ResponseBody> verifyCode(@Body JsonObject verifyCodeObject);

    @POST("/api/users")
    Call<ResponseBody> getUser(@Body JsonObject getUserObject);

    @POST("/api/customers")
    Call<ResponseBody> updateUserInfo(@Body JsonObject object);

    @POST("/api/services")
    Call<ResponseBody> getPackagesList(@Body JsonObject object);
}