package kali.foodtrad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by elliotching on 02-Mar-17.
 */

public class ActivityFirebaseResult extends AppCompatActivity {
    private TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        resetFCMCount();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_result);
        textView = (TextView)findViewById(R.id.textView1);
        String s="Message goes here.";
        textView.setText(s);
    }


}