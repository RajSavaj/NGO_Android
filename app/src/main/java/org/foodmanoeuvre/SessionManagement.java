package org.foodmanoeuvre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "NgoFood";

    public SessionManagement(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(HashMap<String, String> map){
        editor.putBoolean("IS_LOGIN", true);
        editor.putString("uid",map.get("uid"));
        editor.putString("Name",map.get("Name"));
        editor.putString("type",map.get("type"));
        editor.putString("latt",map.get("latt"));
        editor.putString("lang",map.get("lang"));
        editor.putString("mno",map.get("mno"));
        editor.putString("email",map.get("email"));
        editor.putString("pass",map.get("pass"));
        editor.putString("oname",map.get("oname"));
        editor.putString("omno",map.get("omno"));
        editor.putString("address",map.get("address"));
        editor.putString("image",map.get("image"));
        editor.putString("url",map.get("url"));
        editor.commit();
    }


    public boolean isLoggedIn(){
        return pref.getBoolean("IS_LOGIN", false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("uid",pref.getString("uid",""));
        user.put("Name",pref.getString("Name",""));
        user.put("type",pref.getString("type",""));
        user.put("latt",pref.getString("latt",""));
        user.put("lang",pref.getString("lang",""));
        user.put("mno",pref.getString("mno",""));
        user.put("email",pref.getString("email",""));
        user.put("pass",pref.getString("pass",""));
        user.put("oname",pref.getString("oname",""));
        user.put("omno",pref.getString("omno",""));
        user.put("address",pref.getString("address",""));
        user.put("image",pref.getString("image",""));
        user.put("url",pref.getString("url",""));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
