package kali.foodtrad;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by elliotching on 16-Mar-17.
 */

class UniversalLayoutInitToolbarAndTheme {

    String themeSetting;

    UniversalLayoutInitToolbarAndTheme(AppCompatActivity activity, int contentViewResID, int toolbarResID, boolean backKey){
        SharedPreferences pref = activity.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        themeSetting = pref.getString(ResFR.THEME,"Light");

        if(themeSetting.equals("Dark")){
            activity.setTheme(R.style.AppThemeDark);
        }

        activity.setContentView(contentViewResID);
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarResID);
        if(themeSetting.equals("Dark"))toolbar.setPopupTheme(R.style.app_bar_white_text_dark_elliot);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(backKey);
    }

    String getTheme(){
        return themeSetting;
    }


}
