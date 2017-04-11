package com.example.ntech.contactsandmemo.random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contactsandmemo2.db";
    public static final String TABLE_NAME = "contacts";
   // public static final String TABLE_NAME2 = "lol";

    public static final String COL_1 = "_id";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "PHONE";
    public static final String COL_4 = "EMAIL";
    public static final String COL_5 = "ADDRESS";
    public static final String COL_6 = "ADDED";

    public DatabaseHelper(Context context) {
        super(context,(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+DATABASE_NAME), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +TABLE_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT UNIQUE,PHONE TEXT,EMAIL TEXT,ADDRESS TEXT, ADDED TEXT)");
        //db.execSQL("CREATE TABLE " +TABLE_NAME2+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,KP_ID TEXT,LOC_ID TEXT,TILA INTEGER,LAST_UPDATED TEXT, updateStatus TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        //db.execSQL("CREATE TABLE " +TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,KP_ID TEXT UNIQUE,LOC_ID TEXT,TILA INTEGER,LAST_UPDATED TEXT)");
    }

    public long insertData(String NAME, String PHONE, String EMAIL, String ADDRESS) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,NAME);
        contentValues.put(COL_3,PHONE);
        contentValues.put(COL_4,EMAIL);
        contentValues.put(COL_5,ADDRESS);
        contentValues.put(COL_6,getNow());

        return db.insert(TABLE_NAME, null, contentValues);
        //return db.insertWithOnConflict(TABLE_NAME,null,contentValues, db.CONFLICT_REPLACE);
    }

    public boolean updateData(String kp_id, String loc_id, int tila) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,loc_id);
        contentValues.put(COL_4,tila);
        contentValues.put(COL_5,getNow());

        db.update(TABLE_NAME, contentValues, "KP_ID = ?",new String[] {kp_id});
        return true;
    }

    public Cursor getAllData(String loc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE LOC_ID = '"+loc_id+"'", null);
        //Cursor res = db.rawQuery("SELECT KP_ID FROM "+TABLE_NAME, null);
        return res;
    }

    /*
    public Cursor haeSiirtoHistoria() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME2, null);
        return res;
    }*/

    private String getNow(){
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }*/

    /*
    public Boolean moveAndClear() {
        //copy and clear
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+TABLE_NAME2+" (KP_ID,LOC_ID,TILA,LAST_UPDATED) SELECT KP_ID,LOC_ID,TILA,LAST_UPDATED FROM "+TABLE_NAME);
        db.execSQL("DELETE * FROM "+TABLE_NAME);
        return true;
    }*/


    /**
     * Get list of Users from SQLite DB as Array List
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM kp_siirrot";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", cursor.getString(0));
                map.put("KP_ID", cursor.getString(1));
                map.put("LOC_ID", cursor.getString(2));
                map.put("TILA", cursor.getString(3));
                map.put("LAST_UPDATED", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }
    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM kp_siirrot where updateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", cursor.getString(0));
                map.put("KP_ID", cursor.getString(1));
                map.put("LOC_ID", cursor.getString(2));
                map.put("TILA", cursor.getString(3));
                map.put("LAST_UPDATED", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT * FROM kp_siirrot where updateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE kp_siirrot SET updateStatus = '"+ status +"' WHERE ID="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public Cursor getAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT "+COL_2+" FROM "+TABLE_NAME, null);
        return data;
    }

    public Cursor getContact(String contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+"="+"'"+contact+"'", null);
        return data;
    }

}