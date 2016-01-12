package com.example.robin.hungryeye.payment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.robin.hungryeye.frag.Item;
import com.example.robin.hungryeye.helper.ItemStaticBuildInDataBase;

import java.util.*;

/**
 * Created by robingurung on 06/01/2016.
 */
public class Cart {


    private String cartID;
    //cart
    private List<Item> cart = new ArrayList<Item>();
    //menu list
    //private List<Item> menuList = new ArrayList<>();
    private Map<Integer,List<Item>>refMapCart = new HashMap<>();

    //set the menu
    ItemStaticBuildInDataBase database ;
    public String getId() {
        return cartID;
    }

    public void setId(String cartID) {
        this.cartID = cartID;
    }

    public Item getProducts(int pPosition) {

        return cart.get(pPosition);
    }

    public void setItem(Item item) {

        cart.add(item);

    }

    public int getCartSize() {

        return cart.size();

    }

    public boolean checkProductInCart(Item aItem) {

        return cart.contains(aItem);

    }

    public List<Item> getCartProducts() {
        if(cart==null){
            cart = new ArrayList<>();
        }
        return cart;
    }



    public Map<Integer, List<Item>> getRefMapCart() {
        return refMapCart;
    }

    public void setRefMapCart(Map<Integer, List<Item>> refMapCart) {
        this.refMapCart = refMapCart;
    }


    public void insertIntoCart(int pPosition){

        //do a query of the position and insert it into cart



    }
}
