package com.massage.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetUserResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    @SerializedName("result")
    @Expose
    private GetUserInfo result;

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

    public GetUserInfo getResult() {
        return result;
    }

    public void setResult(GetUserInfo result) {
        this.result = result;
    }

}
