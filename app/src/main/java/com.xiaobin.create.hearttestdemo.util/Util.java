package com.xiaobin.create.hearttestdemo.util;

import android.content.Context;

import com.xiaobin.create.hearttestdemo.R;

/**
 * Created by LCZ on 2015-11-10.
 */
public class Util {

    public static final String PREFERENCE_NAME = "com.xiaobin.create.hearttestdemo_preferences";
    public static final String PRE_TIME_INTERVAL_KEY = "time_interval_list_preference";
    public static final String PRE_RECORD_COUNT_KEY = "record_count_list_preference";
    public static final String PRE_CURRENT_BATTERY_POWER = "current_battery_power_preference";
    public static final String PRE_CURRENT_ALREADY_RECORD_TIME = "current_already_record_preference";
    public static final String PRE_START_ATOP_FLAG = "start_or_stop";


    public static String getEntry(Context context,int entryId,int entryValueInd,String entryValue){
        String[] entries = context.getResources().getStringArray(entryId);
        String[] entriesValue = context.getResources().getStringArray(entryValueInd);
        for(int i=0;i<entriesValue.length;i++){
            if(entriesValue[i].equals(entryValue)){
                return entries[i];
            }
        }
        return null;
    }

    public static String getTimeIntervalEntry(Context context,String entryValue){
        String[] entries = context.getResources().getStringArray(R.array.time_interval_entry);
        String[] entriesValue = context.getResources().getStringArray(R.array.time_interval_entry_values);
        for(int i=0;i<entriesValue.length;i++){
            if(entriesValue[i].equals(entryValue)){
                return entries[i];
            }
        }
        return null;
    }


}
