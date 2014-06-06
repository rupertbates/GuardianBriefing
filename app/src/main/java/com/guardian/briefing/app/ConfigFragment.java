package com.guardian.briefing.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConfigFragment extends Fragment implements RadialTimePickerDialog.OnTimeSetListener {
    public static final String LOG_TAG = "GuardianBriefing";
    public static final String FRAG_TAG_TIME_PICKER = "frag_time_picker";

    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.time_list);
        listView.setAdapter(new AlertAdapter(getActivity(), getAlertTimes()));
        return rootView;
    }

    private List<AlertTime> getAlertTimes(){
        SharedPreferences preferences = getActivity().getSharedPreferences("com.guardian.briefing.app.preferences", Activity.MODE_PRIVATE);
        String[] alertTimes = preferences.getString("alert_times", "08:00,12:00,18:00").split(",");
        ArrayList<AlertTime> times = new ArrayList<AlertTime>(alertTimes.length);
        for (String alertTime : alertTimes) {
            times.add(new AlertTime(alertTime));
        }

        return times;
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {

    }


    private class AlertAdapter extends ArrayAdapter<AlertTime> {

        private final List<AlertTime> alertTimes;

        public AlertAdapter(Context context, List<AlertTime> alertTimes) {
            super(context, R.layout.item_alert, R.id.time, alertTimes);
            this.alertTimes = alertTimes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            convertView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                            .newInstance(ConfigFragment.this, 10, 30,
                                    DateFormat.is24HourFormat(getActivity()));

                        timePickerDialog.show(getActivity().getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);

                }
            });
            return convertView;
        }
    }
    private class AlertTime {
        Date time;
        public AlertTime(String alertTime) {
            time = getNextDownloadTime(alertTime);
        }

        public Date getNextDownloadTime(String timeString) {
            //get the time set in preferences
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date time = null;
            try {
                time = format.parse(timeString);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "error parsing scheduled download time", e);
            }

            Calendar downloadTime = Calendar.getInstance();
            downloadTime.set(Calendar.HOUR_OF_DAY, time.getHours());
            downloadTime.set(Calendar.MINUTE, time.getMinutes());

            //if the time has already passed today then
            // schedule it to start tomorrow
            if (downloadTime.before(Calendar.getInstance())) {
                downloadTime.add(Calendar.DAY_OF_YEAR, 1);
            }
            return downloadTime.getTime();
        }

        @Override
        public String toString() {
            return new SimpleDateFormat("h:mm a").format(time);
        }
    }
}
