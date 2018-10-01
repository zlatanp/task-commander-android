package com.example.android.taskcommander.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "JSA-FCM";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.e(TAG, "eeeeeee");
        if (remoteMessage.getNotification() != null) {
            // do with Notification payload...
            // remoteMessage.getNotification().getBody()
            Log.e(TAG, "Title: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            // do with Data payload...
            // remoteMessage.getData()
        }
    }
}
