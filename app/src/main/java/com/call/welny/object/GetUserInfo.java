package com.call.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserInfo {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("promo_code")
    @Expose
    private String promoCode;

    @SerializedName("credits")
    @Expose
    private double credits;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("active_package_info")
    @Expose
    private CustomerPackage customerPackage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public long getCredits() {
        return Math.round(credits);
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public String getFullname() {
        return name + " " + surname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public CustomerPackage getCustomerPackage() {
        return customerPackage;
    }

    public void setCustomerPackage(CustomerPackage customerPackage) {
        this.customerPackage = customerPackage;
    }
}
