package com.example.robin.hungryeye.frag;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.payment.ListItemListener;

/**
 * Created by robingurung on 06/01/2016.
 */
public class DialogFragmentItem extends DialogFragment {


    private ListItemListener mCallBacks;
    Item myItem;
    TextView textView;
    Button addToCart;
    ImageView imageView;
    //position in the aray adapter
    int pos;

    public DialogFragmentItem() {
        // Empty constructor required for DialogFragment
    }
    //instance of the class
    public static DialogFragmentItem newInstance() {
        DialogFragmentItem f = new DialogFragmentItem();
        return f;
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.item_detail, container, false);
        addToCart = (Button)view.findViewById(R.id.AddToCart);
        myItem = new Item();

        //call back inisilize
        try {
            mCallBacks = (ListItemListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ListItemListener interface");
        }

        Bundle bun = getArguments();
        myItem = bun.getParcelable("item");
        pos = bun.getInt("position");
        String title = myItem.getTitle();
        if (getDialog() == null) {
            return view;
        }
        getDialog().setTitle(title);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBacks.onItemClicked(pos);
                Toast.makeText(getActivity(), "Price:" + myItem.getPrice() + " Ratings:" + myItem.getRatings() + " Title:" + myItem.getTitle(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }


}
