package com.example.robin.hungryeye;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.robin.hungryeye.frag.AboutUsFragment;
import com.example.robin.hungryeye.frag.DeliveryFragment;
import com.example.robin.hungryeye.frag.HomeFragment;
import com.example.robin.hungryeye.frag.Menu;

/**
 * Created by robin on 11/26/2015.
 * this class is for tab below the toolbar which are fragments in fragment folder
 */
public class PageAdapter extends FragmentStatePagerAdapter{

    //number of tabs
    private int mNumOfTabs;

    //constuctor overrides super class fragmentmanager
    public PageAdapter(FragmentManager fragmentManager, int mNumOfTabs){
        super(fragmentManager);
        this.mNumOfTabs = mNumOfTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return  homeFragment;
            case 1:
               // MainMenuFragment mainMenuFragment = new MainMenuFragment();
                Menu mainMenuFragment = new Menu();
                return mainMenuFragment;
            case 2:
                DeliveryFragment delivery = new DeliveryFragment();
                return delivery;
            case 3:
                AboutUsFragment about = new AboutUsFragment();
                return  about;
            default:
                return null;
        }

    }
}
