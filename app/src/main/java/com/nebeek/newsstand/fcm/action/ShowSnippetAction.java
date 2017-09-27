package com.nebeek.newsstand.fcm.action;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;
import com.nebeek.newsstand.MainActivity;
import com.nebeek.newsstand.fcm.MyFirebaseMessagingService;

import static com.nebeek.newsstand.util.AppConstants.SNIPPET_ID;

public class ShowSnippetAction extends BaseNotificationAction {

    public ShowSnippetAction() {
        setAction(MyFirebaseMessagingService.SHOW_SNIPPET);
    }

    @Override
    public void run(Context context, RemoteMessage remoteMessage) {
        super.run(context, remoteMessage);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra(SNIPPET_ID, remoteMessage.getData().get(SNIPPET_ID));

        showNotification(notificationIntent, remoteMessage);
    }
}
