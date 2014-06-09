package com.guardian.briefing.app;

import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    public static final String LOG_TAG = "GuardianBriefing";
    public static final String ALERT_TIMES_KEY = "alert_times";
    private SharedPreferences preferences;
    private ScheduledDownloadHelper scheduleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("com.guardian.briefing.app.preferences", MODE_PRIVATE);
        List<AlertTime> alertTimes = getAlertTimes();

        scheduleHelper = new ScheduledDownloadHelper(this);
        scheduleHelper.schedule(alertTimes);

        ListView listView = (ListView) findViewById(R.id.time_list);
        listView.setAdapter(new AlertAdapter(this, this, getAlertTimes()));

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GuardianNotificationManager(MainActivity.this).showNotification();
            }
        });
    }

    private List<AlertTime> getAlertTimes() {
        String[] alertTimes = preferences.getString(ALERT_TIMES_KEY, "08:00,12:00,18:00").split(",");
        ArrayList<AlertTime> times = new ArrayList<AlertTime>(alertTimes.length);
        for (String alertTime : alertTimes) {
            times.add(new AlertTime(alertTime));
        }

        return times;
    }

    public void onAlertTimeChanged(List<AlertTime> alertTimes){
        StringBuilder sb = new StringBuilder(alertTimes.size());
        for (AlertTime alertTime : alertTimes) {
            sb.append(alertTime.toValue()).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        preferences.edit().putString(ALERT_TIMES_KEY, sb.toString()).apply();
        scheduleHelper.schedule(alertTimes);
    }

    public void onAlertCancelled(AlertTime alertTime){
        scheduleHelper.cancel(alertTime);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
