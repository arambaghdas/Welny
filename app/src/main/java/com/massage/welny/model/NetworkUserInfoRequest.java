package com.massage.welny.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.massage.welny.R;
import com.massage.welny.log.FileUtils;
import com.massage.welny.object.GetUserResponse;
import com.massage.welny.object.AccountResponse;
import com.massage.welny.object.AccountErrorResponse;
import com.massage.welny.object.PackageResponse;
import com.massage.welny.util.Preferences;
import com.massage.welny.views.RequestUserInfoView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUserInfoRequest {

    private RequestUserInfoView requestView;
    private Activity activity;

    public NetworkUserInfoRequest(RequestUserInfoView requestView, Activity activity) {
        this.requestView = requestView;
        this.activity = activity;
    }

    public void getUserInfoRequest(Context mContext) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject getUserObject = new JsonObject();
        getUserObject.addProperty("jsonrpc", "2.0");
        getUserObject.addProperty("method", "get_user");
        getUserObject.addProperty("id", "1");

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("uid", Preferences.getUid(mContext));
        paramObject.addProperty("token", Preferences.getToken(mContext));
        getUserObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                getUserObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Log.v("registrationRequest", "obj: " + getUserObject.toString());

        Call<ResponseBody> call = service.getUser(getUserObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: " + res;
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        if (res.contains("error")) {
                            AccountErrorResponse loginErrorResponse = new Gson().fromJson(res, AccountErrorResponse.class);
                            if (loginErrorResponse.getError() == null) {
                                requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                requestView.onGetUserFailResponse(loginErrorResponse.getError().getMessage());
                            }
                        } else {
                            GetUserResponse getUserInfo = new Gson().fromJson(res, GetUserResponse.class);
                            if (getUserInfo.getResult() == null) {
                                requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                Preferences.setUserInfo(mContext, getUserInfo.getResult());
                                requestView.onGetUserSuccessResponse();
                                //sendPackagesListRequest(mContext);
                            }
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                        e.printStackTrace();
                    } catch (IOException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetUserFailResponse(activity.getString(R.string.get_code_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
            }
        });
    }

    public void sendLogOutRequest(Context mContext) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject getUserObject = new JsonObject();
        getUserObject.addProperty("jsonrpc", "2.0");
        getUserObject.addProperty("method", "logout");
        getUserObject.addProperty("id", "1");

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("uid", Preferences.getUid(mContext));
        paramObject.addProperty("token", Preferences.getToken(mContext));
        getUserObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                getUserObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Log.v("registrationRequest", "obj: " + getUserObject.toString());

        Call<ResponseBody> call = service.getUser(getUserObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: " + res;
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        if (res.contains("error")) {
                            AccountErrorResponse loginErrorResponse = new Gson().fromJson(res, AccountErrorResponse.class);
                            if (loginErrorResponse.getError() == null) {
                                requestView.onLogOutFailResponse(activity.getString(R.string.log_out_failed));
                            } else {
                                requestView.onLogOutFailResponse(loginErrorResponse.getError().getMessage());
                            }
                        } else {
                            AccountResponse getUserInfo = new Gson().fromJson(res, AccountResponse.class);
                            requestView.onLogOutSuccessResponse(getUserInfo.getMessage());
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onLogOutFailResponse(activity.getString(R.string.log_out_failed));
                        e.printStackTrace();
                    } catch (IOException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onLogOutFailResponse(activity.getString(R.string.log_out_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onLogOutFailResponse(activity.getString(R.string.log_out_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onLogOutFailResponse(activity.getString(R.string.log_out_failed));
            }
        });
    }

    public void sendPackagesListRequest(Context mContext) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject getUserObject = new JsonObject();
        getUserObject.addProperty("jsonrpc", "2.0");
        getUserObject.addProperty("method", "packages_list");
        getUserObject.addProperty("id", "1");

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                getUserObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Log.v("registrationRequest", "obj: " + getUserObject.toString());

        Call<ResponseBody> call = service.getPackagesList(getUserObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: " + res;
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        if (res.contains("error")) {
                            AccountErrorResponse loginErrorResponse = new Gson().fromJson(res, AccountErrorResponse.class);
                            if (loginErrorResponse.getError() == null) {
                                requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                requestView.onGetUserFailResponse(loginErrorResponse.getError().getMessage());
                            }
                        } else {
                            PackageResponse packageResponse = new Gson().fromJson(res, PackageResponse.class);
                            //requestView.onLogOutSuccessResponse(getUserInfo.getMessage());
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                        e.printStackTrace();
                    } catch (IOException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onGetUserFailResponse(activity.getString(R.string.verify_code_failed));
            }
        });
    }
}
