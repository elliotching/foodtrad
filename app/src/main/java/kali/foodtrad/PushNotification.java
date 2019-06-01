package kali.foodtrad;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.IntentCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by elliotching on 13-Apr-17.
 */

class PushNotification {

    Context context;

    PushNotification(Context c){
        context = c;
    }

    void createNotification( String messageBody ) {

        newMsgIntoDB(messageBody);

        Intent intent = new Intent( context , ActivityNotification. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_notification)
                .setContentTitle("FoodieRoute")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setNumber(getNotiCount())
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }

    void createNotification( String messageBody , boolean logged_out) {

        newMsgIntoDB(messageBody);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ResFR.BUNDLE_KEY_KICKED_OUT, logged_out);
        Intent ii = new Intent(context, ActivityLogIn.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        ii.putExtras(bundle);

        PendingIntent resultIntent = PendingIntent.getActivity( context , 0, ii,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_notification)
                .setContentTitle("FoodieRoute")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setNumber(getNotiCount())
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }

    private int getNotiCount(){
        SharedPreferences pref = context.getSharedPreferences("FCM", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        int count = pref.getInt("notification_number",1);
        int nextComingMsgCount = count+1;
        edit.putInt("notification_number", nextComingMsgCount);
        edit.commit();
        return count;
    }

    private void newMsgIntoDB(String body){

        MyDBHandler dbHandler = new MyDBHandler(context);

        NotiMsg notiMsg;
        notiMsg = new NotiMsg(getTimeDate(), body);
        dbHandler.addProduct(notiMsg);
    }

    private String getTimeDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
