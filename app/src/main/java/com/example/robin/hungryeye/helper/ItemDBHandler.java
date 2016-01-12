package com.example.robin.hungryeye.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Movie;
import android.util.Log;

import com.example.robin.hungryeye.frag.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robin on 12/29/2015.
 */
public class ItemDBHandler {

    public static final String TAG = ItemDBHandler.class.getSimpleName();

    //inner class object
    private  ItemHelper mHelper;

    //this variable must be call beacuse of the fact that oncreate method will not be called if getWriteable or getreadable is not called
    private SQLiteDatabase mDatabase;


    public ItemDBHandler(Context context){
        mHelper = new ItemHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }
    public void addItems(ArrayList<Item> itemArrayList,boolean clearPrevious) {


        //if clear privious is true then it gonna delete all the items in the table, else its going to add it to the end of the table
        if(clearPrevious){
            deleteAll();
        }
        /*
        //get the readable sqlite database
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.query(TABLE_NAME, null, null, null, null, null, null);
        String [] column_names = cursor.getColumnNames();

        for(int i = 0;i<column_names.length;i++) {
            Log.d(TAG, "Column names:" + column_names[i]);
        }

          //get a writeable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();
        //assign an object of ContentValues
        ContentValues values = new ContentValues();
        //add the credintial to values

        values.put(KEY_NAME, itemname);
        values.put(KEY_IMAGE, imagepath);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_PRICE, price);
        values.put(KEY_RATINGS, ratings);

        //insert the values into the db
        long id = db.insert(TABLE_NAME, null, values);
        //close the db
        db.close();
        //for logcat
        Log.d(TAG, "New item added to the db(sqlite) " + id);

        */

        //sql prepared statement
        String sql = "INSERT INTO "+ItemHelper.TABLE_NAME + " VALUES(?,?,?,?,?,?);";
        //compile the statement and start the transction
        SQLiteStatement sqLiteStatement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for(int i = 0;i<itemArrayList.size();i++){
            Item curItem = itemArrayList.get(i);
            sqLiteStatement.clearBindings();
            //for a given column index simply bind the data to be put inside that index
            sqLiteStatement.bindString(2,curItem.getTitle());
            sqLiteStatement.bindString(3,curItem.getImage());
            sqLiteStatement.bindString(4,curItem.getCategory());
            //sqLiteStatement.bindDouble(5, curItem.getPrice());
            sqLiteStatement.bindLong(6, curItem.getRatings());//no int so long

            Log.d(TAG,"inserting items.."+i);
            sqLiteStatement.execute();
        }

        //set the transaction as successful and end the transction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();


    }

    public ArrayList<Item> getItemList(){

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
        Cursor cursor = mDatabase.query(ItemHelper.TABLE_NAME,columns,null,null,null,null,null);
        Log.d(TAG, " after cursor");
        if(cursor != null && cursor.moveToFirst()){
            do{
                //create a Item object and retrive the data from the cursor to be stored in the movie list

                Item item = new Item();
                item.setTitle(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_NAME)));
                item.setImage(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_IMAGE)));
                item.setCategory(cursor.getString(cursor.getColumnIndex(ItemHelper.KEY_CATEGORY)));
                //item.setPrice(cursor.getDouble(cursor.getColumnIndex(ItemHelper.KEY_PRICE)));
                item.setRatings(cursor.getInt(cursor.getColumnIndex(ItemHelper.KEY_RATINGS)));

                Log.d(TAG, " "+item.getTitle()+":"+item.getCategory()+":"+item.getImage());
                //add the item list object to the arraylist to return
                itemList.add(item);
            }while(cursor.moveToNext());
        }

        //checking the items
        for(int i =0;i<itemList.size();i++){
            Log.d(TAG,"item:"+itemList.get(i));
        }
        return itemList;
    }
    public void deleteAll(){
        mDatabase.delete(ItemHelper.TABLE_NAME,null,null);
    }

    /*
    public HashMap<String, String> getMainMenu(String str) {

        Log.d(TAG, "inside getMainMenu");
        HashMap<String, String> temp = new HashMap<>();
        //query sqlite
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        //get the readable sqlite database
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor provide read and write access tot he result set return by the database query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // move to the first row in the db
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            temp.put("itemname", cursor.getString(1));
            temp.put("imagepath", cursor.getString(2));
            temp.put("category", cursor.getString(3));
            temp.put("price", cursor.getString(4));
            temp.put("ratings", cursor.getString(5));

        }
        //close the cursor
        cursor.close();
        //close the db
        db.close();
        Log.d(TAG, "Feteching item[info] from the sqlite");
        //return the user info
        return temp;
    }

    //count the number of rows in the database
    public int getRowCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rawCount = cursor.getCount();
        cursor.close();
        db.close();
        return rawCount;
    }

    //whenever user logs out then delete database
    public void deleteItemTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Item table has been deleted from sqlite");
    }

    */



    //inner class for Item helper
    private static class ItemHelper extends  SQLiteOpenHelper {

        private Context mContext;
        //we can put these sqlite database variables in appconfig class
        //for now we will keep it here
        private static final int DATABASE_VERSION = 1;
        //sqlite database name
        private static final String DATABASE_NAME = "items.db";
        //sqlite table name
        private static final String TABLE_NAME = "items";

        //variables which are the column of the database of the host[server]
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "itemname";
        private static final String KEY_IMAGE = "imagepath";
        private static final String KEY_PRICE = "price";
        private static final String KEY_RATINGS = "ratings";
        private static final String KEY_CATEGORY = "category";


        private static final String CREATE_ITEM_DB =  "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_IMAGE + " TEXT," +
                KEY_PRICE + " REAL," +
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

    }//end of inner class
}
