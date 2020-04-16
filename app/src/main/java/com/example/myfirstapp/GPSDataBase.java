package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.HashMap;

public class GPSDataBase extends SQLiteOpenHelper
{

    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Location.db";
    private static final String TABLE_NAME = "location_table";
    private static final String COL_1 = "ID";
    private static final String COL_LOCS = "Location";
    private static final String COL_TIME = "TimeSpent";

    private String DB_PATH = "/data/data/com.example.myfirstapp/databases";
    //private String DB_NAME

    //public static final String COL_TIME = "XaxisAcceleration";
    //public static final String COL_4 = "YaxisAcceleration";
    //public static final String COL_5 = "ZaxisAcceleration";
    //initialize the database
    //xyz coordinates
    public GPSDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db  = getWritableDatabase();
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = (" create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , Location STRING , TimeSpent LONG)");
        db.execSQL(CREATE_TABLE);
        //db.execSQL(" create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , TimeStamp FLOAT , AccelerationValueX DOUBLE, AccelerationValueY DOUBLE, AccelerationValueZ DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public  boolean insertData(String lc , long tm)
    {
        //long result = 0;

            SQLiteDatabase db = this.getWritableDatabase();   //instance of getWritable database class - to insert data into the DATABASE
            ContentValues contentValues = new ContentValues();   //create an instance of contentValues class to insert content into database
            //contentValues.put
            contentValues.put(COL_LOCS, lc);         // puts timestamp value in column 2
            contentValues.put(COL_TIME, tm);     //puts content value  acceleration in column 1
            long result = db.insert(TABLE_NAME, null, contentValues);  //inserts the values into table

            if (result == -1)
                return false;
            else
                return true;

    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }


    public boolean updateData(String locs, long time) {
        int res = 0 ;
        MainActivity m = new MainActivity();
        SQLiteDatabase db= this.getWritableDatabase();
        String[] columns = {COL_LOCS, COL_TIME};
        Cursor cursor = db.query(TABLE_NAME, columns, COL_LOCS+ " = '" + locs + "'", null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();

        HashMap<String,Integer> locationCodes = new HashMap<>();
        locationCodes.put("HOME" , 1);
        locationCodes.put("WORK" , 2);
        locationCodes.put("GYM",3);


        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TIME, time);

        int requiredIndex = locationCodes.get(locs);

        while (cursor.moveToNext()) {
            int indexLocation = cursor.getColumnIndex(COL_LOCS);
            int indexTime = cursor.getColumnIndex(COL_TIME);
            if(indexLocation == requiredIndex)
            {
                String l = cursor.getString(indexLocation);
                String t = cursor.getString(indexTime);
                ContentValues cv = new ContentValues();
                cv.put(COL_TIME, time);
                String[] whereArgs = {t};
                res = db.update(TABLE_NAME, contentValues, COL_TIME + " =? ", whereArgs);
                return res>1 ;
            }
            else
                continue;

        }
        return res<1;
    }


    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch(SQLiteException e) {

            //database does't exist yet.

        }//catch

        if(checkDB != null)
        {
            checkDB.close();

        }//if

        return checkDB != null ? true : false;

    }//checkDatabase
//    public  boolean insertData(long timestamp hb, float acc_X , float acc_Y , float acc_Z)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();   //instance of getWritable database class - to insert data into the DATABASE
//        ContentValues contentValues = new ContentValues();   //create an instance of contentValues class to insert content into database
//        contentValues.put(COL_LOCS,timestamp);         // puts timestamp value in column 2
//        contentValues.put(COL_TIME,acc_X);     //puts content value  acceleration in column 1
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
