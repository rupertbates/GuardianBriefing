package com.guardian.briefing.app;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class AlertTime {
    Date time;
    private SimpleDateFormat storageFormat;

    public AlertTime(String alertTime) {
        storageFormat = new SimpleDateFormat("HH:mm");
        time = getNextDownloadTime(alertTime);
    }

    private Date getNextDownloadTime(String timeString) {
        //get the time set in preferences

        Date time = null;
        try {
            time = storageFormat.parse(timeString);
        } catch (ParseException e) {
            Log.e(MainActivity.LOG_TAG, "error parsing scheduled download time", e);
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

    public void setTime(String timeString){
        time = getNextDownloadTime(timeString);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("h:mm a").format(time);
    }

    public String toValue(){
        return storageFormat.format(time);
    }

    public int getHour() {
        return time.getHours();
    }

    public int getMinute() {
        return time.getMinutes();
    }

    public long getTime() {
        return time.getTime();
    }
}
