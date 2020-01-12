package com.call.welny.object;

import com.call.welny.object.ErrorResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountErrorResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    @SerializedName("error")
    @Expose
    private ErrorResponse error;

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

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}
