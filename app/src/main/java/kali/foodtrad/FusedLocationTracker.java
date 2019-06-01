package kali.foodtrad;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

class FusedLocationTracker implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    Context context;

    GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;
    LocationRequest mLocationRequest;
    Location mCurrentLocation = null;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    private String TAG = "LocationTracker";
    private FusedLocationDataInterface fusedLocationDataInterface;

    FusedLocationTracker(Context context, FusedLocationDataInterface f){
        this.context = context;
        this.fusedLocationDataInterface = f;
//        wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
//        wifiStatus = wifiManager.isWifiEnabled();
//        if (!wifiStatus){
//            wifiManager.setWifiEnabled(true);
//            wifiManager.startScan();
//        }
        createLocationRequest();
        buildGoogleApiClient();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Log.d(TAG, "createLocationRequest");
    }

    synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        startLocationUpdates();
    }

    PendingResult<Status> pendingResult;

    private void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates");
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ((AppCompatActivity)context).finish();
        }
        pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        if (location != null) {
            fusedLocationDataInterface.getFusedLocationData(location);
        }
    }

    void stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates");
        if (mGoogleApiClient.isConnected()) {
            pendingResult = LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        }
    }
}