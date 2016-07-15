package com.qian.quickshop.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import com.qian.quickshop.adapters.RouteAdapter;
import com.qian.quickshop.adapters.ShoppingListAdapter;
import com.qian.quickshop.data.BaseMap;
import com.qian.quickshop.data.BaseOnSale;
import com.qian.quickshop.data.Route;
import com.qian.quickshop.data.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RouteActivity extends AppCompatActivity {

    @Bind(android.R.id.list)
    ListView mListView;
    @Bind(android.R.id.empty)
    TextView mEmptyTextView;
    @Bind(R.id.finishButton)
    Button mFinishButton;

    @Bind(R.id.list2)
    ListView mOnSaleListView;

    private String shopNum;
    private Pair[] mPairs;
    private Pair[] onSale;

    private BaseMap mBaseMap;
    private BaseOnSale mBaseOnSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);

        mBaseMap = new BaseMap();
        mBaseOnSale = new BaseOnSale();

        Intent intent = getIntent();
        shopNum = intent.getStringExtra("shopNum");
        String[] confirmedList = intent.getStringArrayExtra("confirmedList");


        // get shopping list item pairs into the array list, where un-found items are on aisle(-1)
        mPairs = getBundles(shopNum, confirmedList);

        HashSet<Integer> set = new HashSet<>();
        for (Pair p : mPairs) {
            if (!set.contains(p.getAisleNum())) {
                set.add(p.getAisleNum());
            }
        }
        // get the on sale item pair
        onSale = getOnSale(shopNum, set);

        RouteAdapter adapter = new RouteAdapter(this, mPairs);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);

        RouteAdapter adapterOnSale = new RouteAdapter(this, onSale);
        mOnSaleListView.setAdapter(adapterOnSale);
        mOnSaleListView.setEmptyView(mEmptyTextView);

        mFinishButton.setTransformationMethod(null);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bFinish();
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


    private void writeInternalStorage(String shopNum) {
        String file = "shopMap" + shopNum;

        try {
            Context context = RouteActivity.this;
            FileOutputStream fOut = context.openFileOutput(file, Context.MODE_PRIVATE);
            HashMap<String, Integer> m = mBaseMap.getBaseMap(shopNum);
            String data = dictToString(m);

            fOut.write(data.getBytes());
            fOut.close();
            //Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("=====================Write Internal Storage====================");
            e.printStackTrace();
        }

    }

    private String dictToString(HashMap<String, Integer> m) {
        String r = "";
        for (String key : m.keySet()) {
            r = r + key + " " + m.get(key) + " ";
        }
        Log.e("String map:", r);
        return r;
    }

    private HashMap<String, Integer> stringToDict(String str) {
        String[] strs = str.split(" ");
        int len = strs.length / 2;
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            String key = strs[2 * i];
            Integer value = Integer.parseInt(strs[2 * i + 1]);
            map.put(key, value);
        }

        return map;
    }

    private Pair[] getBundles(String shopNum, String[] confirmedList) {
        // needs to instantiate the map
        // we either read the map from internal storage or receive it and write it to internal storage
        // So the class Route is really important

        // first check whether the shop map exists
        File filePath = new File("shopMap" + shopNum);
        if (!filePath.exists()) {
            //write into internal storage
            Log.e("shopMap" + shopNum, "Not exist and creating=======================");
            writeInternalStorage(shopNum);
        }
        // read a map string from internal storage
        String data = readInternalStorage(shopNum);

        HashMap<String, Integer> m = stringToDict(data);

        Route route = new Route();
        route.initShopMap(m);

        Pair[] res = route.getRoute(confirmedList);
        return res;
    }

    private Pair[] getOnSale(String shopNum, HashSet<Integer> set) {
        HashMap<String, Integer> mOnsale = mBaseOnSale.getBaseOnSaleMap(shopNum);
        Route route = new Route();
        route.initOnSaleMap(mOnsale);

        Pair[] r = route.getOnSaleRoute(set);

        return r;
    }

    private String readInternalStorage(String shopNum) {
        String file = "shopMap" + shopNum;
        String r = "";
        try {
            Context context = RouteActivity.this;
            FileInputStream fin = context.openFileInput(file);
            int c;
            String temp = "";

            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }

            //Toast.makeText(getBaseContext(), "file read", Toast.LENGTH_SHORT).show();
            Log.e("Read Internal ", temp);

            r = temp;
        } catch (Exception e) {
            System.out.println("=====================Read Internal Storage====================");
            e.printStackTrace();
        } finally {
            return r;
        }

    }

    private static File getFileDirectory(Context context) {
        return context.getFilesDir();
    }

    private void bFinish() {
        // when finish, go back to user area
        Intent intent = new Intent(this, UserAreaActivity.class);

        //Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }

}
