package com.guardian.briefing.app;

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


public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = "GuardianBriefing";
    private static final String FRAG_TAG_TIME_PICKER = "frag_tag_time_picker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.time_list);
        listView.setAdapter(new AlertAdapter(this, this, getAlertTimes()));
        listView.setOnItemClickListener(this);
    }

    private List<AlertTime> getAlertTimes() {
        SharedPreferences preferences = getSharedPreferences("com.guardian.briefing.app.preferences", MODE_PRIVATE);
        String[] alertTimes = preferences.getString("alert_times", "08:00,12:00,18:00").split(",");
        ArrayList<AlertTime> times = new ArrayList<AlertTime>(alertTimes.length);
        for (String alertTime : alertTimes) {
            times.add(new AlertTime(alertTime));
        }

        return times;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TimePickerBuilder tpb = new TimePickerBuilder()
                .setStyleResId(R.style.BetterPickersDialogFragment);
        tpb.show();
    }

}
