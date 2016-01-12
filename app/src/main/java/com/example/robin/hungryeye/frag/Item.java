package com.example.robin.hungryeye.frag;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by robin on 12/15/2015.
 */
public class Item implements Parcelable {

    //title of the food
    public String title;
    //thumbnail of the food
    public String image;
    //category of the food
    public String category;
    //price of the food
    public BigDecimal price;
    //ratings of the food
    public int ratings;
    //stockkeeping unit,
    public String sku;
    //constructor
    public Item(){}



    //constructor for menulist
    public Item(String title,String imagePath,BigDecimal price,int ratings,String category,String sku){
        this.title = title;
        this.image = imagePath;
        this.price = price;
        this.ratings = ratings;
        this.category=category;
        this.sku = sku;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku){
        this.sku = sku;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.category);
        dest.writeSerializable(this.price);
        dest.writeInt(this.ratings);
        dest.writeString(this.sku);
    }

    protected Item(Parcel in) {
        this.title = in.readString();
        this.image = in.readString();
        this.category = in.readString();
        this.price = (BigDecimal) in.readSerializable();
        this.ratings = in.readInt();
        this.sku = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
