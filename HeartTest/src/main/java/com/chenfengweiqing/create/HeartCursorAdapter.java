package com.chenfengweiqing.create;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.chenfengweiqing.create.db.HeartSQLiteOpenHelper;

/**
 * Created by LCZ on 2015-11-11.
 */
public class HeartCursorAdapter extends CursorAdapter {


    public HeartCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder= new ViewHolder();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        View view=inflater.inflate(R.layout.list_heart_item ,parent,false);
        viewHolder.heartTime=(TextView) view.findViewById(R.id.heart_time );
        viewHolder.heartLocalPower=(TextView) view.findViewById(R.id.heart_local_power );
        viewHolder.heartSerialNumber=(TextView) view.findViewById(R.id.heart_serial_number );
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder=(ViewHolder) view.getTag();
        int count=cursor.getInt(cursor.getColumnIndex(HeartSQLiteOpenHelper.InterHeart.COUNT));
        String timeStr=cursor.getString(cursor.getColumnIndex(HeartSQLiteOpenHelper.InterHeart.CURRENT_TIME));
        String powerStr=cursor.getString(cursor.getColumnIndex(HeartSQLiteOpenHelper.InterHeart.LOCAL_POWER));
        String serialStr = context.getResources().getString(R.string.the)+count+context.getResources().getString(R.string.record_time_end);
        viewHolder.heartTime.setText(timeStr);
        viewHolder.heartLocalPower.setText(powerStr);
        viewHolder.heartSerialNumber.setText(serialStr);
    }
    class ViewHolder {
        TextView heartTime;
        TextView heartLocalPower;
        TextView heartSerialNumber;
    }
}
