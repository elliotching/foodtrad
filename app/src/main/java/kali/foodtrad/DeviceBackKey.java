package kali.foodtrad;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * Created by elliotching on 15-Mar-17.
 */

class DeviceBackKey implements DialogInterface.OnKeyListener {

    AlertDialog.Builder ad;
    ProgressDialog pd;
    AppCompatActivity activity;
    boolean closeActivity;

    DeviceBackKey(AppCompatActivity a , ProgressDialog progressDialog, boolean closeActivity){
        this.pd = progressDialog;
        activity = a;
        this.closeActivity = closeActivity;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(dialog == pd)pd.dismiss();
            if(closeActivity)activity.finish();
        }
        return false;
    }
}