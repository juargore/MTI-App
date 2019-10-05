package com.cucea.mti;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Arturo on 5/6/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("--","AA: "+remoteMessage.getNotification());
        Log.e("--","AB: "+remoteMessage.getData());

        Intent intMs = new Intent(getApplication(), PantallaMensaje.class);
        intMs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(remoteMessage.getNotification().getTitle() != null){
            intMs.putExtra("header", remoteMessage.getNotification().getTitle());
        } else {
            intMs.putExtra("header", "Mensaje MTI");
        }
        intMs.putExtra("msg", remoteMessage.getNotification().getBody());
        intMs.putExtra("identificador", "1");
        startActivity(intMs);
    }*/

}