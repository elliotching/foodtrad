package kali.foodtrad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by elliotching on 02-Mar-17.
 */

class ActivityFirebase extends AppCompatActivity {

    FirebaseUser a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firebase_result);
    }
}
