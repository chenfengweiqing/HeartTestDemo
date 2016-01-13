package com.xiaobin.create.hearttestdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LCZ on 2015-11-10.
 */
public class HeartSQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "heart.db";
    public final static String TABLE_HEART = "heart_test";
    private final static String CREATE_HEART_TABLE= " CREATE TABLE IF NOT EXISTS "+ TABLE_HEART +"("+
            InterHeart._ID + " INTEGER PRIMARY KEY, "
            + InterHeart.CURRENT_TIME + "  TEXT ,"
            + InterHeart.COUNT + "  INTEGER ,"
            + InterHeart.LOCAL_POWER + " TEXT )";
    private static HeartSQLiteOpenHelper sInstance = null;
    private static final int DATABASE_VERSION = 1;
    private HeartSQLiteOpenHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
    }
    static synchronized HeartSQLiteOpenHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new HeartSQLiteOpenHelper(context);
        }
        return sInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createBookTable(db);
    }
    private void createBookTable(SQLiteDatabase db){
        db.execSQL(CREATE_HEART_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public interface InterHeart{
        String _ID           = "_id";
        String CURRENT_TIME  = "_current_time";
        String COUNT         = "_count";
        String LOCAL_POWER   = "_local_power";
    }
}
