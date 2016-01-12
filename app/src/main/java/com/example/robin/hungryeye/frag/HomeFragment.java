package com.example.robin.hungryeye.frag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.adapter.CustomSwipeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    //viewpager
    ViewPager mViewPager;
    //custumaa
    CustomSwipeAdapter mCustomSwipeAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewPager = (ViewPager)getView().findViewById(R.id.view_pager);
        //mCustomSwipeAdapter = new CustomSwipeAdapter(HomeFragment.this.getActivity());
        //mViewPager.setAdapter(mCustomSwipeAdapter);
    }
}
