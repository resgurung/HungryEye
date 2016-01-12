package com.example.robin.hungryeye.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by robin on 12/3/2015.
 * this class stores the credintail into the sqlite for the duration the use is logged in
 * query the sqlite is beter than querying the host api
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    //TAG for this class for logcat
    public static final String TAG = SQLiteHandler.class.getSimpleName();

    //we can put these sqlite database variables in appconfig class
    //for now we will keep it here
    private static final int DATABASE_VERSION =1;
    //sqlite database name
    private static final String DATABASE_NAME = "login_db";
    //sqlite table name
    private static final String TABLE_NAME = "users";

    //variables which are the column of the database of the host[server]
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";


    //constructor[must have when SQLiteOpenHelper is extended
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table
        String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_NAME+" TEXT,"+KEY_EMAIL+ " TEXT UNIQUE,"+KEY_UID+" TEXT,"
                +KEY_CREATED_AT+" TEXT"+")";
        db.execSQL(CREATE_TABLE);

        Log.d(TAG, "Database table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        //drop the db and create a new one
        onCreate(db);
    }

    //add the user details in the built in sqlitedb
    public void addUser(String name,String email, String uid, String created_at){
        //get a writeable sqlite database
        SQLiteDatabase db  = this.getWritableDatabase();
        //assign an object of ContentValues
        ContentValues values = new ContentValues();
        //add the credintial to values
        values.put(KEY_NAME,name);
        values.put(KEY_EMAIL,email);
        values.put(KEY_UID,uid);
        values.put(KEY_CREATED_AT,created_at);
        //insert the values into the db
        long id =db.insert(TABLE_NAME,null,values);
        //close the db
        db.close();
        //for logcat
        Log.d(TAG,"New user added to the db(sqlite) " +id);

    }
    //get the user
    public HashMap<String,String>getUserDetails(){
        HashMap<String, String> temp = new HashMap<>();
        //query sqlite
        String selectQuery = "SELECT * FROM "+ TABLE_NAME;
        //get the readable sqlite database
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor provide read and write access tot he result set return by the database query
        Cursor cursor = db.rawQuery(selectQuery,null);
        // move to the first row in the db
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            temp.put("name",cursor.getString(1));
            temp.put("email",cursor.getString(2));
            temp.put("uid",cursor.getString(3));
            temp.put("created_at",cursor.getString(4));
        }
        //close the cursor
        cursor.close();
        //close the db
        db.close();
        Log.d(TAG, "Feteching user[info] from the sqlite");
        //return the user info
        return temp;
    }

    //count the number of rows in the database
    public int getRowCount(){
        String countQuery = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rawCount = cursor.getCount();
        cursor.close();
        db.close();
        return rawCount;
    }
    //whenever user logs out then delete database
    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();

        Log.d(TAG, "User has been deleted from sqlite");
    }
}
