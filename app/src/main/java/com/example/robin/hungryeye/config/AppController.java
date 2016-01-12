package com.example.robin.hungryeye.config;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.robin.hungryeye.helper.BitmapCache;
import com.example.robin.hungryeye.helper.ItemDBHandler;

import java.util.Objects;

/**
 * Created by robin on 12/2/2015.
 * download or import volley from github for this project
 * for more info go to "http://code.tutsplus.com/tutorials/an-introduction-to-volley--cms-23800"
 * Volley is used in place of asyncTask android
 * make sure add this class in the manifest file below the application like below
 * <application
 *      android:name=".packagename.AppController"  or  android:name=".AppController"
 */
public class AppController extends Application {
    //TAG for this class for logcat
    public static final  String TAG = AppController.class.getSimpleName();

    //import from volly lib
    private RequestQueue myRequest;
    //for images from the url
    private ImageLoader myImageLoader;
    //create an instance of this class Appcontroller
    private static AppController mInstance;

    //item database
    private static ItemDBHandler mItemDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
    //sync the instance
    public static synchronized AppController getInstance(){
        return mInstance;
    }
    //getRequestQueue
    public RequestQueue getRequestQueue(){
        if(myRequest ==null){
            myRequest = Volley.newRequestQueue(getApplicationContext());
        }
        return myRequest;
    }

    public ImageLoader getMyImageLoader(){
        getRequestQueue();
        if(myImageLoader == null){
            myImageLoader = new ImageLoader(this.myRequest,new BitmapCache());
        }
        return this.myImageLoader;
    }
    //add the request to the list
    public <T> void addToRequestQueue(Request<T> request,String tag){
        //if the request is empty use the main TAG else use the actual tag passed as a string
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        //add our request
        getRequestQueue().add(request);
    }
    //this method only takes Request type T
    public <T> void addToRequestQueue(Request<T> request){
        //main class TAG
        request.setTag(TAG);
        //add the request
        getRequestQueue().add(request);
    }
    //cancel the request, parameter is Object
    public void cancelPendingRequest(Objects tag){
        if(myRequest!=null){
            //cancel myrequest
            myRequest.cancelAll(tag);
        }
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
    public synchronized  static ItemDBHandler getWriteableDatabase(){
        if(mItemDatabase ==null){
            mItemDatabase = new ItemDBHandler(getAppContext());
        }
        return mItemDatabase;
    }


}
