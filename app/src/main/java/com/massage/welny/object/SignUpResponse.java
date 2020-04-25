package com.massage.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    @SerializedName("result")
    @Expose
    private UserInfo result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public UserInfo getResult() {
        return result;
    }

    public void setResult(UserInfo result) {
        this.result = result;
    }

}
