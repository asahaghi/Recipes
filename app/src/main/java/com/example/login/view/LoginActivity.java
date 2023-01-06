package com.example.login.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.R;
import com.example.login.core.Constant;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    TextView tvSignUpNow;
    TextInputEditText etEmail , etPassword ;
    Button btnSubmit;
    String name,email,password , apiKey;
    TextView tvError;
    ProgressBar pbLoading;
    SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initviews();

    }



    private void initviews() {
       sharedPreferences = getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
       if (sharedPreferences.getString("logged","false").equals("true")){
           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
           startActivity(intent);
           finish();
       }
       tvError = findViewById(R.id.tv_error);
       pbLoading = findViewById(R.id.pb_loading);
       tvSignUpNow = findViewById(R.id.tv_sign_up_now);
       tvSignUpNow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
               startActivity(intent);
               finish();
           }
       });
       etEmail = findViewById(R.id.et_email);
       etPassword = findViewById(R.id.et_password);
       btnSubmit = findViewById(R.id.btn_submit);
       btnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               tvError.setVisibility(View.GONE);
               pbLoading.setVisibility(View.VISIBLE);
               email = String.valueOf(etEmail.getText());
               password = String.valueOf(etPassword.getText());
               if (isValidInput(email,password)){
                   RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                   String url ="http://192.168.43.56/login-register/login.php";

                   StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                           new Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {
                                   Log.d("AAA", "onResponse: got Response");
                                   pbLoading.setVisibility(View.GONE);
                                   try {
                                       JSONObject jsonObject = new JSONObject(response);
                                       String message = jsonObject.getString("message");
                                       String status = jsonObject.getString("status");
                                       if (status.equals("successful")){
                                           name = jsonObject.getString("name");
                                           email = jsonObject.getString("email");
                                           apiKey = jsonObject.getString("apiKey");
                                           SharedPreferences.Editor editor = sharedPreferences.edit();
                                           editor.putString("logged","true");
                                           editor.putString("name",name);
                                           editor.putString("email",email);
                                           editor.putString("apiKey",apiKey);
                                           editor.apply();
                                           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                           startActivity(intent);
                                           finish();

                                       }else {
                                           tvError.setText(response);
                                           tvError.setVisibility(View.VISIBLE);
                                       }
                                   } catch (JSONException e) {
                                       e.printStackTrace();

                                   }
                               }
                           }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           pbLoading.setVisibility(View.GONE);
                           tvError.setText(error.getLocalizedMessage());
                           tvError.setVisibility(View.VISIBLE);
                       }
                   }){
                       protected Map<String, String> getParams(){
                           Map<String, String> paramV = new HashMap<>();
                           paramV.put("email", email);
                           paramV.put("password", password);
                           return paramV;
                       }
                   };
                   queue.add(stringRequest);
               }

           }
       });

    }

    private boolean isValidInput(String email1 , String password1){
        email1 = String.valueOf(etEmail.getText());
        password1 = String.valueOf(etPassword.getText());
        if(email1.lastIndexOf("@")<=0 || !email1.contains(".")
        || email1.lastIndexOf(".") < email1.lastIndexOf("@")
        || email1.split("@").length < 2){
            tvError.setText("Wrong Email Address");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            etEmail.requestFocus();
            return false;
        }
        if(password1.length()<6){
            tvError.setText("password should be at least 6 characters");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            etPassword.requestFocus();
            return false;
        }

        return true;

    }
}
