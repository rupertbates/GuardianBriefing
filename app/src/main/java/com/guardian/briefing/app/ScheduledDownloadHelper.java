package com.guardian.briefing.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class ScheduledDownloadHelper {

    private final Context context;

    public ScheduledDownloadHelper(Context context) {
        super();
        this.context = context;
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedule(AlertTime alertTime) {
        cancel(alertTime);
        AlarmManager alarm = getAlarmManager();
        final PendingIntent pending = getPendingIntent(alertTime);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, alertTime.getTime(), AlarmManager.INTERVAL_DAY, pending);
    }

    public boolean downloadIsScheduled(AlertTime alertTime) {
        return PendingIntent.getBroadcast(context, 0, getIntent(alertTime), PendingIntent.FLAG_NO_CREATE) != null;
    }


    private PendingIntent getPendingIntent(AlertTime alertTime) {
        return PendingIntent.getBroadcast(context, 0, getIntent(alertTime), PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Intent getIntent(AlertTime alertTime) {
        Intent intent = new Intent(context, ScheduledDownloadReceiver.class);
        intent.setData(Uri.parse("timer:" + alertTime.toValue()));
        return intent;
    }

    public void cancel(AlertTime alertTime) {
        Log.i(MainActivity.LOG_TAG, "Trying to cancel alert for " + alertTime.toString());
        if (downloadIsScheduled(alertTime)) {
            Log.i(MainActivity.LOG_TAG, "Cancelling alert for " + alertTime.toString());
            final AlarmManager alarm = getAlarmManager();
            alarm.cancel(getPendingIntent(alertTime));
        }
    }

    public void schedule(List<AlertTime> alertTimes) {
        for (AlertTime alertTime : alertTimes) {
            Log.i(MainActivity.LOG_TAG, "Scheduling alert for " + alertTime.toString());
            schedule(alertTime);
        }
    }
}


