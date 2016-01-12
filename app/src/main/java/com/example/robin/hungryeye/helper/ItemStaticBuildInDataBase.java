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
 * Created by robingurung on 06/01/2016.
 */
public class ItemStaticBuildInDataBase {
    public static final String TAG = ItemStaticBuildInDataBase.class.getSimpleName();

    private ItemStaticHelper itemStaticHelper;
    private SQLiteDatabase mDatabase;

    public ItemStaticBuildInDataBase(Context context){
        itemStaticHelper = new ItemStaticHelper(context);
        mDatabase = itemStaticHelper.getWritableDatabase();
    }

    public void addItems(String itemname,String imagepath,String category,BigDecimal price,int ratings,String sku) {

        /*
        //if clear privious is true then it gonna delete all the items in the table, else its going to add it to the end of the table
        if(true){
            deleteAll();
        }*/

        String priceInString = convertBigToString(price);
        //sql prepared statement
        String sql = "INSERT INTO "+ItemStaticHelper.TABLE_NAME + " VALUES(?,?,?,?,?,?,?);";
        //compile the statement and start the transction
        SQLiteStatement sqLiteStatement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

            sqLiteStatement.clearBindings();
            //for a given column index simply bind the data to be put inside that index
            sqLiteStatement.bindString(2,itemname);
            sqLiteStatement.bindString(3,imagepath);
            sqLiteStatement.bindString(4,category);
            sqLiteStatement.bindString(5, priceInString);
            sqLiteStatement.bindLong(6, ratings);//no int so long
            sqLiteStatement.bindString(7,sku);


            sqLiteStatement.execute();


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
        String []columns = {ItemStaticHelper.KEY_ID,
                ItemStaticHelper.KEY_NAME,
                ItemStaticHelper.KEY_IMAGE,
                ItemStaticHelper.KEY_CATEGORY,
                ItemStaticHelper.KEY_PRICE,
                ItemStaticHelper.KEY_RATINGS,
                ItemStaticHelper.KEY_SKU
        };
        Log.d(TAG, " before arraylist creation");
        Cursor cursor = mDatabase.query(ItemStaticHelper.TABLE_NAME,columns,null,null,null,null,null);
        Log.d(TAG, " after cursor");
        if(cursor != null && cursor.moveToFirst()){
            do{
                //create a Item object and retrive the data from the cursor to be stored in the movie list

                Item item = new Item();
                item.setTitle(cursor.getString(cursor.getColumnIndex(ItemStaticHelper.KEY_NAME)));
                item.setImage(cursor.getString(cursor.getColumnIndex(ItemStaticHelper.KEY_IMAGE)));
                item.setCategory(cursor.getString(cursor.getColumnIndex(ItemStaticHelper.KEY_CATEGORY)));
                bigDecimal = new BigDecimal(cursor.getString(cursor.getColumnIndex(ItemStaticHelper.KEY_PRICE)));
                item.setPrice(bigDecimal);
                item.setRatings(cursor.getInt(cursor.getColumnIndex(ItemStaticHelper.KEY_RATINGS)));
                item.setRatings(cursor.getInt(cursor.getColumnIndex(ItemStaticHelper.KEY_SKU)));

                Log.d(TAG, " "+item.getTitle()+":"+item.getCategory()+":"+item.getImage());
                //add the item list object to the arraylist to return
                itemList.add(item);
            }while(cursor.moveToNext());
        }
        return itemList;
    }
    public void deleteAll(){
        mDatabase.delete(itemStaticHelper.TABLE_NAME,null,null);
    }


    //inner class

    private class ItemStaticHelper extends SQLiteOpenHelper {
        private Context mContext;
        //we can put these sqlite database variables in appconfig class
        //for now we will keep it here
        private static final int DATABASE_VERSION = 1;
        //sqlite database name
        private static final String DATABASE_NAME = "items_db";
        //sqlite table name
        private static final String TABLE_NAME = "items_db";

        //variables which are the column of the database of the host[server]
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "itemname";
        private static final String KEY_IMAGE = "imagepath";
        private static final String KEY_CATEGORY = "category";
        private static final String KEY_PRICE = "price";
        private static final String KEY_RATINGS = "ratings";
        private static final String KEY_SKU = "sku";



        private static final String CREATE_ITEM_DB =  "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_IMAGE + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_PRICE + " TEXT," +
                KEY_RATINGS + " INTEGER," +
                KEY_SKU + " TEXT " +");";

        public ItemStaticHelper(Context context) {
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



    }//end of ineer class

}
