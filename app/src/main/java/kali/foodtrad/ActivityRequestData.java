package kali.foodtrad;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by elliotching on 23-Feb-17.
 */

public class ActivityRequestData extends AppCompatActivity {

    Context context = this;
    Toolbar mToolbar;
    EditText mEdittextURL;
    Button mButtonstartconnection;
    TextView mTextviewResult;
    private static final String privateurl = "http://foodlocator.com.my/mobile/test_s.php";
    static int time = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_data);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_apicall);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new ImplementedAsyncRequestData();

        mEdittextURL = (EditText) findViewById(R.id.edittext_apicall_ip_address);
        mEdittextURL.setText(privateurl);

        mButtonstartconnection = (Button) findViewById(R.id.button_start_apiconnection);
        mButtonstartconnection.setOnClickListener(new ButtonStart());

//        mTextviewResult = (TextView) findViewById(R.id.textview_apiresult);
    }

    private class ButtonStart implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == mButtonstartconnection) {
                RequestParams params = new RequestParams();
                params.put("att", "*");
                params.put("table", "Foods");
                AsyncRequestData.makeHTTPCall(context, "Loading...", params);
                //do

            }
        }
    }

//
//
//    public static void startCountPressAgainExit(int countFor_Seconds) {
//        final Handler handler = new Handler();
//        final Timer timer = new Timer();
//        final int endingTime = countFor_Seconds * 1000;
//        TimerTask_FR doAsynchronousTask = new TimerTask_FR() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            time += 100;
//                            if (time == 100) {
//                                AsyncRequestData.progressDialog.setMessage("Press again to cancel.");
//                            } else if (time >= endingTime) {
//                                AsyncRequestData.progressDialog.setMessage("Loading...");
//                                timer.cancel();
//                                time = 0;
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 100); //execute in every 100 ms
//    }






    private void doListView(ArrayList<JSONObject> responses) {
        try {
            Toast.makeText(context, responses.get(1).toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        }
    }


    private class ImplementedAsyncRequestData implements InterfaceAsyncRequestData {

        ImplementedAsyncRequestData() {
            AsyncRequestData.activityRequestData = this;
        }

        @Override
        public void getArrayListJSONResult(ArrayList<JSONObject> responses) {
            doListView(responses);
        }
    }
}
