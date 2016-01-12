package com.example.robin.hungryeye.payment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.robin.hungryeye.MainActivity;
import com.example.robin.hungryeye.R;

/**
 * Created by robingurung on 11/01/2016.
 */
public class ReceiptDialogFragment extends DialogFragment {
    TextView paymentID,paymentClient,titleView,titlePrice;
    Button okButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.receipt_layout, container, false);
        paymentID = (TextView)view.findViewById(R.id.paymentID);
        paymentClient = (TextView)view.findViewById(R.id.client);
        okButton = (Button)view.findViewById(R.id.ok);
        //call back inisilize

        Bundle bun = getArguments();
        Object id =bun.getString("paymentId");
        //Object client = bun.getString("paymentClient");
        paymentID.setText(""+id);
        //paymentClient.setText(""+client);
        getDialog().setTitle("Your Receipt");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write to database
                dismiss();
                Intent i = new Intent(ReceiptDialogFragment.this.getActivity(), MainActivity.class);
                startActivity(i);

            }
        });
        return view;
    }
}
