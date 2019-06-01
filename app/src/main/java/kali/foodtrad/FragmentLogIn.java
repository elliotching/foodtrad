package kali.foodtrad;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;

public class FragmentLogIn extends Fragment {

    //    public static boolean isOpened = false;
//    public static String isOpen_StringKey = "isOpen";
    Context context;
    Fragment mFragment;
    AppCompatActivity activity;
    Timer timer = new Timer();
    String mToBeDisplayMsg = "";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
//    private Button mLoginButton;
//    private Button buttonSignUp;
//    private EditText editTextUsername , editTextPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = this.getContext();
        activity = (AppCompatActivity) context;
        mFragment = this;

        View view = inflater.inflate(R.layout.fragment_one_log_in, container,
                false);


//        mLoginButton = (Button) view.findViewById(R.id.m_login_button);
//        mLoginButton.setOnClickListener(new Click());
//
//        editTextUsername = (EditText) view.findViewById(R.id.edit_text_username);
//        editTextPassword = (EditText) view.findViewById(R.id.edit_text_password);
//        editTextPassword.setOnEditorActionListener(new Click());
//
//        buttonSignUp = (Button) view.findViewById(R.id.button_sign_up);
//        buttonSignUp.setOnClickListener(new Click());
//        mLoginButton.setTransformationMethod(null);
//        this.getArguments().getBoolean()
//        activity.getSupportActionBar().addOnMenuVisibilityListener();



        return view;
    }

    public void stopAsyncTask() {
        timer.cancel();
    }
//
//    private class Click implements View.OnClickListener , TextView.OnEditorActionListener {
//
//        @Override
//        public void onClick(View v) {
//            if(v==mLoginButton){
//                doLogin();
//            }
//            if(v==buttonSignUp){
//                startActivity(new Intent(context, ActivitySignUp.class));
//            }
//        }
//
//        @Override
//        public boolean onEditorAction(TextView textView, int editor, KeyEvent keyEvent) {
//            if(editor == EditorInfo.IME_ACTION_DONE){
//                doLogin();
//            }
//            return false;
//        }
//    }
//
//    private void doLogin(){
//        String username = editTextUsername.getText().toString();
//        String password = editTextPassword.getText().toString();
//
//        if(username.equals("")||password.equals("")){
//            new Dialog_AlertNotice(context, "Error", "Please fill in all fields.").setPositiveKey("OK", null);
//        }
//        else{
//
//            String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);
//            String device = ResFR.getPrefString(context, ResFR.DEVICE);
//            String token = ResFR.getPrefString(context, ResFR.TOKEN);
//
//            String[][] data = new String[][]{
//                    {"pass","!@#$"},
//                    {"username",username},
//                    {"password",md5(password)},
//                    {"deviceUUID",deviceUUID},
//                    {"device",device},
//                    {"token",token}
//            };
//
//            new HTTP_POST(context, data, ResFR.URL_log_in);
//        }
//    }

//    public String md5(String s) {
//        try {
//            // Create MD5 Hash
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//            digest.update(s.getBytes());
//            byte messageDigest[] = digest.digest();
//
//            // Create Hex String
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0; i<messageDigest.length; i++)
//                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//            return hexString.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    private class HTTP_POST implements InterfaceCustomHTTP{
//        HTTP_POST(Context c, String[][] d, String url){
//            CustomHTTP cc = new CustomHTTP(c, d, url);
//            cc.ui = this;
//            cc.execute();
//        }
//        @Override
//        public void onCompleted(String result) {
//            try {
//                JSONObject json = new JSONObject(result);
//                String username = json.optString("username");
//                String activated = json.optString("activated");
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                String warning = ResFR.string(context, R.string.s_dialog_title_warning);
//                new Dialog_AlertNotice(context, warning, result)
//                        .setPositiveKey(R.string.s_dialog_btn_ok, null);
//            }
//        }
//    }


}