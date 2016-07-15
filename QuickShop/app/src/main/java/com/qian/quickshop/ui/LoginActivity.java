package com.qian.quickshop.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.qian.quickshop.R;
import com.qian.quickshop.requests.*;

import org.json.JSONObject;

/**
 * Login page of the activity
 * If user has already logged in before, redirect him to user area activity
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // read from shared preference

        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        if (shared.contains("email") && shared.contains("password")) {
            //user has already logged in before, redirect to user area activity
            String email = shared.getString("email", "");
            String name = shared.getString("name", "");

            Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            startActivity(intent);

        } else {

            setContentView(R.layout.activity_login);

            etEmail = (EditText) findViewById(R.id.etEmail);
            etPassword = (EditText) findViewById(R.id.etPassword);

            bLogin = (Button) findViewById(R.id.bLogin);
            bLogin.setTransformationMethod(null);

            final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

            registerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            });

            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();

                    /**
                     * Query backend database to check whether the user credential is valid
                     */
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Log.d("Login Response", response);

                                if (success) {
                                    String name = jsonResponse.getString("name");
                                    saveInformation(name, email, password);

                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);

                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);

                                    startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed. The email or password you entered is wrong.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    // put the login request into a queue
                    LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);

                }

            });
        }
    }


    /**
     * Save user inform into shared preference
     */
    public void saveInformation(String name, String email, String password) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
    }
}


