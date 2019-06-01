package kali.foodtrad;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static kali.foodtrad.ResFR.DEFAULT_EMPTY;
import static kali.foodtrad.ResFR.DEVICE;
import static kali.foodtrad.ResFR.DEVICEUUID;
import static kali.foodtrad.ResFR.FIREBASE_INSTANCE_ID;
import static kali.foodtrad.ResFR.USERNAME;

/**
 * Created by elliotching on 02-Mar-17.
 */

public class ServiceAndroidFirebaseInstanceIDElliot extends FirebaseInstanceIdService {

    private final String TAG = this.getClass().getSimpleName();
    Context context = this;
    static SharedPreferences pref;
    static String deviceUUID;
    static String device;
    static String username;
    static String firebase_IID;

    @Override
    public void onTokenRefresh() {

        getAllPref();

        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        String jtoken="";
        jtoken = ResFR.getTokenFromFCM_JSON(refreshedToken);
        ResFR.setPrefString(context, ResFR.TOKEN, jtoken);

        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        saveTokenToServer(refreshedToken);

    }

    private void saveTokenToServer(String token) {

        String notification = ResFR.string(context, R.string.s_notif_msg_token_updated);

        String[][] data = {
                {"act", "tokenref"},
                {"token", token},
                {"deviceUUID", deviceUUID},
                {"device", device},
                {"user", username},
                {"notifbody", notification}
        };

        if(username.equals(DEFAULT_EMPTY) || deviceUUID.equals(DEFAULT_EMPTY) || device.equals(DEFAULT_EMPTY)) {

        }else{
            new HTTP_POST(context, data, ResFR.URL);
        }
    }

    void getAllPref() {
        pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        deviceUUID = pref.getString(DEVICEUUID, DEFAULT_EMPTY);
        device = pref.getString(DEVICE, DEFAULT_EMPTY);
        username = pref.getString(USERNAME, DEFAULT_EMPTY);
        firebase_IID = pref.getString(FIREBASE_INSTANCE_ID, DEFAULT_EMPTY);
    }

    class HTTP_POST implements InterfaceCustomHTTP{
        HTTP_POST(Context c, String[][] d, String url){
            CustomHTTP cc = new CustomHTTP(c, d, url);
            cc.ui = this;
            cc.execute();
        }
        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {

        }
    }
}