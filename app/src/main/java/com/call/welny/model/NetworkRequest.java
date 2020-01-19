package com.call.welny.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.call.welny.R;
import com.call.welny.log.FileUtils;
import com.call.welny.object.AccountErrorResponse;
import com.call.welny.object.LoginResponse;
import com.call.welny.object.SignUpResponse;
import com.call.welny.util.Preferences;
import com.call.welny.views.RequestView;
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

public class NetworkRequest {

    private RequestView requestView;
    private Activity activity;
    private String CODE_SENT = "Code has been sent.";

    public NetworkRequest(RequestView requestView, Activity activity) {
        this.requestView = requestView;
        this.activity = activity;
    }

    public void getCodeRequest(Context mContext, String number) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("phone", number);

        JsonObject loginObject = new JsonObject();
        loginObject.addProperty("jsonrpc", "2.0");
        loginObject.addProperty("method", "login_init");
        loginObject.addProperty("id", "1");
        loginObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                loginObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Call<ResponseBody> call = service.loginInit(loginObject);
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
                                requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
                            } else {
                                requestView.onGetCodeFailResponse(loginErrorResponse.getError().getMessage(),
                                        loginErrorResponse.getError().getCode());
                            }
                        } else {
                            LoginResponse loginResponse = new Gson().fromJson(res, LoginResponse.class);
                            if (loginResponse.getResult() == null) {
                                requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
                            } else {
                                if (loginResponse.getResult().getMessage().equals(CODE_SENT)) {
                                    requestView.onGetCodeSuccessResponse(mContext.getString(R.string.code_sent));
                                } else {
                                    requestView.onGetCodeSuccessResponse(loginResponse.getResult().getMessage());
                                }
                            }
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
                        e.printStackTrace();
                    } catch (IOException e){
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onGetCodeFailResponse(activity.getString(R.string.get_code_failed), -1);
            }
        });
    }

    public void verifyCodeRequest(Context mContext, String phone, String code) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("phone", phone);
        paramObject.addProperty("login_code", code);

        JsonObject verifyCodeObject = new JsonObject();
        verifyCodeObject.addProperty("jsonrpc", "2.0");
        verifyCodeObject.addProperty("method", "verify_code");
        verifyCodeObject.addProperty("id", "1");
        verifyCodeObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                verifyCodeObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Call<ResponseBody> call = service.verifyCode(verifyCodeObject);
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
                                requestView.onVerifyCodeFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                Log.v("GetResponse", "res: " + loginErrorResponse.getError().getMessage());
                                if (loginErrorResponse.getError().getCode() == 13) {
                                    requestView.onVerifyCodeSuccessResponse(loginErrorResponse.getError().getMessage());
                                } else {
                                    requestView.onVerifyCodeFailResponse(loginErrorResponse.getError().getMessage());
                                }
                            }
                        } else {
                            SignUpResponse signUpResponse = new Gson().fromJson(res, SignUpResponse.class);
                            if (signUpResponse.getResult() == null) {
                                requestView.onVerifyCodeFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                Preferences.setUserSession(mContext, signUpResponse.getResult());
                                requestView.onVerifyCodeSignUpResponse();
                            }
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onVerifyCodeFailResponse(activity.getString(R.string.verify_code_failed));
                        e.printStackTrace();
                    } catch (IOException e){
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onVerifyCodeFailResponse(activity.getString(R.string.get_code_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onVerifyCodeFailResponse(activity.getString(R.string.verify_code_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                requestView.onVerifyCodeFailResponse(activity.getString(R.string.verify_code_failed));
            }
        });
    }

    public void registrationRequest(Context mContext, String phone, String loginCode, String name, String srName, String email) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        JsonObject registerObject = new JsonObject();
        registerObject.addProperty("jsonrpc", "2.0");
        registerObject.addProperty("method", "signup_finish");
        registerObject.addProperty("id", "1");

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("phone", phone);
        paramObject.addProperty("login_code", loginCode);
        paramObject.addProperty("name", name);
        paramObject.addProperty("surname", srName);
        paramObject.addProperty("email", email);
        registerObject.add("params", paramObject);

        String request = DateFormat.getDateTimeInstance().format(new Date()) + " Request: " +
                registerObject.toString();
        FileUtils.writeFileOnInternalStorage(mContext, request);

        Log.v("registrationRequest", "obj: " + registerObject.toString());

        Call<ResponseBody> call = service.verifyCode(registerObject);
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
                                requestView.onRegistrationFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                requestView.onRegistrationFailResponse(loginErrorResponse.getError().getMessage());
                            }
                        } else {
                            SignUpResponse signUpResponse = new Gson().fromJson(res, SignUpResponse.class);
                            if (signUpResponse.getResult() == null) {
                                requestView.onRegistrationFailResponse(activity.getString(R.string.verify_code_failed));
                            } else {
                                Preferences.setUserSession(mContext, signUpResponse.getResult());
                                requestView.onRegistrationSuccessResponse();
                            }
                        }
                    } catch (JsonParseException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: JsonParseException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onRegistrationFailResponse(activity.getString(R.string.verify_code_failed));
                        e.printStackTrace();
                    } catch (IOException e) {
                        String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                                " Response: IOException";
                        FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                        requestView.onRegistrationFailResponse(activity.getString(R.string.get_code_failed));
                        e.printStackTrace();
                    }
                } else {
                    String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                            " Response: not successful";
                    FileUtils.writeFileOnInternalStorage(mContext, serverResponse);

                    requestView.onRegistrationFailResponse(activity.getString(R.string.verify_code_failed));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String serverResponse = DateFormat.getDateTimeInstance().format(new Date()) +
                        " Response: onFailure" + t.getMessage();
                FileUtils.writeFileOnInternalStorage(mContext, serverResponse);


                requestView.onRegistrationFailResponse(activity.getString(R.string.verify_code_failed));
            }
        });
    }

}
