package com.brianmsurgenor.honoursproject.FoodDiary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.R;

/**
 * Reciever class for the meal notifications set up in the meal entry
 */
public class MealNotifReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String meal = intent.getStringExtra(MealDateContract.Columns.MEAL_TYPE);

        /*
         * Set up the notification details to send the user to the meal entry screen on tapping the
         * notification and build it
         */
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context, MealEntryActivity.class);
        newIntent.putExtra(MealDateContract.Columns.MEAL_TYPE, meal);
        newIntent.putExtra("Notification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Feed your pet")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("Large about of text would go here"))
                        .setContentText("Its time to feed your pet some " + meal);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
