package com.massage.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("discount")
    @Expose
    private int discount;

    @SerializedName("hours")
    @Expose
    private int hours;

    @SerializedName("expiration")
    @Expose
    private int expiration;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("position")
    @Expose
    private int position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
