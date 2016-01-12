package com.example.robin.hungryeye.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.frag.HomeFragment;

/**
 * Created by robin on 11/30/2015.
 * This class is the adapter for the home screen image slider
 *
 */
public class CustomSwipeAdapter extends PagerAdapter{
    //image array
    private int[] image_resources = {R.drawable.food1,R.drawable.food2,R.drawable.food3};
    //context object
    private Context ctx;
    //layout inflater for xml
    private LayoutInflater mLayoutInflater;

    //constructor
    public CustomSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }




    //return the array
    @Override
    public int getCount() {
        return image_resources.length;
    }

    //destroy the image if it is not on the screen
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    //validation
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = mLayoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView image_view = (ImageView)item_view.findViewById(R.id.image_view);
        TextView textView = (TextView)item_view.findViewById(R.id.image_count);
        image_view.setImageResource(image_resources[position]);
        textView.setText("image:" + position);
        container.addView(item_view);
        return item_view;
    }
}
