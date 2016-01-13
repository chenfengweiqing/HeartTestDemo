package com.xiaobin.create.hearttestdemo.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by LCZ on 2015-11-10.
 */
public class HeartContentProvider extends ContentProvider {
    private SQLiteOpenHelper mOpenHelper;
    public static final String HEART_TEST = "heartContentProvider";
    private static final int HEART_ALL = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(HEART_TEST, "heart", HEART_ALL);
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = HeartSQLiteOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch(match){
            case HEART_ALL:
                qb.setTables(HeartSQLiteOpenHelper.TABLE_HEART);
                break;
            default:
                throw new UnsupportedOperationException("URI" + uri + "not support");
        }
        Cursor cursor = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch(match){
            case HEART_ALL:
                db.insert(HeartSQLiteOpenHelper.TABLE_HEART, HeartSQLiteOpenHelper.InterHeart._ID,values);
                break;
            default:
                throw new UnsupportedOperationException("URI" + uri + "not support");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch(match){
            case HEART_ALL:
                break;
            default:
                throw new UnsupportedOperationException("URI" + uri + "not support");
        }
        int count = db.delete(HeartSQLiteOpenHelper.TABLE_HEART, selection, selectionArgs);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch(match){
            case HEART_ALL:
                break;
            default:
                throw new UnsupportedOperationException("URI" + uri + "not support");
        }
        int count = db.update(HeartSQLiteOpenHelper.TABLE_HEART,values,selection,selectionArgs);
        return count;
    }
}
