package com.qian.quickshop.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qian.quickshop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LAUNCHER activity of the app
 */
public class MainPageActivity extends AppCompatActivity {
    @Bind(R.id.startButton)
    Button mStartButton;
    @Bind(R.id.scanButton)
    Button mScanButton;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        final TextView tutorialLink = (TextView) findViewById(R.id.tutorialLink);
        tutorialLink.setText(
                Html.fromHtml(
                        "<a href=\"https://youtu.be/2kqzgnwHSS8\">Master Quick Shop in 2 minutes</a> "));
        tutorialLink.setMovementMethod(LinkMovementMethod.getInstance());

        mScanButton.setTransformationMethod(null);
        mStartButton.setTransformationMethod(null);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startLoginActivity();
            }
        });

        final Activity activity = this;

        /**
         * Start scanning function with zxing library
         */
        mScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });


    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result != null) {
            if (result.getContents() == null) {
                Log.d("Main Activity Scan", "Cancel Scan ====================================");
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Main Activity Scan", "Scanned ====================================");
                Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();
                // start google search activity immediately
                searchOnInternet(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    /**
     * Search on Google to query for the scanned items
     * @param str barcode in digits
     */
    public void searchOnInternet(String str) {
        String query = str + " upc";
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query); // query contains search string
        startActivity(intent);
    }
}
