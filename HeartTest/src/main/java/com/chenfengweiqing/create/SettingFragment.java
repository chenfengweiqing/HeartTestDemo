package com.chenfengweiqing.create;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.chenfengweiqing.create.utils.Util;

/**
 * Created by jack on 2015/11/10.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
    public  static ListPreference mTimeIntervalPreference;
    public  static ListPreference mRecordCountPreference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_setting);
        mTimeIntervalPreference = (ListPreference) findPreference("time_interval_list_preference");
        mRecordCountPreference = (ListPreference) findPreference("record_count_list_preference");
        mTimeIntervalPreference.setSummary(Util.getTimeIntervalEntry(getActivity(), mTimeIntervalPreference.getValue()));
        mRecordCountPreference.setSummary(mRecordCountPreference.getValue().toString()+getResources().getString(R.string.record_time_end));
        mTimeIntervalPreference.setOnPreferenceChangeListener(this);
        mRecordCountPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference==mTimeIntervalPreference){
            mTimeIntervalPreference.setSummary(Util.getTimeIntervalEntry(getActivity(),newValue.toString()));
            return true;
        }else if(preference==mRecordCountPreference){
            mRecordCountPreference.setSummary(newValue.toString()+getResources().getString(R.string.record_time_end));
            return true;
        }else{
            return false;
        }
    }
}
