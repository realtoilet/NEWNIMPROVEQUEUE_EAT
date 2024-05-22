package com.example.queueeat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SharedPrefUtils {
    public static void saveAccount(Context context, String username, String password, String type, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("type", type);
        editor.putString("email", email);
        editor.apply();
    }
    public static void forDataUsage(Context c, String user, String email){
        SharedPreferences sharedPreferences = c.getSharedPreferences("ForDataUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user);
        editor.putString("email", email);
        editor.apply();
    }
    public static boolean checkAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.contains("username") && sharedPreferences.contains("password");
    }
    public static boolean checkType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("type", "").equals("buyer");
    }
    public static String returnUsername(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    public static String returnUsernameForData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ForDataUser", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    public static String returnEmailData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ForDataUser", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }
    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
    }
}
