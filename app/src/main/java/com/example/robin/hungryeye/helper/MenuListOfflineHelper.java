package com.example.robin.hungryeye.helper;

import android.content.Context;
import android.util.Log;

import com.example.robin.hungryeye.frag.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robingurung on 06/01/2016.
 */
public class MenuListOfflineHelper {

    Context context;
    ItemStaticBuildInDataBase build;
    String arr[]={
           "Jollow Rice & Chicken, http://hungryeye.gear.host/images/image_food0.jpg, main, 2.67, 3",
                   "Fish Pie, http://hungryeye.gear.host/images/image_food1.jpg, main, 5.46, 2",
                   "Beef Lasagne, http://hungryeye.gear.host/images/image_food2.jpg, main, 1.29, 4",
                   "Curry Goat & Rice http://hungryeye.gear.host/images/image_food3.jpg main 4.63 3",
                   "Pulled Pork in BBQ Gravy & Sweet Potato Mash, http://hungryeye.gear.host/images/image_food4.jpg, main, 3.67, 2",
                   "Lemon Drizzle Cake, http://hungryeye.gear.host/images/image_side0.jpg, side, 1.67, 2",
                   "Carrot Cake, http://hungryeye.gear.host/images/image_side1.jpg, side, 1.46, 2",
                   "Banana Cake, http://hungryeye.gear.host/images/image_side2.jpg, side, 1.29, 3",
                   "Coke, http://hungryeye.gear.host/images/image_drinks0.jpg, drink, 0.49, 3",
                   "Diet, Coke http://hungryeye.gear.host/images/image_drinks1.jpg, drink, 0.49, 2",
                   "Tango (Orange), http://hungryeye.gear.host/images/image_drinks2.jpg, drink, 0.49, 4",
                   "Mineral Water, http://hungryeye.gear.host/images/image_drinks3.jpg, drink, 0.49, 3",
                   "Sprite, http://hungryeye.gear.host/images/image_drinks6.jpg, drink, 0.49, 2"
    };

    public MenuListOfflineHelper(Context context){
        this.context = context;

        //database
        build= new ItemStaticBuildInDataBase(context);

    }
    public void addToList(){
        build.addItems("Jellow Rice","http://hungryeye.gear.host/images/image_food0.jpg","main",new BigDecimal("5.00"),3,"jell-33-54");
        build.addItems("Fish pie","http://hungryeye.gear.host/images/image_food1.jpg","main",new BigDecimal("5.00"),3,"fish-34-21");
        build.addItems("Beef Lasagne","http://hungryeye.gear.host/images/image_food2.jpg","main",new BigDecimal("5.00"),3,"beef-98-56");
        build.addItems("Curry Goat & Rice","http://hungryeye.gear.host/images/image_food3.jpg","main",new BigDecimal("5.00"),3,"curr-76-45");
        build.addItems("Pork in BBQ Gravy & Sweet Potato Mash", "http://hungryeye.gear.host/images/image_food4.jpg", "main", new BigDecimal("3.67"), 2,"pork-78-56");
        build.addItems("Lemon Drizzle Cake", "http://hungryeye.gear.host/images/image_side0.jpg", "side", new BigDecimal("3.67"), 2,"lemo-84-67");
        build.addItems("Carrot Cake", "http://hungryeye.gear.host/images/image_side1.jpg", "side",new BigDecimal("3.67"), 2,"carr-98-76");
        build.addItems("Banana Cake", "http://hungryeye.gear.host/images/image_side2.jpg", "side", new BigDecimal("3.67"), 3,"bann-92-47");
        build.addItems("Coke", "http://hungryeye.gear.host/images/image_drinks0.jpg", "drink", new BigDecimal("1.00"), 3,"coke-76-90");
        build.addItems("Diet Coke", "http://hungryeye.gear.host/images/image_drinks1.jpg", "drink",new BigDecimal("1.00"), 3,"diet-46-19");
        build.addItems("Tango (Orange)", "http://hungryeye.gear.host/images/image_drinks2.jpg", "drink", new BigDecimal("1.00"), 4,"tang-38-93");
        build.addItems("Mineral Water", "http://hungryeye.gear.host/images/image_drinks3.jpg", "drink", new BigDecimal("1.00"), 3,"mine-09-80");
        build.addItems("Sprite", "http://hungryeye.gear.host/images/image_drinks6.jpg", "drink", new BigDecimal("1.00"), 3,"spri-32-46");
    }


}
