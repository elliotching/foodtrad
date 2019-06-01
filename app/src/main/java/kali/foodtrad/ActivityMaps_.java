package kali.foodtrad;//package kali.foodtrad;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//import static android.content.pm.PackageManager.PERMISSION_GRANTED;
//import static android.location.LocationManager.GPS_PROVIDER;
//import static android.location.LocationManager.NETWORK_PROVIDER;
//import static kali.foodtrad.R.string.s_dialog_msg_location_not_granted;
//
///**
// * Created by elliotching on 11-Apr-17.
// */
//
//// sample get from the stackoverflow , targeting API-23 and above.
//public class ActivityMaps_ extends MyCustomActivity {
//
//
//    /* DO NOT CONTINUOUSLY MOVE CAMERA (SCREEN VIEW) TOWARDS THE USER LOCATION
//    * AS THIS PAGE IS FOR SELLER TO SET LOCATION ONLY! */
//
//
//    GoogleMap mGoogleMap;
//    MapView mapView;
//    LocationRequest mLocationRequest;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    Marker mCurrLocationMarker;
//    MapLocationListener mapLocationListener;
//    LocationManager locManager;
//
//    Button buttonSaveLocation;
//
//    double[] markerLocation;
//
//    private final static int mapZoomLevel = 18;
//
//    private class GoogleApiClientConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//        @Override
//        public void onConnected(Bundle bundle) {
//
//            Log.d("onConnected", "onConnected.");
//            mLocationRequest = new LocationRequest();
//            mLocationRequest.setInterval(1000);
//            mLocationRequest.setFastestInterval(50);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mapLocationListener);
//            }
//
//        }
//
//        @Override
//        public void onConnectionSuspended(int i) {
//        }
//
//        @Override
//        public void onConnectionFailed(ConnectionResult connectionResult) {
//            new Dialog_AlertNotice(context, R.string.s_dialog_title_error, R.string.s_dialog_msg_mapconnectionfail)
//                    .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            activity.finish();
//                        }
//                    });
//        }
//    }
//
//
//    Context context = this;
//    AppCompatActivity activity = (AppCompatActivity) context;
//
//    private static Dialog_Progress dialogProgress_loadLocation;
//
//    private class MapLocationListener implements com.google.android.gms.location.LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//            mLastLocation = location;
//
//            Log.d("onLocationChanged", "onLocationChanged!!!");
//            // on first launch
//            if (mCurrLocationMarker == null) {
//                dialogProgress_loadLocation.dismiss();
//                // first launch, move map camera / viewport to user location
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoomLevel));
//            }
//
//            shiftMarkerToCurrentLocation();
//
//            // Options: auto put marker at my current location
//            // Options: auto move screen to my current location
//        }
//
//    }
//
//    private class OnMapReady implements OnMapReadyCallback {
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            Log.d("onMapReady", "onMapReady.");
//            mGoogleMap = googleMap;
//            // MapType: can be Satelite mode ETC
//            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//            //Initialize Google Play Services
//
//            mGoogleMap.setOnMapClickListener(new OnMapTouched());
//
//
//            //if device OS SDK >= 23 (Marshmallow)
//            if (Build.VERSION.SDK_INT >= 23) {
//                //IF Location Permission already granted
//                if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
//
//
//                    buildGoogleApiClient();
//                    // enable button + cursor of "MyLocation" on top right corner
//                    mGoogleMap.setMyLocationEnabled(true);
//
//
//                } else {
//
//
//                    // Request Location Permission
//                    checkLocationPermission();
//
//
//                }
//            }
//
//            //if device OS is version 5 Lollipop and below ( <= SDK_22 )
//            else {
//                // check location here
//                if (checkLocationPermission_v22()) {
//
//
//                    // Location Permission already granted
//                    buildGoogleApiClient();
//                    // enable button + cursor of "MyLocation" on top right corner
//                    mGoogleMap.setMyLocationEnabled(true);
//
//
//                } else {
//
//
//                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            activity.finish();
//                        }
//                    };
//                    String StringWarningLocationNotGranted = ResFR.string(context, s_dialog_msg_location_not_granted);
//                    new Dialog_AlertNotice(context, "Location", StringWarningLocationNotGranted).setPositiveKey("OK", onClickListener);
//
//
//                }
//            }
//        }
//    }
//
//    private class OnMapTouched implements GoogleMap.OnMapClickListener {
//
//        @Override
//        public void onMapClick(LatLng latLng) {
//            shiftMarkerToTouchedLocation(latLng);
//        }
//    }
//
//    private void shiftMarkerToCurrentLocation() {
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
//        //Place current location marker
//        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
//        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
//        markerLocation = new double[]{
//                mLastLocation.getLatitude(),
//                mLastLocation.getLongitude()
//        };
//    }
//
//    private void shiftMarkerToTouchedLocation(LatLng latLng) {
//        MarkerOptions marker = new MarkerOptions().position(latLng);
//
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
//
//        mCurrLocationMarker = mGoogleMap.addMarker(marker);
//        markerLocation = new double[]{
//                latLng.latitude,
//                latLng.longitude
//        };
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        createMyView(R.layout.activity_maps, R.id.toolbar);
//
//        changeMenu(false, false, false);
//
//        mapLocationListener = new MapLocationListener();
//
//        buttonSaveLocation = (Button) findViewById(R.id.button_save_location);
//        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveLocation();
//            }
//        });
//
//        locManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//
//        mapView.getMapAsync(new OnMapReady());
//
//        mapView.onResume();
//
//
//    }
//
//    private void saveLocation() {
//        Intent i = new Intent();
//        i.putExtra("savedlocation", markerLocation);
//        setResult(RESULT_OK, i);
//        this.onPause();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //stop location updates when Activity is no longer active
//        if (mGoogleApiClient != null && mapLocationListener != null) {
//            Log.d("FusedLocationApi", "run!");
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mapLocationListener);
//
//        }
//
//        activity.finish();
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//
//        Log.d("buildGoogleApiClient", "buildGoogleApiClient.");
//        GoogleApiClientConnection g = new GoogleApiClientConnection();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(g)
//                .addOnConnectionFailedListener(g)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//
//        dialogProgress_loadLocation = new Dialog_Progress(activity, "Location", "Getting user's location...", true);
//    }
//
//    private boolean checkLocationPermission_v22() {
//        if (locManager.isProviderEnabled(GPS_PROVIDER) ||
//                locManager.isProviderEnabled(NETWORK_PROVIDER)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static final int FR_PERMISSIONS_REQUEST_CODE_LOCATION = 99;
//
//    private void checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, FR_PERMISSIONS_REQUEST_CODE_LOCATION);
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case FR_PERMISSIONS_REQUEST_CODE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mGoogleMap.setMyLocationEnabled(true);
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            activity.finish();
//                        }
//                    };
//                    new Dialog_AlertNotice(context, "Denied", "Permission denied. Exiting map...").setPositiveKey("OK", onClickListener);
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (Build.VERSION.SDK_INT > 5
//                && keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//
//            this.onPause();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            this.onPause();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}
//
///*
//
//XML ...........................
//* <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    android:orientation="vertical" android:layout_width="match_parent"
//    android:layout_height="match_parent">
//
//    <fragment xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:tools="http://schemas.android.com/tools"
//        xmlns:map="http://schemas.android.com/apk/res-auto"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:id="@+id/map"
//        tools:context=".MapLocationActivity"
//        android:name="com.google.android.gms.maps.SupportMapFragment"/>
//
//</LinearLayout>
//*
//* */
//
//
