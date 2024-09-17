package com.example.projectfit.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.projectfit.Activities.MainActivity;
import com.example.projectfit.R;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "WATER_REMINDER_CHANNEL";
    private static final int NOTIFICATION_ID = 1;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            showNotification();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private void showNotification() {
        Context context = getApplicationContext();

        // Create the notification channel if it doesn't exist
        createNotificationChannel(context);

        // Intent to open the app's main activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // "Did it!" action
        PendingIntent didItPendingIntent = getActionPendingIntent(context, "ACTION_DID_IT");

        // "Remind me later!" action
        PendingIntent remindMeLaterPendingIntent = getActionPendingIntent(context, "ACTION_REMIND_LATER");

        // Build and display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.timer) // Replace with your app icon
                .setContentTitle("Alert!")
                .setContentText("Drink 200 ml water.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.raindrop, "Did it!", didItPendingIntent) // Action 1
                .addAction(R.drawable.bell, "Remind me later!", remindMeLaterPendingIntent) // Action 2
                .setAutoCancel(true); // Close notification after action

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Water Reminder";
            String descriptionText = "Channel for water reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(descriptionText);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private PendingIntent getActionPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}
