package com.example.robin.hungryeye.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.robin.hungryeye.frag.Item;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by robin on 12/29/2015.
 */
public class CartDBHandlerCopy {

    public static final String TAG = CartDBHandlerCopy.class.getSimpleName();

    //inner class object
    private  ItemHelper mHelper;

    //this variable must be call beacuse of the fact that oncreate method will not be called if getWriteable or getreadable is not called
    private SQLiteDatabase mDatabase;


    public CartDBHandlerCopy(Context context){
        mHelper = new ItemHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }
    public void addItems(/*ArrayList<Item> itemArrayList*/Item item,boolean clearPrevious) {


        //if clear privious is true then it gonna delete all the items in the table, else its going to add it to the end of the table
        if(clearPrevious){
            deleteAll();
        }

        //sql prepared statement
        String sql = "INSERT INTO "+ ItemHelper.TABLE_NAME + " VALUES(?,?,?,?,?,?);";


        try {
            //compile the statement and start the transction
            SQLiteStatement sqLiteStatement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            sqLiteStatement.clearBindings();
            //for a given column index simply bind the data to be put inside that index
            sqLiteStatement.bindString(2,item.getTitle());
            sqLiteStatement.bindString(3,item.getImage());
            sqLiteStatement.bindString(4,item.getCategory());
            sqLiteStatement.bindString(5,convertBigToString(item.getPrice()));
            sqLiteStatement.bindLong(6, item.getRatings());//no int so long
            sqLiteStatement.execute();
        } catch(SQLException e){
            Log.e(TAG, "Creating table " + ItemHelper.TABLE_NAME + "because it doesn't exist!" );
            // create table
            ItemHelper.createTable(mDatabase);
            SQLiteStatement sqLiteStatement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            sqLiteStatement.clearBindings();
            //for a given column index simply bind the data to be put inside that index
            sqLiteStatement.bindString(2,item.getTitle());
            sqLiteStatement.bindString(3,item.getImage());
            sqLiteStatement.bindString(4,item.getCategory());
            sqLiteStatement.bindString(5, convertBigToString(item.getPrice()));
            sqLiteStatement.bindLong(6, item.getRatings());//no int so long
            sqLiteStatement.execute();
        }


        //set the transaction as successful and end the transction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();


    }
    public String convertBigToString(BigDecimal price){
        BigDecimal b = price;
        String s = b.toPlainString();
        return s;
    }
    public ArrayList<Item> getItemList(){

        BigDecimal bigDecimal = null;
        Log.d(TAG, " inside the method getItemsList()");
        ArrayList<Item> itemList = new ArrayList<>();
        //get the list of column
        String []columns = {ItemHelper.KEY_ID,
                ItemHelper.KEY_NAME,
                ItemHelper.KEY_IMAGE,
                ItemHelper.KEY_CATEGORY,
                ItemHelper.KEY_PRICE,
                ItemHelper.KEY_RATINGS
        };
        Log.d(TAG, " before arraylist creation");
        try {
            Cursor cursor = mDatabase.query(ItemHelper.TABLE_NAME, columns, null, null, null, null, null);
            Log.d(TAG, " after cursor");
            if(cursor != null && cursor.moveToFirst()){
                do{
                    //create a Item object and retrive the data from the cursor to be stored in the movie list

                    Item item = new Item();
                    item.setTitle(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_NAME)));
                    item.setImage(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_IMAGE)));
                    item.setCategory(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_CATEGORY)));
                    bigDecimal = new BigDecimal(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_PRICE)));
                    item.setPrice(bigDecimal);
                    item.setRatings(cursor.getInt(cursor.getColumnIndex(ItemHelper.KEY_RATINGS)));

                    Log.d(TAG, " "+item.getTitle()+":"+item.getCategory()+":"+item.getImage());
                    //add the item list object to the arraylist to return
                    itemList.add(item);
                }while(cursor.moveToNext());
            }
        }catch(SQLException e){
            Log.e(TAG, "Creating table " + ItemHelper.TABLE_NAME + "because it doesn't exist!" );
            // create table
            ItemHelper.createTable(mDatabase);
            Cursor cursor = mDatabase.query(ItemHelper.TABLE_NAME, columns, null, null, null, null, null);
            Log.d(TAG, " Table created after cursur");
            if(cursor != null && cursor.moveToFirst()){
                do{
                    //create a Item object and retrive the data from the cursor to be stored in the movie list

                    Item item = new Item();
                    item.setTitle(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_NAME)));
                    item.setImage(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_IMAGE)));
                    item.setCategory(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_CATEGORY)));
                    bigDecimal = new BigDecimal(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_PRICE)));
                    item.setPrice(bigDecimal);
                    item.setRatings(cursor.getInt(cursor.getColumnIndex(ItemHelper.KEY_RATINGS)));

                    Log.d(TAG, " "+item.getTitle()+":"+item.getCategory()+":"+item.getImage());
                    //add the item list object to the arraylist to return
                    itemList.add(item);
                }while(cursor.moveToNext());
            }
        }
        return itemList;
    }
    public void deleteAll(){
        try {
            if(mDatabase.isOpen()){
            mDatabase.delete(ItemHelper.TABLE_NAME, null, null);
            }
            else{
                mDatabase = mHelper.getWritableDatabase();
                mDatabase.delete(ItemHelper.TABLE_NAME, null, null);
            }
        }catch(SQLException e){

        }finally {
            if (this.mDatabase != null && this.mDatabase.isOpen())
            {
                this.mDatabase.close();
            }
        }
    }

    // get the total from the column

    public String getTotal(){
        double total = 0;
        try {
            synchronized (this.mDatabase) {
                //String column = "SELECT sum("+CartHelper.KEY_PRICE+") FROM "+CartHelper.DATABASE_NAME;
                if(mDatabase.isOpen()) {
                    Cursor cursor = this.mDatabase.query(ItemHelper.TABLE_NAME, new String[]{ItemHelper.KEY_PRICE}, null, null, null, null, null);
                    if(cursor != null && cursor.moveToFirst()) {
                        do {
                            //Item item = new Item();
                            //item.setPrice(cursor.getDouble(cursor.getColumnIndex(ItemHelper.KEY_PRICE)));
                            double ppp = cursor.getDouble(cursor.getColumnIndex(ItemHelper.KEY_PRICE));
                            total += ppp;
                            Log.d(TAG," total"+total);
                        } while (cursor.moveToNext());
                    }
                }
                else{
                    mDatabase = mHelper.getWritableDatabase();
                    Cursor cursor = this.mDatabase.query(ItemHelper.TABLE_NAME, new String[]{ItemHelper.KEY_PRICE}, null, null, null, null, null);
                    if(cursor != null && cursor.moveToFirst()) {
                        do {
                            //Item item = new Item();
                            double ppp = cursor.getDouble(cursor.getColumnIndex(ItemHelper.KEY_PRICE));
                            total += ppp;
                            Log.d(TAG," total"+total);
                        } while (cursor.moveToNext());
                    }
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            //catch illiligal argument
            e.printStackTrace();
        }
        finally
        {
            if (this.mDatabase != null && this.mDatabase.isOpen())
            {
                this.mDatabase.close();
            }
        }

        return String.valueOf(total);

    }

    public void deleteOneTime(int position){

        try {
            synchronized (this.mDatabase) {
                //this.mDatabase.delete(CartHelper.TABLE_NAME, CartHelper.KEY_ID + "='" + position + "';", null);
                if(mDatabase.isOpen()){
                    this.mDatabase.delete(ItemHelper.TABLE_NAME, ItemHelper.KEY_ID+" = " + position, null);
                }
                else{
                    mDatabase = mHelper.getWritableDatabase();
                    this.mDatabase.delete(ItemHelper.TABLE_NAME, ItemHelper.KEY_ID+" = " + position, null);
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            //catch illiligal argument
        }
        finally
        {
            if (this.mDatabase != null && this.mDatabase.isOpen())
            {
                this.mDatabase.close();
            }
        }




    }

    //inner class for Item helper
    private static class ItemHelper extends  SQLiteOpenHelper {

        private Context mContext;
        //we can put these sqlite database variables in appconfig class
        //for now we will keep it here
        private static final int DATABASE_VERSION = 1;
        //sqlite database name
        private static final String DATABASE_NAME = "items.db";
        //sqlite table name
        private static final String TABLE_NAME = "cart";

        //variables which are the column of the database of the host[server]
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "itemname";
        private static final String KEY_IMAGE = "imagepath";
        private static final String KEY_PRICE = "price";
        private static final String KEY_RATINGS = "ratings";
        private static final String KEY_CATEGORY = "category";


        private static final String CREATE_ITEM_DB =  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_IMAGE + " TEXT," +
                KEY_PRICE + " TEXT," +
                KEY_RATINGS + " INTEGER" + ");";




        public ItemHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            //create a table item
            try {
                db.execSQL(CREATE_ITEM_DB);
                Log.d(TAG, "Item Database table created");
            }catch(SQLException e){
                Log.d(TAG,e.getMessage());
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + this.TABLE_NAME);
                //drop the db and create a new one
                onCreate(db);
            }catch(SQLException e){
                Log.d(TAG,e.getMessage());
            }
        }
        public static void createTable(SQLiteDatabase db){
             String CREATE_ITEM_DB =  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_NAME + " TEXT," +
                    KEY_CATEGORY + " TEXT," +
                    KEY_IMAGE + " TEXT," +
                    KEY_PRICE + " REAL," +
                    KEY_RATINGS + " INTEGER" + ");";

            try {
                db.execSQL(CREATE_ITEM_DB);
                Log.d(TAG, "Item Database table created");
            }catch(SQLException e){
                Log.d(TAG,e.getMessage());
            }

        }

    }//end of inner class
}
