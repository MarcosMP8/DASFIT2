package com.example.dasfit.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.dasfit.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = context.getString(R.string.app_name);
        String body  = context.getString(R.string.notificacion_cuerpo);
        NotificationHelper.showNotification(context, title, body);
    }
}