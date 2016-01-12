package com.example.robin.hungryeye.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.robin.hungryeye.MainActivity;
import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.adapter.CartAdapter;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.frag.Item;
import com.example.robin.hungryeye.helper.CartDBHandlerCopy;
import com.example.robin.hungryeye.helper.SessionManager;
import com.example.robin.hungryeye.nav.LoginActivity;

import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robingurung on 08/01/2016.
 */
public class CartFragment extends Fragment {

    private static final String TAG = CartFragment.class.getSimpleName();
    public static final String ARG_POSITION = "items";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private ListView listView;
    private CartAdapter adapter;
    private  Item item;
    private List<Item> itemList;
    private int quantity;
    // To store the products those are added to cart
    private List<PayPalItem> productsInCart ;
    private CartDBHandlerCopy dbHelper;
    private TextView textView,textView1;
    private Button payButton,deleteAll;
    private BigDecimal total;
    private SessionManager session;
    private ReceiptListener mReceiptListener;

    // Progress dialog
    private ProgressDialog pDialog;

    public CartFragment() {
        super();
    }
    public static CartFragment getInstance(){
        CartFragment fragment = new CartFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_basket, container, false);
        listView = (ListView)view.findViewById(R.id.list_item1);
        payButton = (Button)view.findViewById(R.id.order);
        //payButton = (Button)view.findViewById(R.id.deleteall);
        textView = (TextView)view.findViewById(R.id.total);
        textView1 = (TextView)view.findViewById(R.id.cartID);
        dbHelper = new CartDBHandlerCopy(getContext());
        itemList = dbHelper.getItemList();
        productsInCart  = new ArrayList<>();
        //session manager
        session =new SessionManager(CartFragment.this.getContext());
        pDialog = new ProgressDialog(CartFragment.this.getActivity());
        pDialog.setCancelable(false);

        //get the number of quantity of item
        quantity = 1;
        //get the receipt listener
        try {
            mReceiptListener = (ReceiptListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ListItemListener interface");
        }
        if(itemList.isEmpty()){
            Toast.makeText(getActivity(), "Cart is empty:item list as well  ", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "Cart is not empty:item in the list" , Toast.LENGTH_LONG).show();
            adapter = new CartAdapter(CartFragment.this.getActivity(),itemList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        total = new BigDecimal(dbHelper.getTotal());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Food")
                        .setMessage("Are you sure you want to delete this food?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //go to database and delete the item
                                dbHelper.deleteOneTime(position);
                                //delete from the itemlist
                                itemList.remove(position);
                                //show message that the item is deleted
                                Toast.makeText(CartFragment.this.getActivity(), " Deleted:"+position, Toast.LENGTH_SHORT).show();
                                total = new BigDecimal(dbHelper.getTotal());
                                //notify the adapter about the data changed
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });


        textView.setText("TOTAL : £ " + total);
        textView1.setText("MY CART");
        /*Bundle bun = getArguments();
        if(itemList.isEmpty()){
            Toast.makeText(getActivity(), "Cart is empty:item list as well  ", Toast.LENGTH_LONG).show();
        }else if(itemList.size()>=0){
            for (Item i : itemList) {

                title.setText("" + item.getTitle());
                cat.setText("" + item.getCategory());
                price.setText("" + item.getPrice());
            }
        }else if (bun == null) {
            Toast.makeText(getActivity(), "Cart is empty  ", Toast.LENGTH_LONG).show();
        } else if(bun!=null){
                Toast.makeText(getActivity(), "Index from Activity " + bun , Toast.LENGTH_LONG).show();
                position = bun.getInt("items");
                item = (Item) adapter.getItem(position);
                itemList.add(item);

        }*/

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for empty cart
                if(itemList.size()<=0){
                    Toast.makeText(CartFragment.this.getActivity(), "Cart is empty! Please add products to cart.", Toast.LENGTH_SHORT).show();
                }else {
                    if (session.isLoggedIn()) {
                        if (itemList.size() > 0) {
                            for (Item item : itemList) {
                                onAddToPayPalCartPressed(item);
                            }
                            launchPayPalPayment();
                        } else {
                            Toast.makeText(CartFragment.this.getActivity(), "Cart is empty! Please add products to cart.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Login Required")
                                .setMessage("Please Login to Continue..")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(CartFragment.this.getActivity(), LoginActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHelper.deleteAll();
                                        itemList.clear();
                                        Intent i = new Intent(CartFragment.this.getActivity(), MainActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                }

            }
        });
        return view;
    }

    /**
     * Launching PalPay payment activity to complete the payment
     * */
    private void launchPayPalPayment() {

        PayPalPayment thingsToBuy = prepareFinalCart();

        Intent intent = new Intent(CartFragment.this.getActivity(), PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, AppConfig.paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     * */
    private PayPalPayment prepareFinalCart() {

        /*PayPalItem[] paypal_items = new PayPalItem[productsInCart.size()];
        paypal_items = productsInCart.toArray(paypal_items);

        // Total amount
        //BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal subtotal = this.total;

        // If you have shipping cost, add it here
        BigDecimal shipping = new BigDecimal("0.0");

        // If you have tax, add it here
        BigDecimal tax = new BigDecimal("0.0");

        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
                shipping, subtotal, tax);

        BigDecimal amount = subtotal.add(shipping).add(tax);

        PayPalPayment payment = new PayPalPayment(
                amount,
                AppConfig.DEFAULT_CURRENCY,
                "Your eBill Subtotal: "+paymentDetails.getSubtotal()+" Shipping cost:"+paymentDetails.getShipping()+ " Tax:"+paymentDetails.getTax(),
                AppConfig.PAYMENT_INTENT);

        payment.items(paypal_items).paymentDetails(paymentDetails);

        // Custom field like invoice_number etc.,
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;*/


        //--- include an item list, payment amount details
        PayPalItem[] paypalItems = new PayPalItem[productsInCart.size()];
        paypalItems = productsInCart.toArray(paypalItems);

        for(int i = 0;i<paypalItems.length;i++){
            Log.d(TAG,"Paypal items:"+paypalItems[i]);
        }
             /*   {
                        new PayPalItem("sample item #1", quantity, new BigDecimal("7.50"), AppConfig.DEFAULT_CURRENCY,
                                "sku-12345678"),
                        new PayPalItem("free sample item #2", quantity, new BigDecimal("1.00"),
                                AppConfig.DEFAULT_CURRENCY, "sku-zero-price"),
                        new PayPalItem("sample item #3 with a longer name", quantity, new BigDecimal("7.99"),
                                AppConfig.DEFAULT_CURRENCY, "sku-33333")
                };*/
        BigDecimal subtotal = PayPalItem.getItemTotal(paypalItems);
        // If you have shipping cost, add it here
        BigDecimal shipping = new BigDecimal("7.21");
        // If you have tax, add it here
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, AppConfig.DEFAULT_CURRENCY, "Your Total:", AppConfig.PAYMENT_INTENT);
        payment.items(paypalItems).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    /**
     * Verifying the mobile payment on the server to avoid fraudulent payment
     * */
    private void verifyPaymentOnServer(final String paymentId,
                                       final String payment_client) {
        // Showing progress dialog before making request
        pDialog.setMessage("Verifying payment...");
        showpDialog();

        StringRequest verifyReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFY_PAYMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "verify payment: " + response.toString());
                //to print off the receipt
                    mReceiptListener.receiptListener(response.toString());
                try {
                    JSONObject res = new JSONObject(response);
                    boolean error = res.getBoolean("error");
                    String message = res.getString("message");

                    // user error boolean flag to check for errors

                    Toast.makeText(CartFragment.this.getActivity(), message,
                            Toast.LENGTH_SHORT).show();

                    if (!error) {
                        // empty the cart
                        productsInCart.clear();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // hiding the progress dialog
                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Verify Error: " + error.getMessage());
                Toast.makeText(CartFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hiding the progress dialog
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","payment");
                params.put("paymentId", paymentId);
                params.put("paymentClientJson", payment_client);

                return params;
            }
        };

        // Setting timeout to volley request as verification request takes sometime
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        verifyReq.setRetryPolicy(policy);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(verifyReq);
    }

    /**
     * Receiving the PalPay payment response
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject()
                                .toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment()
                                .toJSONObject().toString();

                        Log.e(TAG, "paymentId: " + paymentId
                                + ", payment_json: " + payment_client);


                        // Now verify the payment on the server side
                        verifyPaymentOnServer(paymentId, payment_client);


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ",
                                e);
                    }
                }
                itemList.clear();
                textView.setText("TOTAL : n/a");
                dbHelper.deleteAll();
                adapter.notifyDataSetChanged();
                //Intent i = new Intent(CartFragment.this.getActivity(),MainActivity.class);
                //startActivity(i);


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG,
                        "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void onAddToPayPalCartPressed(Item item) {

        //new PayPalItem("sample item #1", quantity, new BigDecimal("7.50"), AppConfig.DEFAULT_CURRENCY,"sku-12345678")


        PayPalItem product = new PayPalItem(item.getTitle(), quantity,
                item.getPrice(), AppConfig.DEFAULT_CURRENCY,item.getSku());

        productsInCart.add(product);

        Toast.makeText(CartFragment.this.getActivity(),
                item.getTitle() + " added to cart!", Toast.LENGTH_SHORT).show();

    }

    public PayPalItem getItem(List<PayPalItem> productsInCart){
        if(!productsInCart.isEmpty()){
            for(PayPalItem item:productsInCart ){
                return  item;
            }
        }
        return productsInCart.get(0);
    }


}
