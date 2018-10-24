package com.andrew.fiilife.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Grigoras on 01.04.2017.
 */

public class SharedPref {

    private static final String GLOBAL_KEY = "pref"; //this is only to point towards the storage


    public static int getUserID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        return prefs.getInt("userID", 0);
    }

    public static int getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        return prefs.getInt("userType", 0);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        return prefs.getString("userEmail", "DefaultEmailIfError");
    }

    public static String getUserToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        return prefs.getString("userToken", "NULL");
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        return prefs.getString("userName", "DefaultEmailIfError");
    }

    public static void setUserID(Context context, int userID) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userID", userID);
        editor.apply();
    }

    public static void setUserEmail(Context context, String userEmailString) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", userEmailString);
        editor.apply();
    }

    public static void setUserName(Context context, String userNameString) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", userNameString);
        editor.apply();
    }

    public static void setUserToken(Context context, String userStringToken) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userToken", userStringToken);
        editor.apply();
    }

    public static void setUserType(Context context, int userStringType) {
        SharedPreferences prefs = context.getSharedPreferences(GLOBAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userType", userStringType);
        editor.apply();
    }

}
