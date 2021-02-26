package com.bong.simplynews;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bong.simplynews.activity.WebActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final static String channelId = "channelId";
    final static String channelName = "keywordAlarm";
    final static String GROUP_KEY = "notificationGroup";

    NotificationManager notiManger;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("Token", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("FireBaseMessaging", "From: " + remoteMessage.getFrom());

        // Notification 메시지를 수신할 경우는
        // remoteMessage.notification?.body!! 여기에 내용이 저장되어있다.
        // Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)

        if (remoteMessage != null && remoteMessage.getData().size() > 0) {

            sendNotification(remoteMessage);
        } else {
            Log.i("수신에러: ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.");
            Log.i("data값: ", remoteMessage.getData().toString());
        }
    }


    private void sendNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String link = remoteMessage.getData().get("link");

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        int uniId = (int) (System.currentTimeMillis() / 7);

        // 일회용 PendingIntent
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임한다.
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        intent.putExtra("URL", link);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT);


        // 알림에 대한 UI 정보와 작업을 지정한다.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        NotificationCompat.Builder summary = new NotificationCompat.Builder(getApplicationContext(), channelId);

        builder.setSmallIcon(R.drawable.ic_stat_name) // 아이콘 설정
                .setContentTitle(title) // 제목
                .setContentText(body) // 메시지 내용
                .setAutoCancel(true)
                //.setSound(soundUri) // 알림 소리
                .setGroup(GROUP_KEY)
                .setContentIntent(pendingIntent); // 알림 실행 시 Intent

        summary.setContentTitle("알림")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setGroup(GROUP_KEY)
                .setAutoCancel(true)
                .setGroupSummary(true);    // 해당 알림이 Summary라는 설정

        notiManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //푸시울렸을때 화면깨우기.
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
        wakeLock.acquire(3000);

        // 알림 생성
        notiManger.notify(uniId, builder.build());
        notiManger.notify(0, summary.build());
    }


}
