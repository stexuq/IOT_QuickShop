package com.qian.quickshop.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qian.quickshop.R;
import com.qian.quickshop.adapters.ShoppingListAdapter;
import com.qian.quickshop.data.ShoppingList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConfirmListActivity extends AppCompatActivity {
    @Bind(android.R.id.list)
    ListView mListView;
    @Bind(android.R.id.empty)
    TextView mEmptyTextView;
    private String shopNum;
    private String dataPassed;
    private String[] shoppingList;

    @Bind(R.id.routeButton)
    Button mRouteButton;
    @Bind(R.id.goBackButton)
    Button mGoBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_list);
        ButterKnife.bind(this);

        mRouteButton.setTransformationMethod(null);
        mGoBackButton.setTransformationMethod(null);

        Intent intent = getIntent();
        shopNum = intent.getStringExtra("shopNum");
        dataPassed = intent.getStringExtra("shoppingList");

        shoppingList = dataPassed.split("\n");

        ShoppingListAdapter adapter = new ShoppingListAdapter(this, shoppingList);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);

        mRouteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRouteActivity(shoppingList);
            }
        });

        mGoBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
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
            Intent intent = new Intent(this, UserAreaActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_scan) {
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


    private void startRouteActivity(String[] items) {
        // check if there is a shoppinglist
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        if (shared.contains("lastShoppingList")) {
            removeFromSharedPreference("lastShoppingList");
        }
        // store last shopping list into the shared preference

        saveInformation(dataPassed);
        // And we start a new activity
        Intent intent = new Intent(this, RouteActivity.class);

        intent.putExtra("shopNum", shopNum);
        intent.putExtra("confirmedList", items);
        startActivity(intent);
    }

    public void saveInformation(String shoppingList) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString("lastShoppingList", shoppingList);
        editor.commit();
    }

    public void removeFromSharedPreference(String key1) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(key1);
        editor.commit();
    }


}
