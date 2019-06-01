package kali.foodtrad;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by elliotching on 27-Apr-17.
 */

public class ActivityExample extends MyCustomActivity {

    Context context = this;
    AppCompatActivity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createMyView(R.layout.activity_log_in, R.id.toolbar);
    }
}
