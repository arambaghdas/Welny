package com.massage.welny.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.massage.welny.R;
import com.massage.welny.log.FileUtils;
import com.massage.welny.object.AccountResponse;
import com.massage.welny.object.AccountErrorResponse;
import com.massage.welny.util.Preferences;
import com.massage.welny.views.RequestUpdateUserInfoView;
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

public class NetworkUpdateUserInfoRequest {

    private RequestUpdateUserInfoView requestView;
    private Activity activity;

    public NetworkUpdateUserInfoRequest(RequestUpdateUserInfoView requestView, Activity activity) {
        this.requestView = requestView;
        this.activity = activity;
    }

    public void sendUpdateUserInfoRequest(Context mContext, String name, String surname, String email) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject getUserObject = new JsonObject();
        getUserObject.addProperty("jsonrpc", "2.0");
        getUserObject.addProperty("method", "update_customer");
        getUserObject.addProperty("id", "1");

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("uid", Preferences.getUid(mContext));
        paramObject.addProperty("token", Preferences.getToken(mContext));
        paramObject.addProperty("name", name);
        paramObject.addProperty("surname", surname);
        paramObject.addProperty("email", email);
        getUserObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                getUserObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Log.v("updateAccount", "obj: " + getUserObject.toString());

        Call<ResponseBody> call = service.updateUserInfo(getUserObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: " + res;
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        Log.v("updateAccount", "res: " + res);

                        if (res.contains("error")) {
                            AccountErrorResponse loginErrorResponse = new Gson().fromJson(res, AccountErrorResponse.class);
                            if (loginErrorResponse.getError() == null) {
                                requestView.onUpdateFailUserInfoResponse(activity.getString(R.string.update_account_failed));
                            } else {
                                requestView.onUpdateFailUserInfoResponse(loginErrorResponse.getError().getMessage());
                            }
                        } else {
                            AccountResponse getUserInfo = new Gson().fromJson(res, AccountResponse.class);
                            Preferences.updateUserInfo(mContext, name, surname, email);
                            requestView.onUpdateUserInfoSuccessResponse(getUserInfo.getMessage());
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onUpdateFailUserInfoResponse(activity.getString(R.string.update_account_failed));
                        e.printStackTrace();
                    } catch (IOException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onUpdateFailUserInfoResponse(activity.getString(R.string.update_account_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onUpdateFailUserInfoResponse(activity.getString(R.string.update_account_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onUpdateFailUserInfoResponse(activity.getString(R.string.update_account_failed));
            }
        });
    }


}
