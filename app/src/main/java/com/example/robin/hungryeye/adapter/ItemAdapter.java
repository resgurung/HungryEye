package com.example.robin.hungryeye.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.frag.Item;

import java.util.List;

/**
 * Created by robin on 12/15/2015.
 */
public class ItemAdapter extends BaseAdapter {

    private static final String TAG = ItemAdapter.class.getSimpleName();
    private static Activity activity;
    private LayoutInflater inflater;
    private static List<Item> items;
    ImageLoader imageLoader = AppController.getInstance().getMyImageLoader();


    public ItemAdapter(Activity activity , List<Item> items){
        this.activity =activity;
        this.items =items;

    }
    public static ItemAdapter getInstance(){
        ItemAdapter adapter = new ItemAdapter(activity,items);
        return adapter;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if(inflater ==null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //}
       // if(convertView ==null){
            convertView = inflater.inflate(R.layout.custom_layout,null);
       // }
       // if(imageLoader ==null){
            imageLoader = AppController.getInstance().getMyImageLoader();
            NetworkImageView imageView=(NetworkImageView) convertView.findViewById(R.id.net_imageview);
            TextView title = (TextView)convertView.findViewById(R.id.textview_title);
            TextView rate = (TextView)convertView.findViewById(R.id.textview_rate);
            TextView price = (TextView)convertView.findViewById(R.id.textview_price);


            //getting data for the row
            Item item = items.get(position);

            imageView.setImageUrl(item.getImage(),imageLoader);
            title.setText(item.getTitle());
            //ratings
            rate.setText(String.valueOf(item.getRatings()));
            //price
            price.setText(String.valueOf(item.getPrice()));
       // }
        return convertView;
    }
}
