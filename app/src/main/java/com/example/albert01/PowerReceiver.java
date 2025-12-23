package com.example.albert01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerReceiver extends BroadcastReceiver {

    private static PowerReceiver instance;

    public static PowerReceiver getInstance() {
        if (instance == null) {
            instance = new PowerReceiver();
        }
        return instance;
    }

    private PowerReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) return;

        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "מטען חובר", Toast.LENGTH_LONG).show();
        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "מטען נותק", Toast.LENGTH_LONG).show();
        }
    }
}