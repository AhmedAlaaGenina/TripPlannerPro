package com.ahmedg.tripplannerpro.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive: AlertReceiver ");
//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
        Toast.makeText(context, " Alarm Ringing..", Toast.LENGTH_LONG).show();
//        context.startService(new Intent(context.getApplicationContext(), MyAlarmService.class));
//        System.out.println("AlertReceiver.onReceive");
//        Intent intent2 = new Intent(context, MainActivity.class);
//        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent2.setAction(Intent.ACTION_MAIN);
//         intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//        context.startActivity(intent2);
        DemoActivity demoActivity = new DemoActivity();
        Intent intent1 = new Intent(context, DemoActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent1);
    }
}
