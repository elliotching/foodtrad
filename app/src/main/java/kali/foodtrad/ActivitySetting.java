package kali.foodtrad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by elliotching on 09-Mar-17.
 */

public class ActivitySetting extends MyCustomActivity {
    Context context = this;
    AppCompatActivity activity= this;
    RadioGroup radioGroup;
    RadioButton radioLight;
    RadioButton radioDark;
    Button buttonChangePassword;
    Button buttonEnglish;
    Button buttonChinese;
    Button buttonSystem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMyView(R.layout.activity_setting, R.id.toolbar_activity_setting);

        initUIsetup();
    }

    private void initUIsetup(){

        buttonEnglish = (Button) findViewById(R.id.button_english);
        buttonChinese = (Button) findViewById(R.id.button_chinese);
        buttonSystem = (Button) findViewById(R.id.button_system);
        buttonChangePassword = (Button) findViewById(R.id.button_change_password);
        radioGroup = (RadioGroup) findViewById(R.id.radiogrp_theme_activity_setting);
        radioLight = (RadioButton) findViewById(R.id.radiobutton_theme_light_activity_setting);
        radioDark = (RadioButton) findViewById(R.id.radiobutton_theme_dark_activity_setting);

        OnClick listener = new OnClick();

        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        String theme = pref.getString(ResFR.THEME,"Light");
        if(theme.equals("Dark")) radioDark.setChecked(true);

        radioLight.setOnClickListener(new OnClick());

        radioDark.setOnClickListener(new OnClick());

        buttonChangePassword.setOnClickListener(new OnClick());

        buttonEnglish.setOnClickListener(listener);
        buttonChinese.setOnClickListener(listener);
        buttonSystem.setOnClickListener(listener);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == radioLight){

                ResFR.setPrefTheme(context, ResFR.THEME, ResFR.THEME_LIGHT_DEFAULT);

                Intent intent = new Intent(context, ActivitySetting.class);
                activity.startActivity(intent);
                activity.finish();
            }

            if(view == radioDark){

                ResFR.setPrefTheme(context, ResFR.THEME, ResFR.THEME_DARK);

                Intent intent = new Intent(context, ActivitySetting.class);
                activity.startActivity(intent);
                activity.finish();
            }

            if(view == buttonChangePassword){
                Intent intent = new Intent(context, ActivityChangePassword.class);
                activity.startActivity(intent);
            }

            if(view == buttonEnglish){
                ResFR.setPrefString(context, ResFR.LANGUAGE, ResFR.ENGLISH);
                updateLang();
                restartThis();
            }

            if(view == buttonChinese){
                ResFR.setPrefString(context, ResFR.LANGUAGE, ResFR.CHINESE);
                updateLang();
                restartThis();
            }

            if(view == buttonSystem){
                ResFR.setPrefString(context, ResFR.LANGUAGE, ResFR.DEFAULT_EMPTY);
                updateLang();
                restartThis();
            }
        }
    }

    private void restartThis(){
        Intent intent = new Intent(context, ActivitySetting.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    void backButtonPressed() {
        restartFromMainActivity();
    }
}
