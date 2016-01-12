package com.example.robin.hungryeye.helper;



import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.frag.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



/**
 * Created by robin on 12/31/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        // Start your job in a seperate thread, calling jobFinished(params, needsRescheudle) when you are done.
        // See the javadoc for more detail.
        //on start job

        Log.d("MyService"," JobService started");
        new MyTask(this).execute(params);
        //retrun true here because job is carried out in background
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop the running job, returing true if it needs to be recheduled.
        // See the javadoc for more detail.
        return true;
    }
    /*

    @Override
    public boolean onStartJob(JobParameters params) {
        //on start job
       new MyTask(this).execute(params);
        //retrun true here because job is carried out in background
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    */


    private static class MyTask extends AsyncTask<JobParameters,Void,JobParameters> {

        MyService myService;


        MyTask(MyService myService) {
            this.myService = myService;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONArray response = sendJSONRequest();
            Log.d("Myservice"," before the items get called");
            ArrayList<Item> itemList = parseJSONResponse(response);
            AppController.getWriteableDatabase().addItems(itemList, true);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myService.jobFinished(jobParameters, false);
            }
        }

        private JSONArray sendJSONRequest() {
            JSONArray response = null;
            //string tag request
            String tag_request = "mainmenu_request";
            RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Method.POST,AppConfig.CONTENT_URL, requestFuture, requestFuture) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> p = new HashMap<>();
                    //the value that gets sent is 'mainmenu' and the key is 'tag'
                    p.put("tag", "mainmenu");

                    return p;
                }
            };

            AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_request);
            try {
                //after 30 sec its timeout
                response = requestFuture.get(3000, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return response;
        }

        private ArrayList<Item> parseJSONResponse(JSONArray response) {

            ArrayList<Item> itemArrayList = new ArrayList<>();
            if (response != null && response.length() > 0) {
                //parse json
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Item item = new Item();
                        item.title = obj.getString("itemname");
                        item.setTitle(item.title);

                        item.image = obj.getString("imagepath");
                        item.setImage(item.image);

                        //item.price = obj.getDouble("price");
                        //item.setPrice(item.price);

                        item.ratings = obj.getInt("ratings");
                        item.setRatings(item.ratings);

                        item.category = obj.getString("category");
                        item.setCategory(item.category);

                        Log.d("MYSERVICE", " " + item.getTitle() + " " + item.getImage() + " " + item.getCategory() + " " + item.getPrice() + " " + item.getRatings());
                        itemArrayList.add(item);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            return itemArrayList;
        }

    }

}
