package kali.foodtrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static kali.foodtrad.R.string.s_dialog_msg_location_not_granted;

/**
 * Created by elliotching on 11-Apr-17.
 */

public class ActivityMaps extends MyCustomActivity {


    /* DO NOT CONTINUOUSLY MOVE CAMERA (SCREEN VIEW) TOWARDS THE USER LOCATION
    * AS THIS PAGE IS FOR SELLER TO SET LOCATION ONLY! */

    GoogleMap googleMap;
    Location lastLocation;
    Marker currLocationMarker;
    MapView mapView;
    FusedLocationTracker fusedLocationTracker;
    FusedLocationDataInterface fusedLocationDataInterface;

    Button buttonSaveLocation;
    CheckBox checkBoxAutoMyLoc;
    double[] markerLocation;

    private Listener listener;

    private boolean viewmaponly = false;
    private double[] viewingFoodlocation = null;

    private final static int mapZoomLevel = 18;

    boolean useMyCurrentLocation = true;

    Context context = this;
    AppCompatActivity activity = (AppCompatActivity) context;
    private static Dialog_Progress dialogProgress_loadLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMyView(R.layout.activity_maps, R.id.toolbar);

        changeMenu(false, false, false);

        listener = new Listener();
        checkBoxAutoMyLoc = (CheckBox) findViewById(R.id.checkbox_currentlocation);
        buttonSaveLocation = (Button) findViewById(R.id.button_save_location);

        buttonSaveLocation.setOnClickListener(listener);
        checkBoxAutoMyLoc.setOnCheckedChangeListener(listener);

        checkIsViewOnlyOrToChooseLocation(savedInstanceState);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReady());
        mapView.onResume();
    }

    private void checkIsViewOnlyOrToChooseLocation(Bundle savedInstanceState) {
        Intent i = getIntent();
        if(i != null){
            viewmaponly = i.getBooleanExtra(ResFR.BUNDLE_KEY_VIEW_MAP_ONLY, false);
            viewingFoodlocation = i.getDoubleArrayExtra(ResFR.BUNDLE_KEY_MAP_LOCATION);
            if(viewmaponly && viewingFoodlocation != null){
                buttonSaveLocation.setVisibility(View.GONE);
                checkBoxAutoMyLoc.setVisibility(View.GONE);
            }
        }
    }

    private void shiftMarkerToSpecificLocation(double[] location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location[0], location[1]);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        currLocationMarker = googleMap.addMarker(markerOptions);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoomLevel));
    }


    private class Listener implements FusedLocationDataInterface, GoogleMap.OnMapClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @Override
        public void getFusedLocationData(Location location) {
            lastLocation = location;

            Log.d("onLocationChanged", "onLocationChanged!!!");
            // on first launch
            if (currLocationMarker == null) {
                dialogProgress_loadLocation.dismiss();
                // first launch, move map camera / viewport to user location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoomLevel));
            }

            // if the checkbox is being checked...
            if(useMyCurrentLocation) {
                shiftMarkerToCurrentLocation();
            }
            // Options: auto put marker at my current location
            // Options: auto move screen to my current location
        }

        @Override
        public void onMapClick(LatLng latLng) {
            if(!useMyCurrentLocation) {
                shiftMarkerToTouchedLocation(latLng);
            }
        }

        @Override
        public void onClick(View v) {
            saveLocation();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            useMyCurrentLocation = isChecked;
            shiftMarkerToCurrentLocation();
        }
    }

    private class OnMapReady implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googlMap) {
            Log.d("onMapReady", "onMapReady.");
            googleMap = googlMap;
            // MapType: can be Satelite mode ETC
            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            //Initialize Google Play Services

            googleMap.setOnMapClickListener(listener);


            if(viewmaponly){
                shiftMarkerToSpecificLocation(viewingFoodlocation);

            }else {

                // if device OS SDK >= 23 (Marshmallow)
                if (Build.VERSION.SDK_INT >= 23) {
                    //IF Location Permission already granted
                    if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {

                        // check again for location v22
                        if (checkLocationPermission_v22()) {
                            fusedLocationDataInterface = listener;
                            fusedLocationTracker = new FusedLocationTracker(context, fusedLocationDataInterface);
                            dialogProgress_loadLocation = new Dialog_Progress(activity, R.string.s_prgdialog_title_location, R.string.s_prgdialog_getting_location, true);
                            // enable button + cursor of "MyLocation" on top right corner
                            googleMap.setMyLocationEnabled(true);
                        } else {
                            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            };
                            String StringWarningLocationNotGranted = ResFR.string(context, s_dialog_msg_location_not_granted);
                            new Dialog_AlertNotice(context, R.string.s_prgdialog_title_location, StringWarningLocationNotGranted).setPositiveKey(R.string.s_dialog_btn_ok, onClickListener);
                        }

                    } else {
                        // Request Location Permission
                        checkLocationPermission();
                    }
                }
                // else if device OS is version 5 Lollipop and below ( <= SDK_22 )
                else {
                    // check location here
                    if (checkLocationPermission_v22()) {
                        fusedLocationDataInterface = listener;
                        fusedLocationTracker = new FusedLocationTracker(context, fusedLocationDataInterface);
                        dialogProgress_loadLocation = new Dialog_Progress(activity, R.string.s_prgdialog_title_location, R.string.s_prgdialog_getting_location, true);
                        // enable button + cursor of "MyLocation" on top right corner
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        };
                        String StringWarningLocationNotGranted = ResFR.string(context, s_dialog_msg_location_not_granted);
                        new Dialog_AlertNotice(context, R.string.s_prgdialog_title_location, StringWarningLocationNotGranted).setPositiveKey(R.string.s_dialog_btn_ok, onClickListener);
                    }
                }
            }
        }
    }

    private void shiftMarkerToCurrentLocation() {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        currLocationMarker = googleMap.addMarker(markerOptions);
        markerLocation = new double[]{
                lastLocation.getLatitude(),
                lastLocation.getLongitude()
        };
    }

    private void shiftMarkerToTouchedLocation(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions().position(latLng);

        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        currLocationMarker = googleMap.addMarker(marker);
        markerLocation = new double[]{
                latLng.latitude,
                latLng.longitude
        };
    }


    private void saveLocation() {
        Intent i = new Intent();
        i.putExtra(ResFR.INTENT_ACTIVITY_RESULT_PUT_EXTRA_LOCATION_KEY, markerLocation);
        setResult(RESULT_OK, i);
        backButtonPressed();
    }

    @Override
    void backButtonPressed() {
//        super.onPause();
        mapView.onPause();
        if(viewmaponly){

        }else {
            fusedLocationTracker.stopLocationUpdates();
        }
        super.backButtonPressed();
    }
}