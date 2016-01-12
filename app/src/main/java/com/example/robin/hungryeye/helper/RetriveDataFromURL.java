package com.example.robin.hungryeye.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.frag.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robin on 12/28/2015.
 * retrive all the tables from the server
 *
 */
public class RetriveDataFromURL {

    private static final String TAG = RetriveDataFromURL.class.getSimpleName();
    private ItemDBHandler itemDB;
    Context context;
    //constructor
    public RetriveDataFromURL(/*List<Item>itemArrayList*/ Context context){
        //this.itemArrayList = itemArrayList;
        this.context = context;
    }

    public /*List<Item>*/ void retriveItems() {
        //crate the volley request obj
        //string tag request
        String tag_request = "drinks_request";
        itemDB = new ItemDBHandler(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.CONTENT_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //parse json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        /*
                        item.title = obj.getString("itemname");
                        item.setTitle(item.title);

                        item.image = obj.getString("imagepath");
                        item.setImage(item.image);

                        item.price = obj.getDouble("price");
                        item.setPrice(item.price);

                        item.ratings = obj.getInt("ratings");
                        item.setRatings(item.ratings);

                        item.category = obj.getString("category");
                        item.setCategory(item.category);

                        */

                        String title = obj.getString("itemname");
                        String imagepath = obj.getString("imagepath");
                        double price =  obj.getDouble("price");
                        int ratings = obj.getInt("ratings");
                        String category = obj.getString("category");
                        //itemDB.addItems(title,imagepath,price,ratings,category);

                        //type is the jsonArray
                        /*
                        item.setTitle(obj.getString("itemname"));
                        Log.d("RetriveDataFromURL", "itemsname: item:" + item.getTitle());

                        item.setImage(obj.getString("imagepath"));
                        Log.d("RetriveDataFromURL", "Imagepath:item:" + item.getImage());

                        item.setRatings(obj.getInt("ratings"));
                        Log.d("RetriveDataFromURL", "Ratings:item:" + item.getRatings());

                        item.setPrice(obj.getDouble("price"));
                        Log.d("RetriveDataFromURL", "Price:item:" + item.getPrice());

                        item.setCategory(obj.getString("category"));
                        Log.d("RetriveDataFromURL", "category:item:" + item.getCategory());*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //notify the data changed to adapter
               // adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Retrival error", new Object[]{error.getStackTrace()});

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                //the value that gets sent is 'mainmenu' and the key is 'tag'
                p.put("tag", "menu");

                return p;
            }
        };
        Log.d("sending request", " Request sending...");
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_request);
        //return itemArrayList;
    }


}
