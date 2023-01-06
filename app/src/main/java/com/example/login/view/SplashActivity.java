package com.example.login.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.login.R;
import com.example.login.core.Constant;

public class SplashActivity extends Activity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("logged","false").equals("true")){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

                finish();

                // FIXME: 6/6/2022
            }
        }, 1500);


    }
}
