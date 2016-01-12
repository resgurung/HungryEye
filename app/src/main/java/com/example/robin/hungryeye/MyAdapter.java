package com.example.robin.hungryeye;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by robin on 11/26/2015.
 * this is the class that handles the navigational part
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //to check which view is worked on
    private static final int TYPE_HEADER = 0;
    //under inflation by view, checking if it is header or item
    private static final int TYPE_ITEM = 1;
    //array to hold all the titles from MainActivity.java
    private String mNavTitles[];
    //array to hold all the image icons passed from the MainActivity.java
    private int mIcons[];
    //String resource for the header View Name
    private String name;
    //int resource for the header view profile picture
    private int profile;
    //String resource for the header view email
    private String email;

    //Inner Viewholder class that extends the RecycleView's ViewHolder
    //ViewHolder class are used to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder{
        int holderID;
        TextView textView;
        ImageView imageView;
        ImageView profileView;
        TextView name;
        TextView email;

        public ViewHolder(View itemView, int viewType){
            super(itemView);
            //set the appropriate view in accordance with the viewtype as passed when the holderis created with View and ViewType as a parameter

            if(viewType==TYPE_ITEM){
                //creating Textview object with the id of textView from item_row.xml
                textView = (TextView)itemView.findViewById(R.id.rowText);
                //creating ImageView object with the id of ImageView from item_row.xml
                imageView = (ImageView)itemView.findViewById(R.id.rowIcon);
                //setting holder id as 1 as the object being populated are typeitem row
                holderID = 1;
            }
            else{
                //creating TextView object from header.xml for name
                name = (TextView)itemView.findViewById(R.id.name);
                //creating TextView object from form header.xml for email
                email = (TextView)itemView.findViewById(R.id.email);
                //creating ImageView object from header.xml for profile pic
                profileView = (ImageView)itemView.findViewById(R.id.circleview);
                //setting holder id as 0 as the object being populated are of type header view
                holderID = 0;
            }
        }
    }


    //MyAdapter constructor
    public MyAdapter(String mNavTitles[],int mIcons[],String name, String email, int profile ){
        this.mNavTitles = mNavTitles;
        this.mIcons = mIcons;
        this.name = name;
        this.email = email;
        this.profile = profile;

    }

    //create the method onCreateViewHolder that ovwerride the method which is called when ViewHolder is
    //created, moreover this method inflate the item_row.xml layout if the viewType is TYPE_ITEM or else
    //we inflate the header.xml
    //plus if the viewType is the TYPE_HEADER then pass it to the viewHolder

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType ==TYPE_ITEM){
            //inflate the item_row.xml
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            //create ViewHolder and pass the object of the type view
            ViewHolder viewHolderItem = new ViewHolder(v,viewType);
            //return the object created
            return viewHolderItem;
        }
        else if(viewType == TYPE_HEADER){
            //inflate the header.xml layout and pass it to the view holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            //create ViewHolder and pass the object of type view
            ViewHolder viewHolderHeader = new ViewHolder(v,viewType);
            //return the object created
            return viewHolderHeader;
        }

        //otherwise return null
        return null;
    }


    //Next method is to override a method which is called when the item in a row is needed to be displayed, here the int position
    //tells us item at which position is being constructed to be displayed and the holder id of the holderobject tell us which
    //view type is being created [1 for item row]
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position){
        //As the list view is going to be called after the header view so we decrement the
        //position by 1 and pass it to the holder while setting the text and image
        if(holder.holderID ==1){
            //Setting the text with the array of our Titles
            holder.textView.setText(mNavTitles[position-1]);
            //Setting the image with array of our icons
            holder.imageView.setImageResource(mIcons[position-1]);
        }
        else{
            //Setting the profile image with holder
            holder.profileView.setImageResource(profile);
            //Setting the name of the profile
            holder.name.setText(name);
            //setting the email of the profile
            holder.email.setText(email);
        }
    }
    //With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position){
        if(isPositionHeader(position))
        return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position){
        return position ==0;
    }

    //this method returns the number if items presesnt in the list
    @Override
    public int getItemCount() {
        //the number of itemsin the list will be +1 the titles including the header view
        return mNavTitles.length+1;
    }
}

