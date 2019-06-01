package kali.foodtrad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by elliotching on 03-Mar-17.
 */

public class ActivityMsgBroadcast extends MyCustomActivity {

//
//
//    Context context = this;
//    AppCompatActivity activity = this;
//    Button btn;
    private ListView listView;
    private AdapterTokenListView adapter;
//    ArrayList<ObjectToken> tokenss = new ArrayList<>();
//    /*used to count the number of message in */
//    int i = 0;

    private CustomHTTP httpGetAllToken;
    private Dialog_Progress progGetAllToken;

    private Listener listener;
    private Button buttonSend;
    private EditText editBroadcastMsg;

    private ArrayList<TokenListObject> arrayListToken = null;
    private CustomHTTP[] sendingTokenHttps = null;

    private int _button_broadcast = R.string.s_button_startbroadcastmessage;
    private int _button_sending = R.string.s_button_sendingbroadcast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_broadcast, R.id.toolbar);
//        fetchAllUsersTokenss();
//        btn = (Button) findViewById(R.id.send_fcm);
//        listView = (ListView) findViewById(R.id.list_view);
//        listView.setOnItemClickListener(new OnClick());
//        btn.setOnClickListener(new OnClick());

        listView = (ListView) findViewById(R.id.list_view);
        buttonSend = (Button) findViewById(R.id.send_braodcast);
        editBroadcastMsg = (EditText) findViewById(R.id.edit_broadcastmsg);

        listener = new Listener();

        buttonSend.setOnClickListener(listener);

        fetchAllUsersTokenss();

    }

    private void fetchAllUsersTokenss() {
        if(progGetAllToken != null){
            progGetAllToken.dismiss();
        }
        progGetAllToken = new Dialog_Progress(activity, R.string.s_prgdialog_title_retrieve, R.string.s_prgdialog_msg_gettingalluserstoken, true);

        String[][] data = {
                {"act", "getalltokens"}
        };
        httpGetAllToken = new CustomHTTP(context, data, ResFR.URL);
        httpGetAllToken.ui = listener;
        httpGetAllToken.execute();
    }

    private class Listener implements InterfaceCustomHTTP, View.OnClickListener, AdapterView.OnItemClickListener {

        @Override
        public void onCompleted(String result) {
        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {
            if(http == httpGetAllToken){
                progGetAllToken.dismiss();
                populateList(result);
            }
            if(sendingTokenHttps != null) {
                buttonSend.setText(_button_broadcast);
                for (int i = 0; i < sendingTokenHttps.length; i++) {
                    if (http == sendingTokenHttps[i]) {
                        Log.d("result" + i, result);
                        try {

//                            {"multicast_id":5577579200943744439,"success":0,"failure":1,"canonical_ids":0,"results":[{"error":"MissingRegistration"}]}
                            JSONObject json = new JSONObject(result);
                            String success = json.optString("success");
                            String failure = json.optString("failure");
                            String results = json.optString("results");

                            if(success.equals("1")){
                                arrayListToken.get(i).sendingResults = " success";
                            }
                            if(failure.equals("1")){

                                JSONArray jarray = new JSONArray(results);
                                JSONObject jobj = jarray.optJSONObject(0);
                                String error = jobj.optString("error");
                                arrayListToken.get(i).sendingResults = " failed, "+error;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listView.invalidateViews();
                        break;
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
            if(v==buttonSend){
                Log.d("Send", "sending");
                startSend();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    private void startSend() {

        buttonSend.setText(_button_sending);
        if(arrayListToken != null) {
            int size = arrayListToken.size();
            sendingTokenHttps = new CustomHTTP[size];
            String username = ResFR.getPrefString(context, ResFR.USERNAME);
            String shopname = ResFR.getPrefString(context, ResFR.SELLER_NAME);
            String msgContent = editBroadcastMsg.getText().toString();
            for (int i = 0; i < sendingTokenHttps.length ; i ++) {

                arrayListToken.get(i).sendingResults = " "+ResFR.string(context, R.string.s_button_sendingbroadcast);

                String[][] data = new String[][]{
                        {"act", "sendmsg"},
                        {"token", arrayListToken.get(i).token},
                        {"data_body", "["+shopname+"]"+" "+msgContent},
                        {"user", username}
                };
                sendingTokenHttps[i] = new CustomHTTP(context, data, ResFR.URL);
                sendingTokenHttps[i].ui = listener;
            }
            listView.invalidateViews();
        }

        if(sendingTokenHttps != null && sendingTokenHttps.length >= 1) {
            Log.d("startSend", "startSend");
            for (int i = 0; i < sendingTokenHttps.length; i++) {
                sendingTokenHttps[i].execute();
                Log.d("startSend", "sendingTokenHttps["+i+"]");
            }
        }
//        if(sendingTokenHttps == null){
//            Log.d("startSend", "sendingTokenHttps == null");
//        }
//            Log.d("startSend", "sendingTokenHttps.length = " + sendingTokenHttps.length);
    }

    private void populateList(String s) {
        try {
            JSONArray jarray = new JSONArray(s);
            arrayListToken = new ArrayList<>();
            for(int i = 0 ; i < jarray.length() ; i ++){
                JSONObject json = jarray.getJSONObject(i);
//                String username = json.optString("username", "");
                String username = json.optString("user", "");
                String token = json.optString("token", "");
                TokenListObject tokenObj = new TokenListObject(username, token);
                arrayListToken.add(tokenObj);
            }
            dumpInitListView();
        } catch (JSONException e) {
            e.printStackTrace();
            showDialogPhpError(s);
        }

    }

    private void initListOfTokenHttps() {

    }

    private void dumpInitListView() {
        if(arrayListToken != null) {
            adapter = new AdapterTokenListView(context, arrayListToken);
            listView.setAdapter(adapter);
        }
    }
}
