package kali.foodtrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elliotching on 25-Apr-17.
 */

public class ActivityLogIn extends MyCustomActivity {
    Context context = this;
    AppCompatActivity activity = (AppCompatActivity) context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button mLoginButton;
    private Button buttonSignUp;
    private EditText editTextUsername, editTextPassword;

    private CustomHTTP httpLogIn;
    private CustomHTTP httpActivation;
    private CustomHTTP httpCheckVersionUpdate;
    private AsyncCheckLogInStatus checkLogInStatus;

    private DialogButnListener dialogButtonnListener = new DialogButnListener();

    private Dialog_Progress progLogIn;
    private Dialog_Progress progCheck;
    private Dialog_Progress progCheckVersionUpdate;
    private Dialog_Progress progActivation;

    private boolean kicked = false;

    OnRequestComplete httpResult = new OnRequestComplete();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new UniversalLayoutInitToolbarAndTheme(activity, R.layout.activity_log_in, R.id.toolbar, false);

        createMyView(R.layout.activity_log_in, R.id.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle bundle = getIntent().getExtras();
        kicked = false;
        if (bundle != null) {
            kicked = bundle.getBoolean(ResFR.BUNDLE_KEY_KICKED_OUT, false);
        }

        checkLogInStatus = new AsyncCheckLogInStatus(context);
        checkLogInStatus.interfaceCustomHTTP = httpResult;

//        startCheckVersionUpdate();

        /*********************************************************
         * HOW TO GET ARGUMENTS::
         * RESULT = mFragment.getArguments().get*Boolean( KEY );
         *   or        <this>.getArguments().get*Boolean( KEY );
         *********************************************************/
        mLoginButton = (Button) findViewById(R.id.m_login_button);
        mLoginButton.setOnClickListener(new Click());

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        editTextPassword.setOnEditorActionListener(new Click());

        buttonSignUp = (Button) findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(new Click());
    }

    private void startCheckVersionUpdate() {
        progCheckVersionUpdate = new Dialog_Progress(activity, R.string.s_prgdialog_title_update, R.string.s_prgdialog_checking_version, false);
        String[][] data = new String[][]{
                {"act", "chkversion"},
                {"version", ResFR.currentVersion},
                {"mode","mobile"}
        };
        httpCheckVersionUpdate = new CustomHTTP(context, data, ResFR.URL);
        httpCheckVersionUpdate.ui = httpResult;
        httpCheckVersionUpdate.execute();
    }


    private void checkLogInStatus() {
        progCheck = new Dialog_Progress(activity,
                R.string.s_prgdialog_title_log_in,
                R.string.s_prgdialog_check_log_in_status,
                true);

        if (!ResFR.getPrefString(context, ResFR.USERNAME).equals(ResFR.DEFAULT_EMPTY)) {
            checkLogInStatus.start();
        } else {
            progCheck.dismiss();
        }
    }


    private void createDialogForAccountActivation() {

        ActivateDialog g = new ActivateDialog();
        new Dialog_CustomNotice(context, R.string.s_dialog_title_accountactivation, R.layout.dialog_input_activation_key, g)
                .setPositiveKey(R.string.s_dialog_btn_ok, g, false)
                .setNegativeKey(R.string.s_dialog_btn_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        doLogout();
                    }
                }, false)
                .setNeutralKey(R.string.s_dialog_btn_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartFromMainActivity();
                    }
                }, true);
    }

    private class Click implements View.OnClickListener, TextView.OnEditorActionListener {

        @Override
        public void onClick(View v) {
            if (v == mLoginButton) {
                doLogin();
            }
            if (v == buttonSignUp) {
                startActivity(new Intent(context, ActivitySignUp.class));
            }
        }

        @Override
        public boolean onEditorAction(TextView textView, int editor, KeyEvent keyEvent) {
            if (editor == EditorInfo.IME_ACTION_DONE) {
                doLogin();
                return true;
            }
            return false;
        }
    }

    private void doLogin() {
        progLogIn = new Dialog_Progress(activity, R.string.s_prgdialog_title_log_in,
                R.string.s_prgdialog_log_in_authenticate, false);

        final String user_email = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (user_email.equals("") || password.equals("")) {
            progLogIn.dismiss();
            new Dialog_AlertNotice(context, R.string.s_dialog_title_error, R.string.s_dialog_msg_plsfillall).setPositiveKey(R.string.s_dialog_btn_ok, null);
        } else {
    
            
            
            
            String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);
            String device = ResFR.getPrefString(context, ResFR.DEVICE);

            String token = FirebaseInstanceId.getInstance().getToken();
            token = ResFR.getTokenFromFCM_JSON(token);
            ResFR.setPrefString(context, ResFR.TOKEN, token);
            token = ResFR.getPrefString(context, ResFR.TOKEN);

            String[][] data = new String[][]{
                    {"act", "login"},
//                    {"key", "C35D"},
                    {"user", user_email},
                    {"pass", password},
                    {"deviceUUID", deviceUUID},
                    {"device", device},
                    {"token", token}
            };

            httpLogIn = new CustomHTTP(context, data, ResFR.URL);
            httpLogIn.ui = httpResult;
            httpLogIn.execute();
        }
    }

    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    String emailAddressForTextViewSpamJunk = null;
    TextView textViewSpamJunkMailBox = null;

    private class ActivateDialog implements InterfaceDialog, DialogInterface.OnClickListener, TextView.OnEditorActionListener {

        EditText editTextActiveationCode;
        Dialog_CustomNotice dialog;

        @Override
        public void onCreateDialogView(View v, Dialog_CustomNotice d) {
            // ON CREATE DIALOG VIEW

            this.dialog = d;
            textViewSpamJunkMailBox = (TextView) v.findViewById(R.id.text_view_check_junk);
            if (emailAddressForTextViewSpamJunk != null) {
                String defaultText = textViewSpamJunkMailBox.getText().toString();
                Log.d("defaultText", defaultText);
                defaultText = defaultText.replace("$address$", emailAddressForTextViewSpamJunk);
                Log.d("replacedText", defaultText);
                Log.d("emailAddress", emailAddressForTextViewSpamJunk);
                textViewSpamJunkMailBox.setText(defaultText);
            }
            editTextActiveationCode = (EditText) v.findViewById(R.id.edit_text_activation_code);
            editTextActiveationCode.setOnEditorActionListener(this);
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String code = editTextActiveationCode.getText().toString();
            validateActivationCode(code);
        }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                dialog.dismiss();
                String code = editTextActiveationCode.getText().toString();
                validateActivationCode(code);

            }
            return false;
        }
    }

    private void validateActivationCode(String code) {

        progActivation = new Dialog_Progress(activity, R.string.s_prgdialog_title_log_in, R.string.s_prgdialog_log_in_authenticate, false);
        String username = ResFR.getPrefString(context, ResFR.USERNAME);

        String[][] data = new String[][]{
                {"act", "activation"},
                {"code", code},
                {"user", username}
        };
        httpActivation = new CustomHTTP(context, data, ResFR.URL);
        httpActivation.ui = httpResult;

        httpActivation.execute();
    }

    private class OnRequestComplete implements InterfaceCustomHTTP {

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {

            Log.d("onCompleted", "http == " + http);
//            Log.d("onCompleted", "http == "+httpActivation);
//            Log.d("onCompleted", "http == "+httpLogIn);
//            Log.d("onCompleted", "http == "+checkLogInStatus.interfaceCustomHTTP);

            if (http == httpCheckVersionUpdate) {

                if (progCheckVersionUpdate != null) {
                    progCheckVersionUpdate.dismiss();
                }
                validateVersionResultAndCheckKicked(result);

            }

            // http result of Activation...
            if (http == httpActivation) {
                Log.d("onCompleted", "http == httpActivation");
                progActivation.dismiss();
                try {
                    JSONObject json = new JSONObject(result);
                    String rResult = json.optString("result", "");
                    //amended by CLTang
                    if (rResult.charAt(0) == '['){
                        //aosifjiosjaf
                        Log.d("onCompleted", rResult);
                    }
                    
//                    if (rResult.equals("1")) {
//                        Toast.makeText(context, R.string.s_dialog_title_success, Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(context, Foodie_main.class);
//                        activity.finish();
//                        startActivity(i);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    new Dialog_AlertNotice(context, R.string.s_dialog_title_error, result)
                            .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    createDialogForAccountActivation();
                                }
                            });
                }
            }


            if (http == httpLogIn) {
                Log.d("onCompleted", "http == httpLogIn");
                progLogIn.dismiss();
                try {
//                    JSONObject json = new JSONObject(result);
                    Log.d("onCompleted-httpLogIn", result);
                    if(result.charAt(0) == '['){
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject json = jsonArray.optJSONObject(0);
                    }
                    else{
                        JSONObject json = new JSONObject(result);
                        String jResult = json.optString("result");
                    }
                    showDialogPhpError(result);
                    
//                    String username_json = json.optString("username");
//                    String username_json = json.optString("user");
//                    String activated = json.optString("activated", "0");
//                    Log.d(this.getClass().getSimpleName(), "user from json = " + username_json);
//
//                    String success = json.optString("success", "0");
//                    String email = json.optString("email");
//                    String is_seller = json.optString("isseller");

//                    if (is_seller.equals("1") || is_seller.equals("2")) {
//                        String seller_location_lat = json.optString("seller_location_lat");
//                        String seller_location_lng = json.optString("seller_location_lng");
//                        String seller_ic_photo = json.optString("seller_ic_photo");
//                        String seller_doc_1 = json.optString("seller_doc_1");
//                        String seller_doc_2 = json.optString("seller_doc_2");
//                        String seller_name = json.optString("seller_name");
                        // edited 01.05.2019
//                        String seller_location_lat = json.optString("slloclat");
//                        String seller_location_lng = json.optString("slloclng");
//                        String seller_ic_photo = json.optString("slic");
//                        String seller_doc_1 = json.optString("sldoc1");
//                        String seller_doc_2 = json.optString("sldoc2");
//                        String seller_name = json.optString("slname");
//                        ResFR.setPrefString(context, ResFR.SELLER_NAME, seller_name);
//                        ResFR.setPrefString(context, ResFR.SELLER_DOC_1, seller_doc_1);
//                        ResFR.setPrefString(context, ResFR.SELLER_DOC_2, seller_doc_2);
//                        ResFR.setPrefString(context, ResFR.SELLER_IC_PHOTO, seller_ic_photo);
//
//                        double lat = doubleOf(seller_location_lat);
//                        double lng = doubleOf(seller_location_lng);
//                        ResFR.setPrefLocation(context, new double[]{lat, lng});
//                    }

//                    emailAddressForTextViewSpamJunk = email;
//
//                    ResFR.setPrefString(context, ResFR.USERNAME, username_json);
//                    ResFR.setPrefString(context, ResFR.EMAIL, email);
//                    ResFR.setPrefString(context, ResFR.IS_SELLER, is_seller);
//
//
//                    if (activated.equals("1") && success.equals("1")) {
//
//                        restartFromMainActivity();
//
//                    } else if (activated.equals("0") && success.equals("1")) {
//
//                        createDialogForAccountActivation();
//
//                    } else {
//
//                        showDialogPhpError(result);
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    String warning = ResFR.string(context, R.string.s_dialog_title_warning);
                    new Dialog_AlertNotice(context, warning, result)
                            .setPositiveKey(R.string.s_dialog_btn_ok, null);
                }
            }

            if (http == checkLogInStatus.httpAsyncCheck) {

                Log.d("onCompleted", "http == checkLogInStatus.interfaceCustomHTTP");
                progCheck.dismiss();
                if (result.equals("")) {
                    activity.finish();
                } else {
                    try {
                        JSONObject json = new JSONObject(result);
                        String activated = json.optString("activated", "");
                        String success = json.optString("success", "0");
                        String log_out = json.optString("log_out", "0");
                        String email = json.optString("email");
                        emailAddressForTextViewSpamJunk = email;
                        if (success.equals("1")) {
                            if (activated.equals("1")) {

                                // MEANS THIS ACCOUNT IS ACTIVATED!!!
                                restartFromMainActivity();

                            }
                            if (activated.equals("0")) {

                                // NOT ACTIVATED!! ASK FOR ACTIVATION CODE!!
                                createDialogForAccountActivation();

                            }

                        } else if (log_out.equals("1")) {

                            // BEEN LOGGED OUT
                            ResFR.setPrefString(context, ResFR.USERNAME, ResFR.DEFAULT_EMPTY);
                            ResFR.setPrefString(context, ResFR.EMAIL, ResFR.DEFAULT_EMPTY);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(ResFR.BUNDLE_KEY_KICKED_OUT, true);

                            Intent ii = new Intent(context, ActivityLogIn.class);
                            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ii.putExtras(bundle);

                            startActivity(ii);

                        }else{
                            showDialogPhpError(result);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        // SHOW ERROR MESSAGE FROM PHP!!!
                        showDialogPhpError(result, true);

                    }
                }
                // no respond. wait for notification to log you out if different token found.
            }
        }
    }


    private void validateVersionResultAndCheckKicked(String s) {
        // if result is not empty, got something. 1. Fail to connect 2. JSONArray
        if (!s.equals("")) {
            try {
                JSONArray jarray = new JSONArray(s);
                String latest = "";
                for (int i = 0; i < jarray.length(); i++) {
                    latest = jarray.getJSONObject(i).optString("version");
                }

                if (!latest.equals("") && !latest.equals(ResFR.currentVersion)) {
                    showDialogNewUpdateAvailable();
                }else{
                    checkIsKicked();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showDialogPhpError(s);
                checkIsKicked();
            }
        }else{
            checkIsKicked();
        }
    }

    private void showDialogNewUpdateAvailable() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_newupdateavailable).setPositiveKey(R.string.s_dialog_btn_ok, dialogButtonnListener);
    }

    private class DialogButnListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            checkIsKicked();
        }
    }

    private void checkIsKicked() {
        if (!kicked) {
            checkLogInStatus();
        } else {
            new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_kicked_out)
                    .setPositiveKey(R.string.s_dialog_btn_ok, null);
        }
    }
}


//        try {
//            String example = "{\"username\":\"尽力\",\"activated\":\"0\"}{\"multicast_id\":6125173515992925350,\"success\":1,\"failure\":0,\"canonical_ids\":0,\"results\":[{\"message_id\":\"0:1493132659266227%69d8bb63f9fd7ecd\"}]}";
//
//            String txt="[{pabcdef}{ghijklmn}]{iikkool}";
//
//            String re1="(\\[.*?\\])";	// Square Braces 1
//            String re2="(\\{.*?\\})";	// Curly Braces 1
//
//            Pattern progLogIn = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//            Matcher m = progLogIn.matcher(example);
//            if (m.find())
//            {
//                String sbraces1=m.group(1);
//                String cbraces1=m.group(2);
//                Log.progCheck("String", "("+sbraces1.toString()+")"+"("+cbraces1.toString()+")"+"\n");
//            }
//
//            String[] separated = example.split("\\}\\{");
//
//            String str1 = separated[0] + "}";
//            String str2 = "{" + separated[1];
//            JSONObject jjson = new JSONObject(str2);
//            Log.progCheck("STRING", "String1: " + str1 + "\nString2: " + str2);
//
//            String success = jjson.optString("success");
//            Log.progCheck(this.getClass().getSimpleName() + "Example", "example = " + success);
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }