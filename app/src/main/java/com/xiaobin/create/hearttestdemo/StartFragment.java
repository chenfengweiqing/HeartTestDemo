package com.xiaobin.create.hearttestdemo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.xiaobin.create.hearttestdemo.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    //view
    private TextView mTimeIntervalView;
    private TextView mMaxCountView;
    private Button mStartButton;
    private Button mStopButton;

    private String mTimeInterval;
    private String mMaxCount;
    private Context mContext;

    // time interval alarmManager
    private long interval;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;
    private Intent mStartIntent;
    private boolean mFlag = false;

    //broadcastReceiver
    private BatteryReceiver receiver=null;
    private int mCurrent;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre = getActivity().getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = pre.edit();

        mTimeInterval = Util.getTimeIntervalEntry(getActivity(), pre.getString(Util.PRE_TIME_INTERVAL_KEY, getResources().getString(R.string.default_interval))) ;
        mMaxCount = pre.getString(Util.PRE_RECORD_COUNT_KEY, getResources().getString(R.string.default_count));

        mContext = getActivity();

        receiver=new BatteryReceiver();
        IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(receiver, filter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTimeIntervalView = (TextView) getView().findViewById(R.id.start_interval);
        mTimeIntervalView.setText(mTimeInterval);
        mMaxCountView = (TextView) getView().findViewById(R.id.start_count);
        mMaxCountView.setText(getResources().getString(R.string.record_time_pre) + mMaxCount + getResources().getString(R.string.record_time_end));
        mStartIntent = new Intent(mContext, HeartService.class);
        mStartButton = (Button) getView().findViewById(R.id.start);
        mStopButton = (Button) getView().findViewById(R.id.data_clear);
        mFlag = pre.getBoolean(Util.PRE_START_ATOP_FLAG, true);
        if(mFlag){
            mStartButton.setText(getResources().getString(R.string.start));
        }else{
            mStartButton.setText(getResources().getString(R.string.stop));
        }

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start :
                if(pre.getBoolean(Util.PRE_START_ATOP_FLAG, true)) {
                    mStartButton.setText(getResources().getString(R.string.stop));
                    editor.putBoolean(Util.PRE_START_ATOP_FLAG, false);
                    editor.commit();
                    pendingIntent = PendingIntent.getService(getActivity(), 0, mStartIntent, 0);
                    am = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                    interval = Long.valueOf(pre.getString(Util.PRE_TIME_INTERVAL_KEY, getResources().getString(R.string.default_interval)));
                    am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                }else{
                    stopRepeating();
                }
                break ;
            case R.id.data_clear :
                showDialog(getView());
                break ;
            default :
                break ;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void showDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.clear_data));
        builder.setMessage(getResources().getString(R.string.clear_data_yes_or_no));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getContentResolver().delete(Uri.parse("content://heartContentProvider/heart"), null, null);
                stopRepeating();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void stopRepeating(){
        if(pendingIntent!=null) {
            am.cancel(pendingIntent);
        }
        mStartButton.setText(getResources().getString(R.string.start));
        editor.putInt(Util.PRE_CURRENT_ALREADY_RECORD_TIME, 1);
        editor.putBoolean(Util.PRE_START_ATOP_FLAG, true);
        editor.commit();
    }

    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);  //获得当前电量
            pre = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
            editor = pre.edit();
            editor.putInt(Util.PRE_CURRENT_BATTERY_POWER,mCurrent);
            editor.commit();
        }
    }
}
