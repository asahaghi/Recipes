package com.example.login.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ProfileFragment extends Fragment {
    private View rootView;
    TextView tvUser, tvEmail, fetchResult;
    SharedPreferences sharedPreferences;
    Button btnLogout, fetchProfile, btnMenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, null, false);
        init();
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

        }


        tvUser.setText(sharedPreferences.getString("name",""));
        tvEmail.setText(sharedPreferences.getString("email",""));
        return rootView;
    }

    private void init(){
        sharedPreferences = getContext().getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        tvEmail = rootView.findViewById(R.id.tv_email);
        tvUser = rootView.findViewById(R.id.tv_user);
        btnMenu = rootView.findViewById(R.id.btn_menu);
        btnLogout = rootView.findViewById(R.id.btn_logout);
        fetchProfile = rootView.findViewById(R.id.fetch_profile);
        fetchResult = rootView.findViewById(R.id.fetch_result);
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
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)requireActivity()).addFragment(new MenuFragment());
            }
        });
    }

    private void callApi() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                        }else
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
