package com.chenfengweiqing.create;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.chenfengweiqing.create.db.HeartSQLiteOpenHelper;
import com.chenfengweiqing.create.utils.Util;

import java.text.SimpleDateFormat;


public class HeartService extends Service {
    private ContentValues values;
    private String mCurTime;
    private String mPower;
    private int count = 0;

    //battary
    private int mCurrent;
    private String mMaxCount;

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    private int mMaxTime;
    private BatteryReceiver receiver=null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver=new BatteryReceiver();
        IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        pre = getApplicationContext().getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
        editor = pre.edit();
        mMaxCount = pre.getString(Util.PRE_RECORD_COUNT_KEY, getResources().getString(R.string.default_count));
        mMaxTime = Integer.valueOf(mMaxCount);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("liaocanzhong", "--mMaxTime="+mMaxTime+"---");
        count = pre.getInt(Util.PRE_CURRENT_ALREADY_RECORD_TIME, 1);
        Log.d("liaocanzhong", "--count="+count+"---");
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
        mCurTime=sdf.format(new java.util.Date());
        mCurrent = pre.getInt(Util.PRE_CURRENT_BATTERY_POWER,110);
        mPower = String.valueOf(mCurrent);
        values = getValues(mCurTime, count, mPower);
        getContentResolver().insert(Uri.parse("content://heartContentProvider/heart"), values);
        count = count+1;
        editor.putInt(Util.PRE_CURRENT_ALREADY_RECORD_TIME, count);
        editor.commit();
        if (count>(mMaxTime)) {
            StartFragment.am.cancel(StartFragment.pendingIntent);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    private ContentValues getValues(String mCurTime,int count,String mPower){
        ContentValues values = new ContentValues();
        values.put(HeartSQLiteOpenHelper.InterHeart.CURRENT_TIME,mCurTime);
        values.put(HeartSQLiteOpenHelper.InterHeart.COUNT, count);
        values.put(HeartSQLiteOpenHelper.InterHeart.LOCAL_POWER, mPower);
        return values;
    }

    private class BatteryReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pre = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
            editor = pre.edit();
            editor.putInt(Util.PRE_CURRENT_BATTERY_POWER,mCurrent);
            editor.commit();
        }
    }
}
