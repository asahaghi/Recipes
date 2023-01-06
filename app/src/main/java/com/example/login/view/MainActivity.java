package com.example.login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.R;
import com.example.login.core.Constant;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvUser, tvEmail, fetchResult;
    SharedPreferences sharedPreferences;
    Button btnLogout, fetchProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        tvUser.setText(sharedPreferences.getString("name",""));
        tvEmail.setText(sharedPreferences.getString("email",""));

    }

    private void init() {
        sharedPreferences = getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        tvEmail = findViewById(R.id.tv_email);
        tvUser = findViewById(R.id.tv_user);
        btnLogout = findViewById(R.id.btn_logout);
        fetchProfile = findViewById(R.id.fetch_profile);
        fetchResult = findViewById(R.id.fetch_result);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });

        fetchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi1();
            }
        });


    }

    private void callApi() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.43.56/login-register/logout.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success")){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("logged", "");
                            editor.putString("name", "");
                            editor.putString("email", "");
                            editor.putString("apiKey", "");
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", sharedPreferences.getString("email",""));
                paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
    private void callApi1(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.43.56/login-register/profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        fetchResult.setText(response);
                        fetchResult.setVisibility(View.VISIBLE);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", sharedPreferences.getString("email",""));
                paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}