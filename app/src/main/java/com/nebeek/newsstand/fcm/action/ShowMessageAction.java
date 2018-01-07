package com.nebeek.newsstand.fcm.action;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;
import com.nebeek.newsstand.MainActivity;
import com.nebeek.newsstand.fcm.MyFirebaseMessagingService;

import static com.nebeek.newsstand.util.AppConstants.MESSAGE_ID;

public class ShowMessageAction extends BaseNotificationAction {

    public ShowMessageAction() {
        setAction(MyFirebaseMessagingService.SHOW_MESSAGE);
    }

    @Override
    public void run(Context context, RemoteMessage remoteMessage) {
        super.run(context, remoteMessage);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra(MESSAGE_ID, remoteMessage.getData().get(MESSAGE_ID));

        showNotification(notificationIntent, remoteMessage);
    }
}
