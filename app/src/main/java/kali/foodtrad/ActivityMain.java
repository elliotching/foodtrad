package kali.foodtrad;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elliotching on 08-May-17.
 */

public class ActivityMain extends MyCustomActivity {

    Button buttonsellerupdatelocation;
    Button buttonselleraddfood;
    Button buttonusersharefood;
    Button buttonsellerviewmyfoodmenu;
    Button buttonallviewlistfood;
    Button buttonbroadcast;
    Button buttonwipelocation;
    RadioButton radioQuick;
    RadioButton radioPrecise;
    LinearLayout layoutSellerUpdateLocation;

    TextView textLocationInfo;
    ImageView image;
    ImageView image2;
    ImageView image3;
    boolean[] isRadioQuickOrPreciseChecked = new boolean[]{false,false}; // 0 = quick, 1 = precise

    private static final int REQUEST_CODE_OPEN_MAP_SET_LOCATION = 213;

    private Listener listener;
    private FusedLocationTracker locationTracker;
    private CustomHTTP httpUpdateSellerLocation;
    private String sellerType;
    private double[] mycurrentlocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_main, R.id.toolbar);
        changeMenu(true, true, true, true, false);

        ResFR.checkFirebaseTokenIfNoThenMakeToast(context);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        buttonselleraddfood = (Button) findViewById(R.id.button_addfood);
        buttonsellerupdatelocation = (Button) findViewById(R.id.button_updatelocation);
        buttonusersharefood = (Button) findViewById(R.id.button_sharefooduser);
        buttonsellerviewmyfoodmenu = (Button) findViewById(R.id.button_viewmyfoodmenu);
        buttonallviewlistfood = (Button) findViewById(R.id.button_viewfoodlist);
        buttonbroadcast = (Button) findViewById(R.id.button_broadcast);
        buttonwipelocation = (Button) findViewById(R.id.button_wipeloc);
        textLocationInfo = (TextView) findViewById(R.id.text_showlocation);
        layoutSellerUpdateLocation = (LinearLayout) findViewById(R.id.layout_seller_update_location);
        radioQuick = (RadioButton) findViewById(R.id.radio_quick);
        radioPrecise = (RadioButton) findViewById(R.id.radio_precise);
        image = (ImageView) findViewById(R.id.imageview_activitymain);
        image2 = (ImageView) findViewById(R.id.imageviewbottom_activitymain);
        image3 = (ImageView) findViewById(R.id.imagesarawakyes_activitymain);

        updateRadioButtonFromPref(radioQuick, radioPrecise);

        listener = new Listener();
        buttonsellerupdatelocation.setOnClickListener(listener);
        buttonselleraddfood.setOnClickListener(listener);
        buttonusersharefood.setOnClickListener(listener);
        buttonsellerviewmyfoodmenu.setOnClickListener(listener);
        buttonallviewlistfood.setOnClickListener(listener);
        buttonbroadcast.setOnClickListener(listener);
        buttonwipelocation.setOnClickListener(listener);
        radioQuick.setOnCheckedChangeListener(listener);
        radioPrecise.setOnCheckedChangeListener(listener);

        adjustImageSize(image, 1, 1, 0, 40.0f);
        adjustImageSize(image2, 1, 1, 0, 40.0f);
        adjustImageSize(image3, 3, 1, 0, 100.0f);

        /* get my current location */
        mycurrentlocation = ResFR.getPrefLocation(context);
        sellerType = ResFR.getPrefString(context, ResFR.IS_SELLER);
        // check is my current location is empty
        if (sellerType.equals("1")) {
            hideButtonsForSeller();
        } else if (sellerType.equals("2")) {
            hideButtonsForMobile();
            if (isLocationEmpty(mycurrentlocation)) {
                showDialogYourLocationIsEmpty();
            }
        } else if (sellerType.equals("0")) {
            buttonsellerviewmyfoodmenu.setText(R.string.s_button_viewfoodsishared);
            hideButtonsForUser();
        }
    }

    private void showDialogYourLocationIsEmpty() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_yoursellerlocationempty).setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    private void hideButtonsForUser() {
        buttonselleraddfood.setVisibility(View.GONE);
        layoutSellerUpdateLocation.setVisibility(View.GONE);
        buttonbroadcast.setVisibility(View.GONE);
        buttonwipelocation.setVisibility(View.GONE);
    }

    private void hideButtonsForSeller() {
        buttonusersharefood.setVisibility(View.GONE);
        buttonsellerupdatelocation.setVisibility(View.GONE);
        buttonwipelocation.setVisibility(View.GONE);
    }

    private void hideButtonsForMobile() {
        buttonusersharefood.setVisibility(View.GONE);
    }

    private void startUpdateLocation() {
        if(isRadioQuickOrPreciseChecked[0] /* direct update */ ) {
            locationTracker = new FusedLocationTracker(context, listener);
            textLocationInfo.setText(R.string.s_text_gettingyourlocation);
        }else if(isRadioQuickOrPreciseChecked[1] /* open map view */ ){
            Intent i = new Intent(context, ActivityMaps.class);
            startActivityForResult(i, REQUEST_CODE_OPEN_MAP_SET_LOCATION);
        }else{

        }
    }

    private void onLocationReceivedDoHttpSave(Location location) {
        locationTracker.stopLocationUpdates();
        String lat = stringOf(location.getLatitude());
        String lng = stringOf(location.getLongitude());
        String savingyourlocation = ResFR.string(context, R.string.s_text_savingyourlocation);
        textLocationInfo.setText(savingyourlocation + " " + lat + " , " + lng);

        String username = ResFR.getPrefString(context, ResFR.USERNAME);

        String[][] data = new String[][]{
                {"act", "updselloc"},
                {"user", username},
                {"mode", "mobile"},
                {"slloclat", lat},
                {"slloclng", lng}
        };

        httpUpdateSellerLocation = new CustomHTTP(context, data, ResFR.URL);
        httpUpdateSellerLocation.ui = listener;
        httpUpdateSellerLocation.execute();
    }

    private void onLocationReceivedDoHttpSave(double[] location) {
        String lat = stringOf(location[0]);
        String lng = stringOf(location[1]);
        String savingyourlocation = ResFR.string(context, R.string.s_text_savingyourlocation);
        textLocationInfo.setText(savingyourlocation + " " + lat + " , " + lng);

        String username = ResFR.getPrefString(context, ResFR.USERNAME);
    
        String[][] data = new String[][]{
                {"act", "updselloc"},
                {"user", username},
                {"mode", "mobile"},
                {"slloclat", lat},
                {"slloclng", lng}
        };

        httpUpdateSellerLocation = new CustomHTTP(context, data, ResFR.URL);
        httpUpdateSellerLocation.ui = listener;
        httpUpdateSellerLocation.execute();
    }

    private void deleteLocation(){
        String username = ResFR.getPrefString(context, ResFR.USERNAME);
    
        String[][] data = new String[][]{
                {"act", "delselloc"},
                {"user", username},
                {"mode", "mobile"},
                {"slloclat", ""},
                {"slloclng", ""}
        };

        httpUpdateSellerLocation = new CustomHTTP(context, data, ResFR.URL);
        httpUpdateSellerLocation.ui = listener;
        httpUpdateSellerLocation.execute();
    }

    private void onHttpUpdateLocationResult(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String success = json.optString("success");
            String latS = json.optString("lat");
            String lngS = json.optString("lng");
            if (success.equals("1")) {
                if(latS.equals("") || lngS.equals("")){
                    textLocationInfo.setText(R.string.s_deletelocationsuccess);
                    double emptyLocation = ResFR.DEFAULT_EMPTY_LOCATION;
                    ResFR.setPrefLocation(context, new double[]{emptyLocation, emptyLocation});
                }else {
                    textLocationInfo.setText(R.string.s_dialog_title_success);
                    double lat = doubleOf(latS);
                    double lng = doubleOf(lngS);
                    ResFR.setPrefLocation(context, new double[]{lat, lng});
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showDialogPhpError(result);
        }
    }

    private void checkLocationAndAddFood() {

        if (sellerType.equals("1")) {
            if (isLocationEmpty(mycurrentlocation)) {
                showDialogYourLocationIsEmpty();
            }
        } else if (sellerType.equals("2")) {
            if (isLocationEmpty(mycurrentlocation)) {
                showDialogYourLocationIsEmpty();
            }
        }
        Intent i = new Intent(context, ActivityAddFood.class);
        startActivity(i);
    }


    private void postShareFood() {
        Intent i = new Intent(context, ActivityAddFood.class);
        startActivity(i);
    }

    private void viewAllFood() {
        Intent i = new Intent(context, ActivityFoodListingListViewElliot.class);
        startActivity(i);
    }

    private void viewMyFood() {
        Intent i = new Intent(context, ActivityFoodMenu.class);
        startActivity(i);
    }

    private void gotoBroadcastPage() {
        Intent i = new Intent(context, ActivityMsgBroadcast.class);
        startActivity(i);
    }

    private class Listener implements View.OnClickListener, FusedLocationDataInterface, InterfaceCustomHTTP, CompoundButton.OnCheckedChangeListener {

        @Override
        public void onClick(View v) {
            if (v == buttonselleraddfood) {
                checkLocationAndAddFood();
            }
            if (v == buttonsellerupdatelocation) {
                startUpdateLocation();
            }
            if (v == buttonusersharefood) {
                postShareFood();
            }
            if (v == buttonsellerviewmyfoodmenu) {
                viewMyFood();
            }
            if (v == buttonallviewlistfood) {
                viewAllFood();
            }
            if (v == buttonbroadcast) {
                gotoBroadcastPage();
            }
            if( v == buttonwipelocation){
                deleteLocation();
            }
//            Button buttonsellerupdatelocation;
//            Button buttonselleraddfood;
//            Button buttonusersharefood;
//            Button buttonsellerviewmyfoodmenu;
//            Button buttonallviewlistfood;
//            Button buttonsellersharefood;
        }

        @Override
        public void getFusedLocationData(Location location) {
            onLocationReceivedDoHttpSave(location);
        }

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {
            if (http == httpUpdateSellerLocation) {
                onHttpUpdateLocationResult(result);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if( buttonView == radioQuick && isChecked ){
                Log.d(TAG, "radioQuickPressed");
                isRadioQuickOrPreciseChecked = new boolean[]{true, false};
                saveRadioButtonStatus(isRadioQuickOrPreciseChecked);
            }
            if( buttonView == radioPrecise && isChecked ){
                Log.d(TAG, "radioPrecisePressed");
                isRadioQuickOrPreciseChecked = new boolean[]{false, true};
                saveRadioButtonStatus(isRadioQuickOrPreciseChecked);
            }

            Log.d(TAG, "radioPressedExit");
        }
    }



    private void updateRadioButtonFromPref(RadioButton quick, RadioButton precise){
        int radioNumber = ResFR.getPrefRadioOfUpdateLocation(context);
        Log.d(TAG, "loaded Radio Button = "+radioNumber);
        if(radioNumber == 0) {
            quick.setChecked(true);
            precise.setChecked(false);
            isRadioQuickOrPreciseChecked = new boolean[]{true, false};
        }else if(radioNumber == 1){
            quick.setChecked(false);
            precise.setChecked(true);
            isRadioQuickOrPreciseChecked = new boolean[]{false, true};
        }else{

        }
    }

    private void saveRadioButtonStatus(boolean[] b){
        for(int i = 0; i < b.length ; i++){
            if(b[i]){
                ResFR.setPrefRadioOfUpdateLocation(context, i);
                Log.d(TAG, "saved Radio Button = "+i);
                return;
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_OPEN_MAP_SET_LOCATION && resultCode == RESULT_OK){
            double[] locationResult = data.getDoubleArrayExtra(ResFR.INTENT_ACTIVITY_RESULT_PUT_EXTRA_LOCATION_KEY);

            if(locationResult != null) {
                onLocationReceivedDoHttpSave(locationResult);
            }else{
                showDialogYourLocationIsRecievedFromMap();
            }
        }
    }

    private void showDialogYourLocationIsRecievedFromMap() {

        new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_yourlocationnotreceivedfrommap).setPositiveKey(R.string.s_dialog_btn_ok, null);

    }


}
