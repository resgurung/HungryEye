package com.example.robin.hungryeye.frag;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.adapter.ItemAdapter;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.helper.ItemStaticBuildInDataBase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


/**
 * Created by robin on 12/14/2015.
 *  Food menu
 */
public class Menu extends Fragment  {


    private static final String TAG = Menu.class.getSimpleName();

    //fragment initialisation parameter
    private  static final String ITEMS_ARRAY = "item_array";
    private ProgressDialog dialog;
    private List<Item>arrayItems;
    //offline database
    ItemStaticBuildInDataBase build;
    private ListView listView;
    private ItemAdapter adapter;
    private Item item;
    // Progress dialog
    private ProgressDialog pDialog;

    //empty constructor
    public Menu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.menulist, container, false);

        listView = (ListView)view.findViewById(R.id.list_item);
        build = new ItemStaticBuildInDataBase(getContext());
        pDialog = new ProgressDialog(Menu.this.getActivity());
        pDialog.setCancelable(false);

        //if(isNetworkAvailable()){
            //check network if there is network then fetch product else load from the database
            // Fetching products from server
            //JSONArray response = sendJSONRequest();
            //ArrayList<Item> itemList = parseJSONResponse(response);
            //AppController.getWriteableDatabase().addItems(itemList, true);

            //adapter = new ItemAdapter(Menu.this.getActivity(),itemList);
            //listView.setAdapter(adapter);
        //}
       // else{
            //get menu list from database
        arrayItems = build.getItemList();
            //set the list in adapter
        adapter = new ItemAdapter(Menu.this.getActivity(),arrayItems);
            //listView.setAdapter(adapter);
        //}
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String food = String.valueOf(parent.getItemAtPosition(position));
                item = (Item) adapter.getItem(position);
                //Log.d("Dialog", "" + item.getTitle());
                //Log.d("Dialog", "" + item.getCategory());
                //Log.d("Dialog", "" + item.getImage());
                //Log.d("Dialog", "" + item.getPrice());
                //Log.d("Dialog", "" + item.getRatings());

                Bundle bun = new Bundle();
                bun.putParcelable("item", item);
                Log.d(TAG, "" + parent.getPositionForView(view));
                bun.putInt("position", parent.getPositionForView(view));
                showDialog(bun);


            }
        });


        return view;
    }


public void showDialog(Bundle bun){
    DialogFragmentItem dialogFragmentItem = DialogFragmentItem.newInstance();//the fragment you want to show
    dialogFragmentItem.setArguments(bun);
    dialogFragmentItem.setTargetFragment(this, 0);
    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    dialogFragmentItem.show(fragmentTransaction, "dialog");
}

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

                    //item.price = obj.get("price");
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

    private JSONArray sendJSONRequest() {
        JSONArray response = null;
        //string tag request
        String tag_request = "mainmenu_request";
        //RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, AppConfig.CONTENT_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                //the value that gets sent is 'mainmenu' and the key is 'tag'
                p.put("tag", "mainmenu");

                return p;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_request);

        return response;
    }
    /*
    check if there is network if not then use local database
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
