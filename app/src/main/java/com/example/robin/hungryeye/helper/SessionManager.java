package com.example.robin.hungryeye.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by robin on 12/2/2015.
 * to manage the session for the user
 */
public class SessionManager {
    //TAG for this class for logcat
    public static final String TAG = SessionManager.class.getSimpleName();

    SharedPreferences sharedPreferences;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME ="LoginTest";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    //constructor
    public SessionManager(Context context){
        this.context = context;
        sharedPreferences =context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();

        Log.d(TAG,"user login modified in sharedPreferences");
    }

    //return whatever is in the sharedpreference, default is false
    //isLoggedIn is used whenever the users moves from ane activity to another
    //while going through the application
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGEDIN,false);
    }
}
