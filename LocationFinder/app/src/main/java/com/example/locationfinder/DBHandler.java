package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    //create a database name
    public static final String DB_NAME ="LOCATION_FINDER_APP_DB";

    //table name
    public static final String TABLE_NAME = "LOCATIONS";

    //columns
    public static final String ID = "id";
    public static final String ADDR = "address";
    public static final String LA = "latitude";
    public static final String LONG = "longitude";

    //database version
    static final int DB_VERSION = 1;

    //constructor for database handler
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //create a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LA + " TEXT NOT NULL, " +
                LONG + " TEXT, " +
                ADDR + " TEXT);";
        db.execSQL(query);
    }

    //add to table
    public boolean addOne(LocationModel locationModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDR, locationModel.getAddress());
        values.put(LA, locationModel.getLatitude());
        values.put(LONG, locationModel.getLongitude());

        long insert = db.insert(TABLE_NAME, null, values);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    //geteveryone from table
    public List<LocationModel> getEveryone(){
        List<LocationModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int locationID = cursor.getInt(0);
                String locationAddr = cursor.getString(3);
                String locationLa = cursor.getString(1);
                String locationLong = cursor.getString(2);

                LocationModel newlocation = new LocationModel(locationID, locationAddr, locationLa, locationLong);
                returnList.add(newlocation);
            }while(cursor.moveToNext());
        }else{
            //failure
        }
        cursor.close();
        db.close();

        return returnList;
    }

    //delete record from table
    public boolean deleteOne(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + ADDR + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{address});

        if(cursor.getCount() > 0 ){
            long result = db.delete(TABLE_NAME, "address=?", new String[]{address});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    //update a record to table
    public boolean updateOne(String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDR, address);
        values.put(LA, latitude);
        values.put(LONG,longitude);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ADDR + " = ?", new String[]{address});
        long result = db.update(TABLE_NAME, values, "address=?", new String[]{address});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //search a record
    public List<LocationModel> searchOne(String address){
        List<LocationModel> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ADDR + " = ?", new String[]{address});
        if(cursor.moveToFirst()){
            do{
                int locationID = cursor.getInt(0);
                String locationAddr = cursor.getString(3);
                String locationLa = cursor.getString(1);
                String locationLong = cursor.getString(2);

                LocationModel newlocation = new LocationModel(locationID, locationAddr, locationLa, locationLong);
                returnList.add(newlocation);
            }while(cursor.moveToNext());
        }else{
            //failure
        }
        cursor.close();
        db.close();

        return returnList;

    }


    //upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
    }
}
