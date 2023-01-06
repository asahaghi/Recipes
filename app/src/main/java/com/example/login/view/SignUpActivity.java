package com.example.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText etEmail , etUser, etPassword , etConfirm ;
    Button btnSubmit;
    String name,email,password,confirm;
    TextView tvError;
    ProgressBar pbLoading;
    TextView tvLoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init(){
        etConfirm = findViewById(R.id.et_confirm);
        etEmail = findViewById(R.id.et_email);
        etUser = findViewById(R.id.et_user);
        etPassword = findViewById(R.id.et_password);
        btnSubmit = findViewById(R.id.btn_submit);
        tvError = findViewById(R.id.tv_error);
        pbLoading = findViewById(R.id.pb_loading);
        tvLoginNow = findViewById(R.id.tv_login_now);
        tvLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(intent);
               finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                name = String.valueOf(etUser.getText());
                email = String.valueOf(etEmail.getText());
                password = String.valueOf(etPassword.getText());
                if (isValidInput(name,email,password)){
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://192.168.43.56/login-register/register.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("AAA", "onResponse: got Response");
                                    pbLoading.setVisibility(View.GONE);
                                    if (response.equals("success")){
                                        Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        tvError.setText(response);
                                        tvError.setVisibility(View.VISIBLE);
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
                            paramV.put("name", name);
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

    private boolean isValidInput(String name1 ,String email1 , String password1){
        email1 = String.valueOf(etEmail.getText());
        name1 = String.valueOf(etUser.getText());
        password1 = String.valueOf(etPassword.getText());
        confirm = String.valueOf(etConfirm.getText());
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

        if (name1.length()<3){
            tvError.setText("username should be at least 3 characters");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            etUser.requestFocus();
            return false;
        }

        if (!(password1.equals(confirm))){
            tvError.setText("password doesn't match, please try again");
            tvError.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            etConfirm.requestFocus();
            return false;
        }

        return true;

    }
}
