package com.qian.quickshop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.qian.quickshop.R;

import com.qian.quickshop.requests.*;

import org.json.JSONObject;


/**
 * Create a register request based on user info: name, email and password
 */
public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final CheckBox cbAgreePolicy = (CheckBox) findViewById(R.id.agreePolicyCheckBox);

        // Security and privacy policy is hosted on aws server
        final TextView tvPolicyLink = (TextView) findViewById(R.id.policyLinkTextView);
        tvPolicyLink.setText(
                Html.fromHtml(
                "<a href=\"https://www.google.com/\">Security and Privacy Policy</a> "));
        tvPolicyLink.setMovementMethod(LinkMovementMethod.getInstance());

        final Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setTransformationMethod(null);

        bRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!cbAgreePolicy.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("To register, you MUST agree to our Security and Privacy Policy.")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else {
                    final String name = etName.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();



                    // send a register request and check the response
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Log.e("Register", success + "");

                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register Succeeded.")
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent  = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                                    })
                                            .create()
                                            .show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register Failed.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    // put the register request into a queue
                    RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }

        });

    }
}
