package com.example.albert01;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuActivity1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuActivity2) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.menuActivity3) {
            Intent intent = new Intent(this, Activity3.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }else if (id == R.id.menuActivity4) {
            Intent intent = new Intent(this, Activity4.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }else if (id == R.id.menuActivity5) {
            Intent intent = new Intent(this, Activity5.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }return false;
    }
}
