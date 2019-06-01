package kali.foodtrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Elliot on 03-Sep-16.
 */
public class ActivitySignUp extends MyCustomActivity {
    Context context = this;
    AppCompatActivity activity = this;

    private Dialog_Progress pd;
    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private CheckBox checkBoxSeller;
    private Button signUp;
    private Button buttonLocation;
    private EditText editShopname;
    private CheckBox checkBoxMobileSeller;
    private LinearLayout layoutSeller;
    private boolean isSeller = false;
    private boolean isMobile = false;
    private double[] pickedLocation = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMyView(R.layout.activity_sign_up, R.id.toolbar_activity_sign_up);

        ResFR.checkFirebaseTokenIfNoThenMakeToast(context);

        editUsername = (EditText) findViewById(R.id.edit_text_username);
        editEmail = (EditText) findViewById(R.id.edit_text_email);
        editPassword = (EditText) findViewById(R.id.edit_text_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_text_password_confirm);
        signUp = (Button) findViewById(R.id.button_sign_up);
        checkBoxSeller = (CheckBox) findViewById(R.id.checkbox_isseller);
        buttonLocation = (Button) findViewById(R.id.button_location);
        editShopname = (EditText) findViewById(R.id.edittext_shopname);
        checkBoxMobileSeller = (CheckBox) findViewById(R.id.checkbox_mobileseller);
        layoutSeller = (LinearLayout) findViewById(R.id.linearlayout_sellersignupforms);
        layoutSeller.setVisibility(View.GONE);
        OnClickEvent a = new OnClickEvent();
        buttonLocation.setOnClickListener(a);
        checkBoxSeller.setOnCheckedChangeListener(a);
        checkBoxMobileSeller.setOnCheckedChangeListener(a);
        signUp.setOnClickListener(a);
    }

    class OnClickEvent implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @Override
        public void onClick(View view) {
            if (view == signUp) {
                doSubmit();
            }
            if (view == buttonLocation) {
                Intent i = new Intent(context, ActivityMaps.class);
                activity.startActivityForResult(i, PICK_LOC_CODE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == checkBoxSeller) {
                isSeller = isChecked;
                if (isSeller) {
                    layoutSeller.setVisibility(View.VISIBLE);
                } else {
                    layoutSeller.setVisibility(View.GONE);
                }
            }
            if (buttonView == checkBoxMobileSeller) {
                isMobile = isChecked;
            }
        }
    }



    void showWarningFillAll() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, R.string.s_dialog_msg_plsfillall).setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    void showWarningIncorrectPassword() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, R.string.s_dialog_msg_plsfillall).setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    void doSubmit() {
        pd = new Dialog_Progress(activity,
                ResFR.string(context, R.string.s_prgdialog_title_sign_up),
                ResFR.string(context, R.string.s_prgdialog_submitting_signup_form), false);

        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirm = editConfirmPassword.getText().toString();


        String is_seller = "0";

        if (username.equals("") || email.equals("") || password.equals("") || confirm.equals("")) {
            pd.dismiss();
            showWarningFillAll();
        } else if (!password.equals(confirm)) {
            pd.dismiss();
            showWarningFillAll();
        } else if (isSeller) {
            is_seller = "1";
            if(isSeller && isMobile){
                // mobile seller "2".
                is_seller = "2";
            }
            String shopname = editShopname.getText().toString();
            if (shopname.equals("") || pickedLocation == null) {
                pd.dismiss();
                showWarningFillAll();
            } else {
                submitData(username, email, password, confirm, is_seller, shopname, pickedLocation);
            }
        } else {
            submitData(username, email, password, confirm, is_seller);
        }
    }

    private void submitData(String username, String email, String password, String confirm, String is_seller) {
        String token = ResFR.getPrefString(context, ResFR.TOKEN);

        String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);

        String device = Build.MODEL;
        ResFR.setPrefString(context, ResFR.DEVICE, device);
        device = ResFR.getPrefString(context, ResFR.DEVICE);


        String[][] data = new String[][]{
                {"act", "signup"},
                {"user", username},
                {"email", email},
                {"pass", md5(password)},
                {"confirm", md5(confirm)},
                {"isseller", is_seller},
                {"token", token},
                {"deviceUUID", deviceUUID},
                {"device", device}
        };

        CustomHTTP cc = new CustomHTTP(context, data, ResFR.URL);
        cc.ui = new InterfaceCustomHTTP() {
            @Override
            public void onCompleted(String result) {
                pd.dismiss();
                try {
                    JSONObject json = new JSONObject(result);
                    String success = json.optString("success");
                    String username = json.optString("username");

                    if (success.equals("1")) {
                        showSuccessDialogOrToast(username);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    String warning = ResFR.string(context, R.string.s_dialog_title_warning);
                    new Dialog_AlertNotice(context, warning, result)
                            .setPositiveKey(R.string.s_dialog_btn_ok, null);
                }

            }

            @Override
            public void onCompleted(String result, CustomHTTP http) {

            }
        };
        cc.execute();
    }

    private void submitData(String username, String email, String password, String confirm, String is_seller, String shopname, double[] location) {
        String token = ResFR.getPrefString(context, ResFR.TOKEN);

        String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);

        String device = Build.MODEL;
        ResFR.setPrefString(context, ResFR.DEVICE, device);
        device = ResFR.getPrefString(context, ResFR.DEVICE);
    
    
        String[][] data = new String[][]{
                {"act", "signup"},
                {"user", username},
                {"email", email},
                {"pass", md5(password)},
                {"confirm", md5(confirm)},
                {"isseller", is_seller},
                {"token", token},
                {"deviceUUID", deviceUUID},
                {"device", device},
                {"slname", shopname},
                {"slloclat", stringOf(location[0])},
                {"slloclng", stringOf(location[1])}
        };

        CustomHTTP cc = new CustomHTTP(context, data, ResFR.URL);
        cc.ui = new InterfaceCustomHTTP() {
            @Override
            public void onCompleted(String result) {
                pd.dismiss();
                try {
                    JSONObject json = new JSONObject(result);
                    String success = json.optString("success");
                    String username = json.optString("username");

                    if (success.equals("1")) {
                        showSuccessDialogOrToast(username);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    String warning = ResFR.string(context, R.string.s_dialog_title_warning);
                    new Dialog_AlertNotice(context, warning, result)
                            .setPositiveKey(R.string.s_dialog_btn_ok, null);
                }

            }

            @Override
            public void onCompleted(String result, CustomHTTP http) {

            }
        };
        cc.execute();
    }


    // pick location code
    public static final int PICK_LOC_CODE = 768;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // return from map activity
        if (resultCode == RESULT_OK && requestCode == PICK_LOC_CODE) {
            pickedLocation = data.getExtras().getDoubleArray(ResFR.INTENT_ACTIVITY_RESULT_PUT_EXTRA_LOCATION_KEY);
            if (pickedLocation == null) {
                Log.d("Elliot", "returned Location success. Result is null");
            } else {
                Log.d("Elliot", "returned Location success.");
                String def = buttonLocation.getText().toString();
                def += (": " + stringOf(pickedLocation[0]) + " , " +
                        stringOf(pickedLocation[1]));
                buttonLocation.setText(def);
            }
        }
    }


    public String md5(String s) {
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

    private void showSuccessDialogOrToast(String user) {
        String successsignup = ResFR.string(context, R.string.s_dialog_msg_sccesssignup);
        successsignup = successsignup.replace("$username$", user);

        if(ResFR.getPrefIsAppRunning(context)) {
            new Dialog_AlertNotice(context, R.string.s_dialog_title_success, successsignup)
                    .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restartAtLogIn();
                        }
                    });
        }else{
            makeToast(successsignup);
        }
    }
}
