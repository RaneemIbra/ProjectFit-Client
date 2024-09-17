package com.example.projectfit.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.projectfit.Activities.MainActivity;
import com.example.projectfit.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "WATER_REMINDER_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_DID_IT = "ACTION_DID_IT"; // Action for "Did it!"
    private static final String ACTION_REMIND_LATER = "ACTION_REMIND_LATER"; // Action for "Remind me later!"

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_DID_IT.equals(action)) {
                increaseWaterIntake(context); // Increase water intake by 200 ml
                closeNotification(context); // Close the notification
            } else if (ACTION_REMIND_LATER.equals(action)) {
                remindMeLater(context); // Handle "Remind me later" action
                closeNotification(context); // Close the notification
            } else {
                showNotification(context); // Show the notification again if no specific action
            }
        }
    }

    private void increaseWaterIntake(Context context) {
        Intent intent = new Intent(context, MainActivity.class);  // Replace with the correct activity or service to update the water intake
        intent.putExtra("ACTION_INCREASE_WATER", 200);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Add this flag

        context.startActivity(intent);
    }

    private void remindMeLater(Context context) {
        // Logic to handle the "Remind me later!" action (you can set up another alarm or anything else here)
    }

    private void closeNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID); // Cancel the notification
    }

    private void showNotification(Context context) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // "Did it!" action
        PendingIntent didItPendingIntent = getActionPendingIntent(context, ACTION_DID_IT);

        // "Remind me later!" action
        PendingIntent remindMeLaterPendingIntent = getActionPendingIntent(context, ACTION_REMIND_LATER);

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

        // Check for notification permission before notifying
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                requestNotificationPermission(context);
            }
        } else {
            // For Android versions below 13, notify directly
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private PendingIntent getActionPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);  // Set action here
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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

    private void requestNotificationPermission(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
