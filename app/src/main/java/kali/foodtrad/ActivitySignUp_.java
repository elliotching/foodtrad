package kali.foodtrad;//package kali.foodtrad;
//
//import android.content.Context;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
///**
// * Created by Elliot on 03-Sep-16.
// */
//class ActivitySignUp_ extends AppCompatActivity {
//    Context context = this;
//    AppCompatActivity activity = this;
//
//    Dialog_Progress pd;
//    EditText editUsername, editEmail, editPassword, editConfirmPassword;
//    Button signUp;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        new UniversalLayoutInitToolbarAndTheme(this, R.layout.activity_sign_up, R.id.toolbar_activity_sign_up, true);
//
//        editUsername = (EditText) findViewById(R.id.edit_text_username);
//        editEmail = (EditText) findViewById(R.id.edit_text_email);
//        editPassword = (EditText) findViewById(R.id.edit_text_password);
//        editConfirmPassword = (EditText) findViewById(R.id.edit_text_password_confirm);
//        editConfirmPassword.setOnEditorActionListener(new OnClickEvent());
//        signUp = (Button) findViewById(R.id.button_sign_up);
//        signUp.setOnClickListener(new OnClickEvent());
//    }
//
//
//    class OnClickEvent implements View.OnClickListener,TextView.OnEditorActionListener {
//
//        @Override
//        public void onClick(View view) {
//            doSubmit();
//        }
//
//        @Override
//        public boolean onEditorAction(TextView textView, int editor, KeyEvent keyEvent) {
//            if (editor == EditorInfo.IME_ACTION_DONE) {
//                doSubmit();
//            }
//            return false;
//        }
//
//        void doSubmit(){
//            pd = new Dialog_Progress(activity,
//                    ResFR.string(context, R.string.s_prgdialog_title_sign_up),
//                    ResFR.string(context, R.string.s_prgdialog_submitting_signup_form), false);
//
//            String username = editUsername.getText().toString();
//            String email = editEmail.getText().toString();
//            String password = editPassword.getText().toString();
//            String confirm = editConfirmPassword.getText().toString();
//
//            if(username.equals("")||email.equals("")||password.equals("")||confirm.equals("")){
//                pd.dismiss();
//                new Dialog_AlertNotice(context, "Error", "Please fill in all fields.").setPositiveKey("OK",null);
//            }else if(!password.equals(confirm)){
//                pd.dismiss();
//                new Dialog_AlertNotice(context, "Error", "Password does not matched.").setPositiveKey("OK",null);
//            }
//            else{
//
//                String token = FirebaseInstanceId.getInstance().getToken();
//                ResFR.setPrefString(context, ResFR.TOKEN, token);
//                token = ResFR.getPrefString(context, ResFR.TOKEN);
//
//                String deviceUUID = ResFR.getPrefString(context, ResFR.DEVICEUUID);
//
//                String device = Build.MODEL;
//                ResFR.setPrefString(context, ResFR.DEVICE, device);
//                device = ResFR.getPrefString(context, ResFR.DEVICE);
//
//                String[][] data = new String[][]{
//                        {"pass", "!@#$"},
//                        {"username", username},
//                        {"email", email},
//                        {"password", md5(password)},
//                        {"confirm", md5(confirm)},
//                        {"is_seller", "0"},
//                        {"token", token},
//                        {"deviceUUID", deviceUUID},
//                        {"device", device}
//
//
//                };
//
//                new HTTP_POST(context, data, ResFR.URL_sign_up);
//            }
//        }
//    }
//
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
//            pd.dismiss();
//            try {
//                JSONObject json = new JSONObject(result);
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                String warning = ResFR.string(context, R.string.s_dialog_title_warning);
//                new Dialog_AlertNotice(context, warning, result)
//                        .setPositiveKey(R.string.s_dialog_btn_ok, null);
//            }
//
//        }
//
//        @Override
//        public void onCompleted(String result, CustomHTTP customHTTP) {
//
//        }
//    }
//}
