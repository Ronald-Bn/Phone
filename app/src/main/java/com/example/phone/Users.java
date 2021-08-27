package com.example.phone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Users {
    Context context;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public static final String PREF_NAME = "User_login";
    public static final String LOGIN = "is_user_login";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";


    public Users(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public boolean isUserLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void UserSessionManage(String name, String email, String phone){
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(EMAIL,email);
        editor.putString(PHONE,phone);
        editor.apply();
    }

    public void checkLogin(){
        if(!this.isUserLogin()){
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
            ((HomeActivity)context).finish();
        }
    }

    public HashMap<String ,String> userDetails(){
        HashMap<String, String > user = new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(PHONE,sharedPreferences.getString(PHONE, null));
        return  user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        ((HomeActivity)context).finish();
    }
}
