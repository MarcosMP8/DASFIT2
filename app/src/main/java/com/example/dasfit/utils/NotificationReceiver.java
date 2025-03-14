package com.example.dasfit.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.showNotification(context, "DASFIT", "¡Hora de entrenar! Revisa tu rutina.");
    }
}
