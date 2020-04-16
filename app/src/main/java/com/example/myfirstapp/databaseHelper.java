package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper
{
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "timeStamp.db";
    public static final String TABLE_NAME = "sensor_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TimeStamp";
    public static final String COL_3 = "AccelerationValue";

    //public static final String COL_3 = "XaxisAcceleration";
    //public static final String COL_4 = "YaxisAcceleration";
    //public static final String COL_5 = "ZaxisAcceleration";
    //initialize the database
    //xyz coordinates
    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db  = getWritableDatabase();
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = (" create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , TimeStamp LONG , AccelerationValue DOUBLE)");
        db.execSQL(CREATE_TABLE);
        //db.execSQL(" create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , TimeStamp FLOAT , AccelerationValueX DOUBLE, AccelerationValueY DOUBLE, AccelerationValueZ DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


   public  boolean insertData(long timestamp , float acceleration)
    {
        SQLiteDatabase db = this.getWritableDatabase();   //instance of getWritable database class - to insert data into the DATABASE
        ContentValues contentValues = new ContentValues();   //create an instance of contentValues class to insert content into database
        contentValues.put(COL_2,timestamp);         // puts timestamp value in column 2
        contentValues.put(COL_3,acceleration);     //puts content value  acceleration in column 1
        long result = db.insert(TABLE_NAME , null , contentValues);  //inserts the values into table
        if(result == -1)
            return false;
        else
            return true;



    }


//    public  boolean insertData(long timestamp hb, float acc_X , float acc_Y , float acc_Z)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();   //instance of getWritable database class - to insert data into the DATABASE
//        ContentValues contentValues = new ContentValues();   //create an instance of contentValues class to insert content into database
//        contentValues.put(COL_2,timestamp);         // puts timestamp value in column 2
//        contentValues.put(COL_3,acc_X);     //puts content value  acceleration in column 1
//        contentValues.put(COL_4,acc_Y);     //puts content value  acceleration in column 2
//        contentValues.put(COL_5,acc_Z);     //puts content value  acceleration in column 3
//
//        long result = db.insert(TABLE_NAME , null , contentValues);  //inserts the values into table
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
}
