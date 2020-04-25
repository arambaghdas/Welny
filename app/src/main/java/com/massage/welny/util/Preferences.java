package com.massage.welny.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.massage.welny.object.GetUserInfo;
import com.massage.welny.object.UserInfo;
import com.google.gson.Gson;

public class Preferences {

    static String preference_file_key = "welcome_info";
    static String preference_key = "welcome_screen_seen";
    static String preference_user_session_info_key = "user_session_info";
    static String preference_user_info_key = "user_info";

    static public boolean shouldShowWelcomeScreen(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(preference_key, true);
    }

    static public void setWelcomeScreenSeenState(Context context, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(preference_key, value);
        editor.commit();
    }

    static public String getUid(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(preference_user_session_info_key, "");
        UserInfo userInfo =  gson.fromJson(json, UserInfo.class);
        if (userInfo != null) {
            return String.valueOf(userInfo.getUid());
        } else {
            return "";
        }
    }

    static public String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(preference_user_session_info_key, "");
        UserInfo userInfo =  gson.fromJson(json, UserInfo.class);
        if (userInfo != null) {
            return userInfo.getToken();
        } else {
            return "";
        }
    }

    static public String getUserSession(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(preference_user_session_info_key, "");

        //Gson gson = new Gson();
        //String json = sharedPref.getString(preference_user_session_info_key, "");
        //return gson.fromJson(json, UserInfo.class);
    }

    static public void setUserSession(Context context, UserInfo userInfo) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        if (userInfo != null) {
            String json = gson.toJson(userInfo);
            editor.putString(preference_user_session_info_key, json);
        } else {
            editor.putString(preference_user_session_info_key, "");
        }
        editor.commit();
    }

    static public void clearUserSession(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(preference_user_session_info_key, "");
        editor.commit();
    }

    static public GetUserInfo getUserInfo(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(preference_user_info_key, "");
        return gson.fromJson(json, GetUserInfo.class);
    }

    static public void updateUserInfo(Context context, String name, String surname, String email) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(preference_user_info_key, "");
        GetUserInfo getUserInfo =  gson.fromJson(json, GetUserInfo.class);
        getUserInfo.setName(name);
        getUserInfo.setSurname(surname);
        getUserInfo.setEmail(email);
        setUserInfo(context, getUserInfo);
    }

    static public void setUserInfo(Context context, GetUserInfo userResponse) {
        SharedPreferences sharedPref = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        if (userResponse != null) {
            String json = gson.toJson(userResponse);
            editor.putString(preference_user_info_key, json);
        } else {
            editor.putString(preference_user_info_key, "");
        }
        editor.commit();
    }

    static public String getPromoCode(Context context) {
        GetUserInfo getUserInfo = getUserInfo(context);
        if (getUserInfo != null) {
            return getUserInfo.getPromoCode();
        } else {
            return "";
        }
    }
}
