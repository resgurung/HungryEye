package com.example.robin.hungryeye.payment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.adapter.ItemAdapter;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.frag.DialogFragmentItem;
import com.example.robin.hungryeye.frag.Item;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


//paypal



import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

/*
    How payment gateway works

    1. Customer place order
    2. Marchent is verified
    3. The order and payment is sent
    4. Marchent request payment authrosization
    5. Payment getway authorize the payment
    6. the marchant confirms order
    7. marchant provide goods or service
    8. marchan request for payment


 */
public class BasketActivity extends AppCompatActivity  {

    private static final String TAG = BasketActivity.class.getSimpleName();
    ListView listView;
    TextView textView, cartID;
    ArrayList<Item>list;
    Item item;

    ItemAdapter itemAdapter;
    BigDecimal total;

    private Cart myCart;
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId(AppConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_basket);
        //textView =(TextView)findViewById(R.id.textView1);
        //cartID = (TextView)findViewById(R.id.cartID);
        listView = (ListView)findViewById(R.id.list_item);
        myCart = new Cart();
        myCart.setId("EmailID");
        cartID.setText(myCart.getId());

        item = new Item();
        list = new ArrayList<>();

        //setContentView(R.layout.menulist);

        itemAdapter = new ItemAdapter(BasketActivity.this,list);
        listView.setAdapter(itemAdapter);

        //total = BigDecimal.valueOf(item.getPrice());
        textView.setText("Total: "+total);

        //paypal service starts from here
        /*Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);*/


        Button orderPressed = (Button)findViewById(R.id.order);
        orderPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuyPressed(v);
            }
        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.


        PayPalPayment payment = new PayPalPayment(total, AppConfig.DEFAULT_CURRENCY, "Total",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(BasketActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

}



