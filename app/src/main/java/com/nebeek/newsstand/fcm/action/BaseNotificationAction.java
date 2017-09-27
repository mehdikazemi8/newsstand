package com.nebeek.newsstand.fcm.action;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.util.FileHelper;

import okhttp3.ResponseBody;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public abstract class BaseNotificationAction {
    private String action;
    private Context context;

    protected NotificationCompat.Builder notificationBuilder = null;

    public void run(Context context, RemoteMessage remoteMessage) {
        setContext(context);
    }

    public void getPictureFromServer(String largeIconUrl) {
        DataRepository.getInstance().downloadPhoto(largeIconUrl, new DataSource.DownloadPhotoCallback() {
            @Override
            public void onResponse(ResponseBody response) {
                String filePath = FileHelper.writeToDisk(response, context);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                notificationBuilder
                        .setLargeIcon(bitmap)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setStyle(new android.support.v4.app.NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                manager.notify(123, notificationBuilder.build());
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }

    protected void showNotification(Intent notificationIntent, RemoteMessage remoteMessage) {
        notificationIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new NotificationCompat.Builder(context, "0").
                setContentTitle(remoteMessage.getData().get("body"))
                .setContentText(remoteMessage.getData().get("message"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent).setAutoCancel(true);

        if (remoteMessage.getData().containsKey("largeIcon")) {
            getPictureFromServer(remoteMessage.getData().get("largeIcon"));
        } else {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(123, notificationBuilder.build());
        }
    }

    protected boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public String getAction() {
        return action;
    }

    protected void setAction(String action) {
        this.action = action;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
