package kali.foodtrad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by elliotching on 27-Apr-17.
 */
public class ActivityChangePassword extends MyCustomActivity {


    Context context = this;
    AppCompatActivity activity = this;

    EditText editOldPass;
    EditText editNewPass;
    EditText editConfirmPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.createMyView(R.layout.activity_change_password, R.id.toolbar);

        editOldPass = (EditText) findViewById(R.id.edittext_password_old);
        editNewPass = (EditText) findViewById(R.id.edittext_password_new);
        editConfirmPass = (EditText) findViewById(R.id.edittext_password_new_confirm);

        editConfirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == IME_ACTION_DONE){
                    submitPasswordChange();
                }
                return false;
            }
        });
    }

    private void submitPasswordChange() {

        final Dialog_Progress dialog = new Dialog_Progress(activity, R.string.s_prgdialog_title_submit_password, R.string.s_prgdialog_submitting_password, false);

        String newP = editNewPass.getText().toString();
        String oldP = editOldPass.getText().toString();
        final String confirmP = editConfirmPass.getText().toString();
        String username = ResFR.getPrefString(context, ResFR.USERNAME);

        String[][] data = new String[][]{
                {"act","changepass"},
                {"mode","mobile"},
                {"user", username},
                {"old_pass", md5(oldP)},
                {"new_pass", md5(newP)},
                {"con_pass", md5(confirmP)}
        };

        CustomHTTP c = new CustomHTTP(context, data, ResFR.URL);
        c.ui = new InterfaceCustomHTTP() {
            @Override
            public void onCompleted(String result) {
                dialog.dismiss();

                try {
                    JSONObject json = new JSONObject(result);

                    String activated = json.optString("activated", "");
                    String success = json.optString("success", "");
                    email = json.optString("email", "");

                    if(activated.equals("0")){
                        createDialogForAccountActivation();
                    }
                    else if(success.equals("1")){
                        new Dialog_AlertNotice(context, R.string.s_dialog_title_success, R.string.s_dialog_msg_sccesspasswordchange)
                                .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        restartAtLogIn();
                                    }
                                });
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

        c.execute();
    }

    private void createDialogForAccountActivation(){
//        Dialog_Progress p = new Dialog_Progress(context, R.string.)

        ActivateDialog g = new ActivateDialog();
        new Dialog_CustomNotice(context, R.string.s_dialog_title_accountactivation, R.layout.dialog_input_activation_key, g )
                .setPositiveKey(R.string.s_dialog_btn_ok, g , false)
                .setNegativeKey(R.string.s_dialog_btn_cancel, null, true);
    }


    String email;
    private class ActivateDialog implements InterfaceDialog, DialogInterface.OnClickListener, TextView.OnEditorActionListener{

        EditText editTextActiveationCode;
        TextView textViewSpamJunkMailBox;
        AlertDialog dialog;
        @Override
        public void onCreateDialogView(View v, Dialog_CustomNotice d) {
            // ON CREATE DIALOG VIEW

            this.dialog = d;
            textViewSpamJunkMailBox = (TextView) v.findViewById(R.id.text_view_check_junk);
            if(email != null){
                String defaultText = textViewSpamJunkMailBox.getText().toString();
                Log.d("defaultText", defaultText);
                defaultText = defaultText.replace("$address$", email);
                Log.d("replacedText", defaultText);
                Log.d("emailAddress", email);
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
            if(i == EditorInfo.IME_ACTION_DONE){
                dialog.dismiss();
                String code = editTextActiveationCode.getText().toString();
                validateActivationCode(code);
            }
            return false;
        }


    }


    private void validateActivationCode(String code){

        final Dialog_Progress d = new Dialog_Progress(activity, R.string.s_prgdialog_title_log_in, R.string.s_prgdialog_log_in_authenticate, false);
        String username = ResFR.getPrefString(context, ResFR.USERNAME);

        String[][] data = new String[][]{
                {"act", "activation"},
                {"mode","mobile"},
                {"code", code},
                {"user", username}
        };
        CustomHTTP c = new CustomHTTP(context, data, ResFR.URL);
        c.ui = new InterfaceCustomHTTP() {
            @Override
            public void onCompleted(String result) {

                d.dismiss();
                try {
                    JSONObject json = new JSONObject(result);
                    
                    String success = json.optString("result", "");
                    if(success.equals("true")){
                        restartFromMainActivity();
                    }
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

            @Override
            public void onCompleted(String result, CustomHTTP http) {

            }
        };

        c.execute();
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
}
