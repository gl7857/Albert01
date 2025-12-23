package com.example.albert01;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Activity6 extends MasterActivity {

    private static boolean isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
    }

    public void startListening(View view) {
        if (!isRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_POWER_CONNECTED);
            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

            getApplicationContext().registerReceiver(PowerReceiver.getInstance(), filter);

            isRegistered = true;
            Toast.makeText(this, "האזנה לטעינה הופעלה", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "ההאזנה כבר פועלת", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopListening(View view) {
        if (isRegistered) {
            try {
                getApplicationContext().unregisterReceiver(PowerReceiver.getInstance());
                isRegistered = false;
                Toast.makeText(this, "האזנה לטעינה הופסקה", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
            }
        } else {
            Toast.makeText(this, "ההאזנה כבויה כרגע", Toast.LENGTH_SHORT).show();
        }
    }
}