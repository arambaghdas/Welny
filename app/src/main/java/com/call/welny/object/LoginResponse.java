package com.call.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    @SerializedName("result")
    @Expose
    private ErrorResponse result;

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

    public ErrorResponse getResult() {
        return result;
    }

    public void setResult(ErrorResponse result) {
        this.result = result;
    }

}
