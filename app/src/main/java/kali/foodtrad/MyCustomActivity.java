package kali.foodtrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by elliotching on 16-Mar-17.
 */

class MyCustomActivity extends AppCompatActivity {

//    public static boolean mIsInForegroundMode = false;
    String themeSetting;

    AppCompatActivity activity = this;
    Context context = this;
    String TAG = this.getClass().getSimpleName();
    protected void createMyView(int contentViewResID, int toolbarResID) {
        ResFR.setPrefIsAppRunning(this, true);
        debugInfo();

        checkLocationPermission();

        themeSetting = ResFR.getPrefTheme(this, ResFR.THEME);

        if(themeSetting.equals("Dark")){
            this.setTheme(R.style.AppThemeDark);
        }

        this.setContentView(contentViewResID);
        Toolbar toolbar = (Toolbar) this.findViewById(toolbarResID);
        if(themeSetting.equals("Dark"))toolbar.setPopupTheme(R.style.app_bar_white_text_dark_elliot);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void debugInfo() {
        String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);
        String username = ResFR.getPrefString(context, ResFR.USERNAME);
        String email = ResFR.getPrefString(context, ResFR.EMAIL);
        String device = ResFR.getPrefString(context, ResFR.DEVICE);
        String token = ResFR.getPrefString(context, ResFR.TOKEN);

        String is_seller = ResFR.getPrefString(context, ResFR.IS_SELLER);
        String seller_name = ResFR.getPrefString(context, ResFR.SELLER_NAME);
        String seller_doc_1 = ResFR.getPrefString(context, ResFR.SELLER_DOC_1);
        String seller_doc_2 = ResFR.getPrefString(context, ResFR.SELLER_DOC_2);
        String seller_ic_photo = ResFR.getPrefString(context, ResFR.SELLER_IC_PHOTO);

        double[] loc = ResFR.getPrefLocation(context);
        double lat = loc[0];
        double lng = loc[1];

        Log.d(this.getClass().getSimpleName(), "deviceUUID = "+deviceUUID);
        Log.d(this.getClass().getSimpleName(), "device = "+device);
        Log.d(this.getClass().getSimpleName(), "username = "+username);
        Log.d(this.getClass().getSimpleName(), "email = "+email);
        Log.d(this.getClass().getSimpleName(), "token = "+token);
        Log.d(this.getClass().getSimpleName(), "is_seller = "+is_seller);
        Log.d(this.getClass().getSimpleName(), "seller_name = "+seller_name);
        Log.d(this.getClass().getSimpleName(), "seller_doc_1 = "+seller_doc_1);
        Log.d(this.getClass().getSimpleName(), "seller_doc_2 = "+seller_doc_2);
        Log.d(this.getClass().getSimpleName(), "seller_ic_photo = "+seller_ic_photo);
        Log.d(this.getClass().getSimpleName(), "location (lat,lng) = "+lat+" , "+lng);
        Log.d(this.getClass().getSimpleName(), "Double.NEGATIVE_INFINITY = "+Double.NEGATIVE_INFINITY);
        Log.d(this.getClass().getSimpleName(), "Double.MAX_VALUE = "+Double.MAX_VALUE);
        Log.d(this.getClass().getSimpleName(), "Double.MIN_VALUE = "+Double.MIN_VALUE);
    }


    // THIS METHOD ONLY USED IN FOODIE_MAIN !!!
    protected String getMyTheme(){
        return themeSetting;
    }

    @Override
    protected void onPause() {
        super.onPause();

        boolean isActive = false;
        ResFR.setPrefIsAppRunning(this, isActive);

        Log.d("AppIsActive", String.valueOf(isActive));
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isActive = true;
        ResFR.setPrefIsAppRunning(this, isActive);

        Log.d("AppIsActive", String.valueOf(isActive));
    }


    void backButtonPressed(){
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Build.VERSION.SDK_INT > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            backButtonPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*    OPTIONS MENU >>>    */
    /*    OPTIONS MENU >>>    */
    /*    OPTIONS MENU >>>    */
    private boolean showAddFood = false;
    private boolean showSettings = false;
    private boolean showUsername = false;
    private boolean showLogout = false;
    private boolean showSearch = false;

    private MenuItem menuAddFood;
    private MenuItem menuSettings;
    private MenuItem menuUsername;
    private MenuItem menuLogout;
    private MenuItem menuSearch;

    // CALL "invalidateOptionsMenu()" TO FIRE THIS METHOD!!
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuAddFood.setVisible(showAddFood);
        menuUsername.setVisible(showUsername);
        menuSettings.setVisible(showSettings);
        menuLogout.setVisible(showLogout);
        menuSearch.setVisible(showSearch);
        return true;
    }

    private void createMenu(Context context, Menu menu){
        menuAddFood = menu.findItem(R.id.add_food);
        menuSettings = menu.findItem(R.id.setting);
        menuUsername = menu.findItem(R.id.username);
        String username = menuUsername.getTitle().toString();
        username += ResFR.getPrefString(context, ResFR.USERNAME);
        menuUsername.setTitle(username);
        menuLogout = menu.findItem(R.id.log_out);
        menuSearch = menu.findItem(R.id.menu_search);
    }

    protected void changeMenu(boolean showAddFood, boolean showUsername, boolean showSettings){
        this.showAddFood = showAddFood;
        this.showUsername = showUsername;
        this.showSettings = showSettings;
        invalidateOptionsMenu();
    }
    protected void changeMenu(boolean showAddFood, boolean showUsername, boolean showSettings, boolean logout){
        this.showLogout = logout;
        changeMenu(showAddFood, showUsername, showSettings);
    }
    protected void changeMenu(boolean showAddFood, boolean showUsername, boolean showSettings, boolean logout, boolean search){
        this.showSearch = search;
        changeMenu(showAddFood, showUsername, showSettings, logout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        createMenu(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == menuAddFood) {
            Intent intent = new Intent(this, ActivityAddFood.class);
            this.startActivity(intent);
            return true;
        }
        if(item == menuUsername){
            return true;
        }
        if (item == menuSettings){
            Intent intent = new Intent(this, ActivitySetting.class);
            this.startActivity(intent);
            return true;
        }
        if(item == menuLogout){
            doLogout();
            return true;
        }
        if(item == menuSearch){
            gotoSearchPage();
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            backButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*    <<< OPTIONS MENU    */
    /*    <<< OPTIONS MENU    */
    /*    <<< OPTIONS MENU    */


    private void gotoSearchPage() {
        Intent i = new Intent(context, ActivitySearch.class);
        startActivity(i);
    }

    void doLogout(){
        final Dialog_Progress p = new Dialog_Progress(activity, R.string.s_prgdialog_title_log_out, R.string.s_prgdialog_log_out, false);

        AsyncLogOut logOut = new AsyncLogOut(context);
        logOut.interfaceCustomHTTP = new InterfaceCustomHTTP() {
            @Override
            public void onCompleted(String result) {
                p.dismiss();

                try {
                    JSONObject json = new JSONObject(result);
                    String success = json.optString("success");
                    String log_out = json.optString("log_out");
                    String username_json = json.optString("username");

                    String username = ResFR.getPrefString(context, ResFR.USERNAME);

                    if(log_out.equals("1") && success.equals("1")){
                        ResFR.setPrefString(context, ResFR.USERNAME, ResFR.DEFAULT_EMPTY);
                        ResFR.setPrefString(context, ResFR.EMAIL, ResFR.DEFAULT_EMPTY);

                        ResFR.setPrefString(context, ResFR.SELLER_NAME, ResFR.DEFAULT_EMPTY);
                        ResFR.setPrefString(context, ResFR.SELLER_DOC_1, ResFR.DEFAULT_EMPTY);
                        ResFR.setPrefString(context, ResFR.SELLER_DOC_2, ResFR.DEFAULT_EMPTY);
                        ResFR.setPrefString(context, ResFR.SELLER_IC_PHOTO, ResFR.DEFAULT_EMPTY);
                        ResFR.setPrefLocation(context, new double[]{ResFR.DEFAULT_EMPTY_LOCATION , ResFR.DEFAULT_EMPTY_LOCATION});
                        restartAtLogIn();
                    }
                    else{
                        showDialogPhpError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    showDialogPhpError(result);
                }
            }

            @Override
            public void onCompleted(String result, CustomHTTP http) {

            }
        };
        logOut.executeThis();
    }

    void restartAtLogIn() {
        Intent i = new Intent(context, ActivityLogIn.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.finish();
        activity.startActivity(i);
    }

    void showDialogPhpError(String error){
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, error)
                .setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    void showDialogPhpError(final String error, final boolean closeActivityIfFAILTOCONNECT){
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, error)
                .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(closeActivityIfFAILTOCONNECT && error.equals(ResFR.string(context, R.string.s_dialog_msg_failedconnect))){
                            activity.finish();
                        }
                    }
                });
    }


    public static final int FR_PERMISSIONS_REQUEST_CODE_LOCATION = 99;

    void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, CHANGE_WIFI_STATE) != PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, ACCESS_WIFI_STATE) != PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE, ACCESS_WIFI_STATE, ACCESS_COARSE_LOCATION}, FR_PERMISSIONS_REQUEST_CODE_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FR_PERMISSIONS_REQUEST_CODE_LOCATION: {
                for (int grantResult : grantResults) {
                    if (grantResult == PERMISSION_GRANTED) {

                    } else {
                        this.finish();
                        break;
                    }
                }
//                if (allGranted) {
//                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
//                        if (fusedLocationTracker.mGoogleApiClient == null) {
//                            fusedLocationTracker.buildGoogleApiClient();
//                        }
//                        googleMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    // EXIT MAP!!!!!!
//                    this.onPause();
//                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    boolean checkLocationPermission_v22() {
        LocationManager locManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locManager.isProviderEnabled(GPS_PROVIDER) ||
                locManager.isProviderEnabled(NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    void makeToast(String result){
        Toast.makeText(this, "FoodieRoute: "+result, Toast.LENGTH_LONG).show();
    }

    String stringOf(double value){
        return String.format("%.10f", value);
    }

    String stringOf0dec(double value){
        return String.format("%.0f", value);
    }
    String stringOf2dec(double value){
        return String.format("%.2f", value);
    }

    void updateFoodDistanceStringAndDouble(FoodListingObject food, double mylat, double mylng){

        double lat = food.lat;
        double lng = food.lng;
        String distanceString = ResFR.string(context, R.string.s_listview_fooddistance_away);
        String locationunknown = ResFR.string(context, R.string.s_label_locationunknown);
        double distanceValueMeterDouble = ResFR.DEFAULT_EMPTY_LOCATION;
        if (mylat > 90.0 || mylat < -90.0 || mylng > 180.0 || mylng < -180.0 ||
                lat > 90.0 || lat < -90.0 || lng > 180.0 || lng < -180.0) {
            distanceString = locationunknown;
        } else {
            Log.d("AdapterListView", "Location found " + mylat + " " + mylng);
            ValueDist distance = distanceOf(mylat, mylng, lat, lng, 0.0, 0.0);
            if(distance.unit.equals("K")) {
                String value_unit = stringOf2dec(distance.value) + " " + "km";
                distanceString = distanceString.replace("$distance_meter$", value_unit);
                distanceValueMeterDouble = distance.valueInMeter;
            }else{
                String value_unit = stringOf0dec(distance.value) + " " + "meter";
                distanceString = distanceString.replace("$distance_meter$", value_unit);
                distanceValueMeterDouble = distance.valueInMeter;
            }
        }

        food.distanceString = distanceString;
        food.distanceDouble = distanceValueMeterDouble;
    }

    // Haversine formulae
    ValueDist distanceOf(double lat1, double lon1,
                                 double lat2, double lon2,
                                 double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        double result = Math.sqrt(distance);
        if(result > 999.9){
            double results = result / 1000.0;
            return new ValueDist("K", results, result);
        }else{
            return new ValueDist("M", result, result);
        }
    }

    class ValueDist{
        final String unit;
        final double value;
        final double valueInMeter;
        ValueDist(String u, double v, double m){
            unit = u;
            value = v;
            valueInMeter = m;
        }
    }

    boolean isLocationEmpty(double[] mycurrentlocation){
        Log.d(TAG, "checkingCurrentLocation = "+mycurrentlocation[0]+" , "+mycurrentlocation[1]);
        Log.d(TAG, "CHECK_EMPTY_LOCATION = "+ResFR.CHECK_EMPTY_LOCATION);
        if(mycurrentlocation[0] > ResFR.CHECK_EMPTY_LOCATION || mycurrentlocation[1] > ResFR.CHECK_EMPTY_LOCATION){

            Log.d(TAG, "LOCATION IS EMPTY");
            return true;// Empty
        }else{

            Log.d(TAG, "LOCATION IS NOT EMPTY");
            return false;// not empty
        }
    }

    void restartFromMainActivity() {
        Intent i = new Intent(context, ActivityMain.class);
        activity.finish();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    void updateLang(){
        Resources res = context.getResources();
        String appLang = ResFR.getPrefString(context, ResFR.LANGUAGE);
// Change locale settings in the app.

        if(appLang.equals(ResFR.DEFAULT_EMPTY)) {
            appLang = Locale.getDefault().getLanguage();
        }
        Log.d("language", appLang);
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = (new Locale(appLang));
        // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    double doubleOf(String s){
        try {
            return Double.valueOf(s);
        }catch(Exception e){
            return ResFR.DEFAULT_EMPTY_LOCATION;
        }
    }

    DialogInterface.OnClickListener clickToEndActivity() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        };
    }

    void adjustImageSize(ImageView imageView, int widthRatio, int heightRatio, int maxWidthPixel, float percentageMaxImageWidthToScreen) {
        /* Adjust Image Viewport Size */
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        Log.d("AdapterImageView", "LinearLayout.LayoutParams params.width = "+params.width);
        int screenWidth = Screen.getWidth(context);
        Log.d("AdapterImageView", "screenWidth = "+screenWidth);
        int pixelOf20dp = Screen.getPixels(context, 20.0f);
        Log.d("AdapterImageView", "pixelOf20dp = "+pixelOf20dp);
        int marginLR = pixelOf20dp * 2;
        Log.d("AdapterImageView", "marginLR = "+marginLR);
        int maxImageWidth = screenWidth - marginLR;
//        int maxImageWidthChanged = maxImageWidthAvailableOriginal;
        Log.d("AdapterImageView", "maxImageWidth = "+maxImageWidth);

//        int imageViewNewWidth = maxImageWidth / 2;

        if(widthRatio == 0) {
            widthRatio = 1;
        }
        if(heightRatio == 0) {
            heightRatio = 1;
        }

        if(percentageMaxImageWidthToScreen <= 0.5f) {
            // Do nothing
        }
        else{
            float maxImageWF = maxImageWidth;
            maxImageWF = maxImageWF * percentageMaxImageWidthToScreen / 100.0f;
            maxImageWidth = (int)maxImageWF;
        }

        if(maxWidthPixel == 0) {
            // Do nothing
        }
        else if (maxImageWidth > maxWidthPixel) {
            maxImageWidth = maxWidthPixel;
        }


        params.width = maxImageWidth;
        int imageViewNewHeight = maxImageWidth / widthRatio * heightRatio;

        params.height = imageViewNewHeight;

        imageView.setLayoutParams(params);
    }

    void adjustImageSize(ImageView imageView) {
        adjustImageSize(imageView, 4, 3, 1280, 0);
    }

    void createBitmapDisplayOnImageView(ImageView imageView, String filepath){
//        File image = new File(filepath);
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
//        imageView.setImageBitmap(bitmap);
        imageView.setImageURI(Uri.parse(filepath));
    }
}


