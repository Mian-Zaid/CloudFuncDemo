package com.example.cloudfunctionsdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {

    //generate new token for device and register in firestore
    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Map<String,Object> token=new HashMap<>();

        token.put("token",s);

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("DeviceTokens").document().set(token);
    }

    //receive notification and display it on device
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @org.jetbrains.annotations.NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                "myNotification",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }
}
