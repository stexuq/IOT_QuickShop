package googleplayservices.samples.android.treehouse.com.iamhere;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final String TAG = MapsActivity.class.getSimpleName();
    public static final Integer numberOfList = 5;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    HashMap<LatLng, String> store = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        loadStore();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Log.d(TAG, "Happy");
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //Log.d(TAG, "Here");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        Log.d(TAG, "onConnected");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d(TAG, "onLocationChanged");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //Log.d(TAG, "latitude" +String.valueOf(latLng.latitude));
        //Log.d("location:", String.format("%s , %s", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude)));

        createMarker(latLng, "Current Location");

        //double lat = latLng.latitude;
        Log.d(TAG, "lat-" + String.valueOf(latLng.latitude) + " long-" + String.valueOf(latLng.longitude));

        // Search store
        search(latLng);

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng clickStore = marker.getPosition();
                Log.d(TAG, "click store: lat-" + clickStore.latitude + "  long-" + clickStore.longitude);
                if(store.containsKey(clickStore)) {
                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                    intent.putExtra("store", store.get(clickStore));
                    startActivity(intent);
                }
            }
        });

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

//    public void startDailyActivity(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
    private void loadStore(){
       // giant eagle
        Log.d(TAG, "load store");
        store.put(new LatLng(40.452869,-79.95028)   , new String("0"));
        store.put(new LatLng(40.4566574,-79.9344419), new String("1"));
        store.put(new LatLng(40.4314306,-80.0475339), new String("2"));
        store.put(new LatLng(40.4316497,-80.0478775), new String("3"));
        store.put(new LatLng(40.4318687,-80.048221) , new String("4"));
        store.put(new LatLng(40.4320878,-80.0485646), new String("5"));
        store.put(new LatLng(40.4323069,-80.0489081), new String("6"));
        store.put(new LatLng(40.432526,-80.0492516) , new String("7"));
        store.put(new LatLng(40.432745,-80.0495952) , new String("8"));
        store.put(new LatLng(40.4329641,-80.0499387), new String("9"));


        // target
        store.put(new LatLng(40.4585616,-79.9992175), new String("10"));
        store.put(new LatLng(40.4589995,-79.9999045), new String("11"));
        store.put(new LatLng(40.4592185,-80.0002481), new String("12"));
        store.put(new LatLng(40.5222498,-80.1100696), new String("13"));
        store.put(new LatLng(40.343726,-80.1271059) , new String("14"));
        store.put(new LatLng(40.5356271,-80.1384092), new String("15"));
        store.put(new LatLng(40.3481438,-80.0239882), new String("16"));
        store.put(new LatLng(40.4463073,-80.2540377), new String("17"));
        store.put(new LatLng(40.6417942,-80.0118537), new String("18"));

        // whole food
        store.put(new LatLng(40.4611891,-80.0033399), new String("20"));
        store.put(new LatLng(40.6123154,-80.122733) , new String("21"));

    }
    private void search(LatLng latLng) {
        Log.d(TAG, "search");
        double anchorLatitude = latLng.latitude;
        double anchorLongtitude = latLng.longitude;

        TreeMap<Double, LatLng> dist = new TreeMap<>();

        for (LatLng key : store.keySet()) {
            double storeLatitude = key.latitude;
            double storeLongitude = key.longitude;
            double distKey = Math.pow((storeLatitude - anchorLatitude), 2)
                    + Math.pow((storeLongitude - anchorLongtitude), 2);
            dist.put(distKey, key);
            //createMarker(key, "store " + String.valueOf(store.get(key)));
        }
        // Add marker
        int i = 0;

        for(Map.Entry<Double, LatLng> entry : dist.entrySet()) {
           if(i < numberOfList){
               Double distKey = entry.getKey();
               System.out.println("dist: " + distKey);
               LatLng key = entry.getValue();
               String targetStore = store.get(key);
               createMarker(key, "store " + targetStore);
               i++;

           } else{
             break;
           }
        }

//        Collection c = dist.values();
//        Iterator itr = c.iterator();
//
//        while(itr.hasNext() && i > 0){
//
//            LatLng key = (LatLng)itr.next();//dist.get(distKey);
//            //System.out.println("longitude: " + );
//            String targetStore = store.get(key);
//            createMarker(key, "store " + targetStore);
//            i--;
//        }
    }

    private void createMarker(LatLng latLng, String str){
        Log.d(TAG, "createMarker");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(str);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    // Permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}


