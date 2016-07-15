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
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qian.quickshop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** allow user to create their shopping list
 *
 */
public class AddItemActivity extends AppCompatActivity {

    @Bind(R.id.itemEditText)
    EditText mItemsEditText;
    @Bind(R.id.addItemsButton)
    Button mAddItemsButton;
    @Bind(R.id.bUseLastShoppingList)
    Button mUseLastShoppingList;

    private EditText mEmailEditText;

    private String shopNum = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        mAddItemsButton.setTransformationMethod(null);
        mUseLastShoppingList.setTransformationMethod(null);

        Intent intent = getIntent();
        shopNum = intent.getStringExtra("shopNum");

        mUseLastShoppingList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String lastShoppingList = "";

                // set the shopping list to be the last one
                SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
                if (shared.contains("lastShoppingList")) {
                    lastShoppingList = shared.getString("lastShoppingList", "");
                }

                mItemsEditText.setText(lastShoppingList);

            }
        });


        mAddItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String items = mItemsEditText.getText().toString();
                startConfirmActivity(shopNum, items);
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


    private void startConfirmActivity(String shopNum, String items) {
        Intent intent = new Intent(this, ConfirmListActivity.class);
        intent.putExtra("shopNum", shopNum);
        intent.putExtra("shoppingList", items);
        startActivity(intent);
    }


}
