package com.guardian.briefing.app;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.util.List;

class AlertAdapter extends ArrayAdapter<AlertTime> {

    private static final String FRAG_TAG_TIME_PICKER = "FRAG_TAG_TIME_PICKER";
    private MainActivity mainActivity;
    private final List<AlertTime> alertTimes;

    public AlertAdapter(MainActivity mainActivity, Context context, List<AlertTime> alertTimes) {
        super(context, R.layout.item_alert, R.id.time, alertTimes);
        this.mainActivity = mainActivity;
        this.alertTimes = alertTimes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        final AlertTime alertTime = alertTimes.get(position);
        convertView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog.newInstance(
                        new TimeSetHandler(alertTime),
                        alertTime.getHour(),
                        alertTime.getMinute(),
                        DateFormat.is24HourFormat(mainActivity));

                timePickerDialog.show(mainActivity.getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });
        convertView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.LOG_TAG, "Removing alert at " + alertTime.toString());
                alertTimes.remove(alertTime);
                mainActivity.onAlertCancelled(alertTime);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class TimeSetHandler implements RadialTimePickerDialog.OnTimeSetListener{
        private final AlertTime time;

        public TimeSetHandler(AlertTime time){
            this.time = time;
        }
        @Override
        public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
            Log.i(MainActivity.LOG_TAG, "Time set to " + hour + ":" + minute);
            mainActivity.onAlertCancelled(time);
            time.setTime(hour + ":" + minute);
            mainActivity.onAlertTimeChanged(alertTimes);
            notifyDataSetChanged();
        }
    }
}
