package com.guardian.briefing.app;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ScheduledDownloadReceiver extends BroadcastReceiver {

    private static PowerManager.WakeLock mLock;
    private static WifiManager.WifiLock mWifiLock;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(MainActivity.LOG_TAG, "Received scheduled download notification");
        new GuardianNotificationManager(context).showNotification();
    }




    public static void acquireLocks(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.LOG_TAG);


        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, MainActivity.LOG_TAG);

        mLock.acquire();
        mWifiLock.acquire();

    }
    public static void releaseLocks() {
        if (mLock != null && mLock.isHeld()) {
            mLock.release();
        }
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }
}