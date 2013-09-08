package com.pennappsf13.cmu.TextBack;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/8/13
 * Time: 2:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextBackNotification {
    public static final String NOTIFICATION_TAG = "com.pennappsf13.cmu.TextBack.Notification";
    public static final int NOTIFICATION_NUM = 1231;

    private static TextBackNotification sNotification;

    private Context mContext;
    private Notification mNotification;
    private NotificationManager mNotificationManager;


    public static TextBackNotification get(Context c) {
        if (sNotification == null) {
            sNotification = new TextBackNotification(c);
        }
        return sNotification;
    }

    private TextBackNotification(Context c) {
        mContext = c;
        mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(String text) {
        mNotificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_NUM);

        Resources r = mContext.getResources();
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class),0);

        mNotification = new NotificationCompat.Builder(mContext)
                .setTicker(r.getString(R.string.service_is_on))
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentTitle(r.getString(R.string.service_title))
                .setContentText("Message: " + text)
                .setContentIntent(pi)
                .setAutoCancel(false).getNotification();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_NUM, mNotification);

    }

    public void stopNotification() {
        mNotificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_NUM);
    }

}
