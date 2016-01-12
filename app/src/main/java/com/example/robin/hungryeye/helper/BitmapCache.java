package com.example.robin.hungryeye.helper;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by robin on 12/15/2015.
 */
public class BitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {


    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    public BitmapCache() {
        this(getDefaultCacheSize());
    }

    public static int getDefaultCacheSize(){
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int catcheSize = maxMemory/8;
        return catcheSize;
    }
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight()/1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
