package com.example.robin.hungryeye;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.frag.Item;
import com.example.robin.hungryeye.helper.CartDBHandlerCopy;
import com.example.robin.hungryeye.helper.ItemStaticBuildInDataBase;
import com.example.robin.hungryeye.helper.MenuListOfflineHelper;
import com.example.robin.hungryeye.helper.SQLiteHandler;
import com.example.robin.hungryeye.helper.SessionManager;
import com.example.robin.hungryeye.nav.ContactUs;
import com.example.robin.hungryeye.nav.LoginActivity;
import com.example.robin.hungryeye.nav.Recents;
import com.example.robin.hungryeye.nav.SettingActivity;
import com.example.robin.hungryeye.payment.Cart;
import com.example.robin.hungryeye.payment.CartFragment;
import com.example.robin.hungryeye.payment.ListItemListener;
import com.example.robin.hungryeye.payment.ReceiptDialogFragment;
import com.example.robin.hungryeye.payment.ReceiptListener;
import com.paypal.android.sdk.payments.PayPalService;

import java.util.ArrayList;
import java.util.HashMap;


/*
 This is the mainActivity
 */
public class MainActivity extends AppCompatActivity implements ListItemListener,ReceiptListener{

    //for logcat
    private static final String TAG = LoginActivity.class.getSimpleName();


    //job shedule from the appcompat 21<
    // Get an instance of the JobScheduler, this will delegate to the system JobScheduler on api 21+
    // and to a custom implementataion on older api levels.
    //JobScheduler jobScheduler;
    //just the job id reference
    private static final int JOB_ID = 100;

    private SessionManager session;
    private SQLiteHandler localDBhandler;
    private CartDBHandlerCopy cartDBHelper;
    private  Cart myCart;
    private ItemStaticBuildInDataBase staticBuildInDataBase;

    //declare titles and icon for navigation drawer list
    static final String[] TITLES = {"LOG IN/ LOG OUT","RECENTLY VIEWED","CONTACT US"};
    //static final int[] ICON = {R.drawable.ic_login,R.drawable.ic_recently,R.drawable.ic_contact,R.drawable.ic_about,R.drawable.ic_delivery};
    static final int[] ICON = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    //String resources for name and email for the header view
    //int resources for the profile picture in the header

    //String NAME = "Resham Gurung";
    //String EMAIL= "reshamgurung01@gmail.com";

    //variable to set when login is confirmed
    String NAME;
    String EMAIL;
    int PROFILE_IMAGE = R.mipmap.ic_launcher;

    //toolbar object
    private Toolbar toolbar;

    //recycleview for the slider navigation
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout mDrawerLayout;

    //tablayout
    TabLayout mTabLayout;
    //view pager
    ViewPager mViewPager;
    ActionBarDrawerToggle mDrawerToggle;

    MenuListOfflineHelper help;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //Assign the toolbar object to the view and set it to actionbar to toolbar
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        //*****************************************************//
        help = new MenuListOfflineHelper(getApplicationContext());
        Log.d(TAG, "Offline inisilize");
        Log.d(TAG, "Before add to list");
        help.addToList();
        //*****************************************************//

        //session manager
        session =new SessionManager(getApplicationContext());
        localDBhandler = new SQLiteHandler(getApplicationContext());
        //*****************************************************//

        staticBuildInDataBase = new ItemStaticBuildInDataBase(getApplicationContext());
        Log.d(TAG, "CARTDB inisilize");
        cartDBHelper = new CartDBHandlerCopy(getApplicationContext());
        Log.d(TAG, " Cart finish inisilize");

        //*****************************************************//
           // static database (offline)
        myCart = new Cart();
        //*****************************************************//

        if(session.isLoggedIn()){
            //floating logout button
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "You have logged out, Thank you", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    logoutUser();

                }
            });

            //get the details from the datbase
            HashMap<String, String> details = localDBhandler.getUserDetails();
            //set the name as the name of the user otherwise guest
            NAME =details.get("name");
            //set the name as the user email otherwise guest
            EMAIL = details.get("email");

        }
        else{
            logoutUser();
            //variable to set when login is confirmed
             NAME = "guest";
             EMAIL= "guest@guest.com";
        }




        //assign the tablayout object to the xml view
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        //create new tab, add it to the tab_layout and set the text
        mTabLayout.addTab(mTabLayout.newTab().setText("HOME"));
        mTabLayout.addTab(mTabLayout.newTab().setText("MENU"));
        mTabLayout.addTab(mTabLayout.newTab().setText("DELIVERY"));
        mTabLayout.addTab(mTabLayout.newTab().setText("ABOUT US"));







        //assign the mViewPager object to the activity_main.xml with the id pager
        mViewPager = (ViewPager)findViewById(R.id.pager);
        //create a object of the pageadapter class implemented in this project which takes two parameter
        final PageAdapter mPageadapter = new PageAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());
        //now set the adapter to the viewPager
        mViewPager.setAdapter(mPageadapter);
        //lishen to the change by onchangelistener
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //set the onselectlistener to the tablayout to lishen to the clicks on the tabs
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //left empty
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //left empty
            }
        });


        //Assign the recycleView object to the xml view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //fixed size
        mRecyclerView.setHasFixedSize(true);
        //creating adapter of MyAdapter class and passing the TITLES,ICONs, HEADER VIEW NAME, HEADER VIEW EMAIL and HEADER VIEW PROFILE PIC
        mAdapter = new MyAdapter(TITLES,ICON,NAME,EMAIL,PROFILE_IMAGE);
        //setting the adapter to recycleview
        mRecyclerView.setAdapter(mAdapter);

        //detect the tap on the navigational with GestureDector SimpleOnGestureListener
        // [Note: "new GestureDetector.SimpleOnGestureListener()" for complex tap]
        final GestureDetector mGestureDector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        //check if the item is clicked on the navigational panel
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                //get the motionevent x and y
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDector.onTouchEvent(e)) {
                    //close the drawer
                    mDrawerLayout.closeDrawer(rv);
                    //checking
                    //Toast.makeText(MainActivity.this, "The item clicked is:"+ mRecyclerView.getChildAdapterPosition(child), Toast.LENGTH_SHORT).show();

                    //pass the child int value to ontouch drawer method to open a new fragment in this case
                    onTouchDrawer(mRecyclerView.getChildAdapterPosition(child));
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                //left empty
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                //left empty
            }

        });


        //create a layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //set the layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Assign the drawer object to the View
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        //When the toggle is carried out
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //code here will execute once the drawer is open
                //this drawer doesn't do anything when the drawer is open so this space left empty
                Toast.makeText(MainActivity.this, " Opened", Toast.LENGTH_SHORT).show(); //just checking
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //code here will execute when the drawer is closed
                //this space is left empty because when the drawer is closed nothing happens
                //if you want to carry out something when the drawer is closed then place the code here
                Toast.makeText(MainActivity.this, " Closed", Toast.LENGTH_SHORT).show(); //just checking
            }
        };

        //mDrawerLayout object lishening to mDrawerToggle
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //set the drawer toggle sync state
        mDrawerToggle.syncState();

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, AppConfig.paypalConfig);

        startService(intent);

    }

    //logout the user
    public void logoutUser(){
        session.setLogin(false);
        localDBhandler.deleteUser();
        NAME = "guest";
        EMAIL= "guest@guest.com";
        //Intent i = new Intent(MainActivity.this,LoginActivity.class);
        //startActivity(i);
        //finish();
    }
    //now open a new Activity or fragment if any of the item is clicked
    public void onTouchDrawer(final int position){

        Intent i;
        if(position == 0){
            //start a new activity
            Toast.makeText(MainActivity.this, " Pressed:"+position, Toast.LENGTH_SHORT).show();
        }
        else if(position ==1){
            //start a new activity
            //Toast.makeText(MainActivity.this, " Pressed:"+position, Toast.LENGTH_SHORT).show();
            i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else if(position ==2){
            //start a new activity
            //Toast.makeText(MainActivity.this, " Pressed:"+position, Toast.LENGTH_SHORT).show();
            i = new Intent(MainActivity.this,Recents.class);
            startActivity(i);
        }
        else if(position ==3){
            //start a new activity
            //Toast.makeText(MainActivity.this, " Pressed:"+position, Toast.LENGTH_SHORT).show();
           i =new Intent(MainActivity.this, ContactUs.class);
            startActivity(i);
        }
        else
            Toast.makeText(MainActivity.this, " Press something you moron", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar
        //automatically handle clicks on the Home/Up button,
        //as you specify a parent activity in androidManifest
/*
        int id = item.getItemId();

        //no inspection Simplifiable If statement
        if(id ==R.id.action_settings){
            return true;
        }
        if(id ==R.id.basket){
            return true;
        }
        return super.onOptionsItemSelected(item);

*/
        Intent i;
        switch(item.getItemId()) {
            case R.id.action_settings:
                i = new Intent(MainActivity.this, SettingActivity.class);
                this.startActivity(i);
                finish();
                break;
            case R.id.basket:
                // another startActivity, this is for item with id "menu_item2"
                //i = new Intent(MainActivity.this, BasketActivity.class);
                //this.startActivity(i);
                //finish();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content, CartFragment.getInstance());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onItemClicked(int position) {

        /*
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article

        CartFragment cartFrag = (CartFragment)
                getSupportFragmentManager().findFragmentById(R.id.checkout);

        if (cartFrag != null) {
            // If cart frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            cartFrag.updateArticleView(position);
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            CartFragment newFragment = new CartFragment();
            Bundle args = new Bundle();
            args.putInt(CartFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(android.R.id.content, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }*/
        Log.d(TAG,""+position);
        ArrayList<Item> items = new ArrayList<>();
        items = staticBuildInDataBase.getItemList();
        Item item= items.get(position);
            //add it to the cart
        cartDBHelper.addItems(item,false);
        //AppController.getCartWriteableDatabase().addItems(item, true);

    }


    @Override
    public void receiptListener(String receipt) {
        openReceipt(receipt);
        Log.d(TAG," Receipt print: "+receipt);
    }
    public void openReceipt(String paymentID){
        DialogFragment dialogFragmentItem = new ReceiptDialogFragment();//the fragment you want to show
        Bundle bun = new Bundle();
        bun.putString("paymentId", paymentID);
        dialogFragmentItem.setArguments(bun);
        dialogFragmentItem.show(getSupportFragmentManager(),"receipt");
        //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //dialogFragmentItem.show(fragmentTransaction, "dialog");
    }
}
