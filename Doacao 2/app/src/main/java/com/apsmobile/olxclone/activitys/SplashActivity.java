package com.apsmobile.olxclone.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.helper.SPFiltro;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::homeApp, 3000);

        SPFiltro.LimparFiltros(this);
    }

    private void homeApp(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}