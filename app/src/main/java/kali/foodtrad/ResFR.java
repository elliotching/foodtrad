package kali.foodtrad;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elliotching on 24-Feb-17.
 */

class ResFR {

    static final String currentVersion = "1.0.0.4";

    private final Context context;
    private static final String APP_IS_ACTIVE = "FR_app_is_active";
    static final String PREF_NAME = "FoodieRoute";
    static final String DEVICEUUID = "FR_deviceUUID";
    static final String LANGUAGE = "FR_lang";

    static final String ENGLISH = "en";
    static final String CHINESE = "zh";
    static final String KOREAN = "ko";
    static final String THEME = "FR_theme";
    static final String TOKEN = "FR_token";
    static final String USERNAME = "FR_username";
    static final String DEVICE = "FR_device";
    static final String EMAIL = "FR_email";
    static final String IS_SELLER = "FR_is_seller";
    private static final String SELLER_LOCATION_LAT = "FR_seller_location_lat";
    private static final String SELLER_LOCATION_LNG = "FR_seller_location_lng";
    static final String SELLER_IC_PHOTO = "FR_seller_ic_photo";
    static final String SELLER_DOC_1 = "FR_seller_doc_1";
    static final String SELLER_DOC_2 = "FR_seller_doc_2";
    static final String SELLER_NAME = "FR_seller_name";
    static final String FIREBASE_INSTANCE_ID = "FR_firebase_IID";
    static final String DEFAULT_EMPTY = "empty";
    static final String THEME_LIGHT_DEFAULT = "Light";
    static final String THEME_DARK = "Dark";
    static final String RADIO_BUTTON_QUICK_OR_PRECISE = "radio_button_0_quick_or_1_precise";
    static final float DEFAULT_EMPTY_LOCATION = Float.MAX_VALUE;//Float.MAX_VALUE;
    static final float CHECK_EMPTY_LOCATION = Float.MAX_VALUE / 10.0f;
    static final String LAST_PIC_TAKEN_PATH = "FR_last_pic_path";
    static final String LAST_PIC_NAME = "FR_last_pic_name";
    static final String LAST_PIC_INCOMPLETE = "FR_last_pic_incomplete";

//    static final float DEFAULT_EMPTY_LOCATION = 12123123123123123.0f;//Float.MAX_VALUE;
//    static final float CHECK_EMPTY_LOCATION = 12123123123123.0f;;


    // USED IN ACTIVITY LOG IN ( WHENEVER RECEIVED MESSAGE OF "LOG OUT" = 1 )
    // OPEN THE ACTIVITY LOG IN AND IT WILL CHECK THE EXISTENT OF THIS KEY
    static final String BUNDLE_KEY_KICKED_OUT = "kicked_out";
    static final String BUNDLE_KEY_VIEW_MAP_ONLY = "view_map_only";
    static final String BUNDLE_KEY_MAP_LOCATION = "marker_location";

    static final String INTENT_ACTIVITY_RESULT_PUT_EXTRA_LOCATION_KEY = "savedlocation";

    static final String BUNDLE_KEY_VIEW_MY_FOOD_ONLY = "view_my_food_only";
    static final String BUNDLE_KEY_ADDING_FOOD_MENU = "adding_food_menu";

    // deprecated!!!
    static final String URL_get_token = "http://foodlocator.com.my/mobile/get_token.php";
    static final String URL_insert_token = "http://foodlocator.com.my/mobile/insert_token.php";

    // currently_active!!!
//    private static final String DOMAIN = "http://192.168.50.3:2083/mb/";//wifi
    private static final String DOMAIN = "http://192.168.50.107:2083/mb/";//ethernet
//    private static final String DOMAIN = "https://kalivolume.xyz/mb/";
//    private static final String DOMAIN = "https://zacbrook.my/mb/";
    
    static final String URL = DOMAIN+"action.php";
    static final String URL_READING = DOMAIN+"read_img.php";
    static final String URL_SMALL_IMG = DOMAIN+"read_img.php";
    static final String URL_UPLOAD = DOMAIN+"imgupload/upload.php";
//    static final String URL_send_mesg = "http://"+DOMAIN+"/mobile/send_mesg.php";
//    static final String URL_sign_up = "http://"+DOMAIN+"/mobile/sign_up.php";
//    static final String URL_log_in = "http://"+DOMAIN+"/mobile/log_in.php";
//    static final String URL_check_log_in_status = "http://"+DOMAIN+"/mobile/check_log_in_status.php";
//    static final String URL_on_token_refresh = "http://"+DOMAIN+"/mobile/on_token_refresh.php";
//    static final String URL_add_food = "http://"+DOMAIN+"/mobile/add_food.php";
//    static final String URL_read_image = "http://"+DOMAIN+"/mobile/imgupload/read_image.php";
//    static final String URL_read_small_image = "http://"+DOMAIN+"/mobile/imgupload/read_small_image.php";
//    static final String URL_get_all_food = "http://"+DOMAIN+"/mobile/get_all_food.php";
//    static final String URL_get_my_food = "http://"+DOMAIN+"/mobile/get_my_food.php";
//    static final String URL_check_version_update = "http://"+DOMAIN+"/mobile/check_version_update.php";
//    static final String URL_account_activation = "http://"+DOMAIN+"/mobile/account_activation.php";
//    static final String URL_change_password = "http://"+DOMAIN+"/mobile/change_password.php";
//    static final String URL_log_out = "http://"+DOMAIN+"/mobile/log_out.php";
//    static final String URL_update_seller_location = "http://"+DOMAIN+"/mobile/update_seller_location.php";
//    static final String URL_get_all_token = "http://"+DOMAIN+"/mobile/get_all_token.php";
//    static final String URL_search = "http://"+DOMAIN+"/mobile/search.php";


    ResFR(Context c){
        context = c;
    }

    String string(int _R){
        return context.getResources().getString(_R);
    }

    Resources res(){
        return context.getResources();
    }

    int color(int _R){
        return ResourcesCompat.getColor(context.getResources(),_R,null);
    }

    static int color(Context context, int _R){
        return ResourcesCompat.getColor(context.getResources(),_R,null);
    }

    static int dimenPx(Context context,int _d){
        return context.getResources().getDimensionPixelSize(_d);
    }

    static String string(Context context, int _R){
        return context.getResources().getString(_R);
    }



    /************/
    // GET
    /************/
    static String getPrefString(Context context, String keyName){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        return pref.getString(keyName, DEFAULT_EMPTY);
    }

    static String getPrefTheme(Context context, String keyName){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        return pref.getString(keyName, THEME_LIGHT_DEFAULT);
    }

    static boolean getPrefIsAppRunning(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        return pref.getBoolean(APP_IS_ACTIVE , false);
    }

    static int getPrefRadioOfUpdateLocation(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        int quick0OrPrecise1 = pref.getInt(RADIO_BUTTON_QUICK_OR_PRECISE , -1);
        Log.d("getRadioButtonStatus", "Pref Radio Int Number = "+quick0OrPrecise1);
        return quick0OrPrecise1;
    }

    static double[] getPrefLocation(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        double lat = pref.getFloat(SELLER_LOCATION_LAT , DEFAULT_EMPTY_LOCATION);
        double lng = pref.getFloat(SELLER_LOCATION_LNG , DEFAULT_EMPTY_LOCATION);
        return new double[]{lat, lng};
    }

    static String getPrefLocationLat(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        double lat = pref.getFloat(SELLER_LOCATION_LAT , DEFAULT_EMPTY_LOCATION);
        if(lat == DEFAULT_EMPTY_LOCATION){
            return DEFAULT_EMPTY;
        }else{
            return stringOf(lat);
        }
    }

    static String getPrefLocationLng(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        double lng = pref.getFloat(SELLER_LOCATION_LNG , DEFAULT_EMPTY_LOCATION);
        if(lng == DEFAULT_EMPTY_LOCATION){
            return DEFAULT_EMPTY;
        }else{
            return stringOf(lng);
        }
    }

    static String[] getPrefLastPic(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        String lastPath = pref.getString(LAST_PIC_TAKEN_PATH , DEFAULT_EMPTY);
        String lastPicName = pref.getString(LAST_PIC_NAME , DEFAULT_EMPTY);
        return new String[]{lastPath, lastPicName};
    }

    static boolean getPrefCheckLastPic(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        boolean lastPath = pref.getBoolean(LAST_PIC_INCOMPLETE , false);
        return lastPath;
    }

    /************/
    // SAVE SET
    /************/
    static void setPrefString(Context context, String keyName, String value){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit().putString(keyName, value);
        editor.commit();
    }

    static void setPrefTheme(Context context, String keyName, String value){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit().putString(keyName, value);
        editor.commit();
    }

    static void setPrefIsAppRunning(Context context, boolean value){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit().putBoolean( APP_IS_ACTIVE , value);
        editor.commit();
    }

    static void setPrefLocation(Context context, double[] loc){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat( SELLER_LOCATION_LAT , (float)loc[0]);
        editor.putFloat( SELLER_LOCATION_LNG , (float)loc[1]);
        editor.commit();
    }

    static void setPrefRadioOfUpdateLocation(Context context, int isQuick0OrPrecise1Checked){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt( RADIO_BUTTON_QUICK_OR_PRECISE, isQuick0OrPrecise1Checked);
        editor.commit();
    }

    static void setPrefLastPic(Context context, String[] image){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean( LAST_PIC_INCOMPLETE, true);
        editor.putString( LAST_PIC_TAKEN_PATH, image[0]);
        editor.putString( LAST_PIC_NAME, image[1]);
        editor.commit();
    }

    static void setPrefRemoveLastPic(Context context){
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean( LAST_PIC_INCOMPLETE, false);
        editor.putString( LAST_PIC_TAKEN_PATH, DEFAULT_EMPTY);
        editor.putString( LAST_PIC_NAME, DEFAULT_EMPTY);
        editor.commit();
    }



    private static String stringOf(double value){
        return String.format("%.10f", value);
    }

    static double doubleOf(String s){
        try {
            return Double.valueOf(s);
        }catch(Exception e){
            return ResFR.DEFAULT_EMPTY_LOCATION;
        }
    }

    static void checkFirebaseTokenIfNoThenMakeToast(Context c){
        String token = ResFR.getPrefString(c, ResFR.TOKEN);
        if(token.equals(ResFR.DEFAULT_EMPTY)){
            Toast.makeText(c, "Token not found.", Toast.LENGTH_LONG).show();
        }else{
//            Toast.makeText(c, "Token found.", Toast.LENGTH_LONG).show();
        }
    }

    static String getTokenFromFCM_JSON(String tokenInJSON) {
        String jtoken = "";
        try {
            JSONObject json = new JSONObject(tokenInJSON);
            jtoken = json.optString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return tokenInJSON;
        }

        Log.d("RESFR.getToken",jtoken);
        return jtoken;
    }
}
