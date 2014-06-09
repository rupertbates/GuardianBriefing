package com.guardian.briefing.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.guardian.briefing.app.content.Item;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GuardianNotificationManager implements Observer<List<Item>> {

    private final Context context;
    private Bitmap bitmap;


    public GuardianNotificationManager(Context context) {
        this.context = context;
    }

    public void showNotification() {
        createObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private Observable<List<Item>> createObservable() {
        return Observable.create(new Observable.OnSubscribe<List<Item>>() {
            @Override
            public void call(Subscriber<? super List<Item>> subscriber) {
                ArrayList<Item> items = new ContentHelper().getItems();
                bitmap = getBitmap(items.get(0).getMainImage().getSmallUrl());
                subscriber.onNext(items);
            }

        });
    }

    private void fireNotification(List<Item> items) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setContentTitle("Guardian Briefing");
        b.setContentText(items.get(0).title);
        b.setTicker("Guardian Briefing");
        b.setSmallIcon(R.drawable.ic_launcher_guardian);
        b.setWhen(System.currentTimeMillis());
        NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle(b);
        bigPicture.bigPicture(bitmap);
        bigPicture.setSummaryText(items.get(0).title);
        b.setStyle(bigPicture);
//        if(bitmap != null)
//            b.setLargeIcon(bitmap);
        //setDestination(extras, b);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(111, b.build());
    }

    private Bitmap getBitmap(String url) {
        try {
            return Picasso.with(context)
                    .load(url)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(MainActivity.LOG_TAG, "Error fetching content", e);
    }

    @Override
    public void onNext(List<Item> items) {
        fireNotification(items);
    }
}
