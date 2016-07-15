package com.qian.quickshop.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qian.quickshop.R;

public class UserAreaActivity extends AppCompatActivity {

    private Button mGetShopButton;
    private Button mLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomMessage);
        final TextView emailTextView = (TextView) findViewById(R.id.emailTextView);

        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        String name = shared.getString("name", "");
        String email = shared.getString("email", "");

        String message = "Hi, " + name +"!\n" + "Welcome to your user area.\n";
        welcomeMessage.setText(message);

        String emailMessage = "Your current email is: " + email;
        emailTextView.setText(emailMessage);


        mLogOutButton = (Button) findViewById(R.id.bLogOut);
        mLogOutButton.setTransformationMethod(null);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // remove saved
               removeFromSharedPreference("name", "email", "password", "lastShoppingList");

                Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mGetShopButton = (Button) findViewById(R.id.bGiantEagle);
        mGetShopButton.setTransformationMethod(null);

        // route to activity supported by google map api
        mGetShopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserAreaActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "You are now in user area.", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_scan) {
            // start the scanning function
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.setPrompt("Scan");
            intentIntegrator.setCameraId(0);
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setBarcodeImageEnabled(false);
            intentIntegrator.initiateScan();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Main Activity Scan", "Cancel Scan ====================================");
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            } else {
                Log.e("Main Activity Scan", "Scanned ====================================");
                Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();
                // start google search activity immediately
                searchOnInternet(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void searchOnInternet(String str) {
        String query = str + " upc";
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query); // query contains search string
        startActivity(intent);
    }

    // remove from shared preference
    public void removeFromSharedPreference(String key1, String key2, String key3, String key4) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(key1);
        editor.remove(key2);
        editor.remove(key3);
        editor.remove(key4);
        editor.commit();
    }
}
