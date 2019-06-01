package kali.foodtrad;//package kali.foodtrad;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**
// * Created by elliotching on 03-Mar-17.
// */
//
//public class ActivityPostHTTPDataFirebaseTest extends AppCompatActivity {
//
//    static String firebase_HTTP = "https://fcm.googleapis.com/fcm/send";
//
//
//    Context context = this;
//    AppCompatActivity activity = this;
//    Button btn;
//    ListView listView;
//    ArrayList<ObjectToken> tokenss = new ArrayList<>();
//    /*used to count the number of message in */
//    int i = 0;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_http_data_firebase_test);
//        fetchAllUsersTokenss();
//        btn = (Button) findViewById(R.id.send_fcm);
//        listView = (ListView) findViewById(R.id.list_view);
//        listView.setOnItemClickListener(new OnClick());
//        btn.setOnClickListener(new OnClick());
//    }
//
//
//    private void sendMsg(String[][] msgContent) {
//        new MSG_SEND(context,msgContent, ResFR.URL_send_mesg);
//    }
//
//
//    private String[][] data_body(String receiverss) {
////        JSONArray a = new JSONArray();
////
////        JSONObject json = new JSONObject();
////        JSONObject jsonNoti = new JSONObject();
////            try {
////                jsonNoti.put("body", "HERE IS MSG BODY "+(i++));
////
//////                json.put("data", jsonNoti);
//////                json.put("to", receiverss);
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
//        String username = pref.getString("FR_username", "empty");
//        String[][] msg = {
//                {"table","msgs_testing"},
//                {"pass","!@#$"},
//                {"data_body","HERE IS MSG BODY "+(i++)},
//                {"token",receiverss},
//                {"username",username}
//        };
//        return msg;
//    }
//
//    private void fetchAllUsersTokenss(){
//        String[][] a = {
//                {"table","tokens_testing"},
//                {"pass","!@#$"}
//        };
//        new RetrieveAllTokens(context, a, ResFR.URL_get_token);
//    }
//
////    private class MyAsyncHttpRespondHandler extends AsyncHttpResponseHandler {
////
////
////        @Override
////        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
////            if (statusCode == 200) {
////                // Hide Progress Dialog
////                String echoResult = new String(responseBody);
////                Toast.makeText(context, echoResult,
////                        Toast.LENGTH_LONG).show();
////                new Dialog_AlertNotice(context, "Result", echoResult).setPositiveKey("OK",null).show();
////                System.out.println("onCompleted: " + echoResult);
////            }
////        }
////
////        @Override
////        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
////
////            // When Http response code is '404'
////            if (statusCode == 404) {
////                Toast.makeText(context,
////                        "Requested resource not found",
////                        Toast.LENGTH_LONG).show();
////            }
////
////            // When Http response code is '500'
////            else if (statusCode == 500) {
////                Toast.makeText(context,
////                        "Something went wrong at server end",
////                        Toast.LENGTH_LONG).show();
////            }
////
////            // When Http response code other than 404, 500
////            else {
////                Toast.makeText(
////                        context,
////                        "Error Occured n Most Common Error: \n" +
////                                "1. Device not connected to Internet\n" +
////                                "2. Web App is not deployed in App server\n" +
////                                "3. App server is not running\n HTTP Status code : "
////                                + statusCode, Toast.LENGTH_LONG)
////                        .show();
//////                System.out.println(headers.length >=1?headers[0].toString():"headers is empty");
//////                System.out.println(headers.length >=2?headers[1].toString():"headers 2 is empty");
//////                System.out.println(headers.length >=3?headers[2].toString():"headers 3 is empty");
////            }
////        }
////    }
//
//    private class MSG_SEND implements InterfaceCustomHTTP{
//        Dialog_Progress pd;
//        MSG_SEND(Context context, String[][] msgContent, String url){
//            pd = new Dialog_Progress(activity,
//                    ResFR.string(context, R.string.s_prgdialog_title_send_msg),
//                    ResFR.string(context, R.string.s_prgdialog_sending_msg), false);
//
//            CustomHTTP c = new CustomHTTP(context, msgContent, url);
//            c.ui=this;
//            c.execute();
//        }
//
//        @Override
//        public void onCompleted(String result) {
//            pd.dismiss();
//
//            // this part is try to get the data / info
//            // from Firebase returned JSON result
//            // whether it is success or not sent msg.
////            try {
////                JSONObject json = new JSONObject(result);
////                String multicast_id = json.optString();
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//            new Dialog_AlertNotice(context, "Status", result).setPositiveKey("OK",null);
//        }
//
//        @Override
//        public void onCompleted(String result, CustomHTTP http) {
//
//        }
//    }
//
//    private class RetrieveAllTokens implements InterfaceCustomHTTP{
//        Dialog_Progress pd;
//        RetrieveAllTokens(Context context, String[][] a, String url){
//
//            pd = new Dialog_Progress(activity,
//                    ResFR.string(context, R.string.s_prgdialog_title_load_user),
//                    ResFR.string(context,R.string.s_prgdialog_retrieving_all_token), false);
//
//            CustomHTTP c = new CustomHTTP(context, a, url);
//            c.ui=this;
//            c.execute();
//        }
//
//        @Override
//        public void onCompleted(String result) {
//            pd.dismiss();
//            try {
//                JSONArray d = new JSONArray(result);
//                for(int i = 0 ; i < d.length() ; i++){
//                    JSONObject a = d.optJSONObject(i);
//                    tokenss.add(new ObjectToken( a.optString("token"),a.optString("username") ) );
//                }
//                listView.setAdapter(new AdapterTokenListView(context, tokenss));
//            } catch (JSONException e) {
//                e.printStackTrace();
//                new Dialog_AlertNotice(context, "Error", "Something's wrong.").setPositiveKey("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        activity.finish();
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onCompleted(String result, CustomHTTP http) {
//
//        }
//    }
//
//    private class OnClick implements View.OnClickListener,AdapterView.OnItemClickListener{
//        @Override
//        public void onClick(View view) {
////            ArrayList<ObjectToken> ba = new ArrayList<>();
////            for(int ii = 0; ii< tokenss.size(); ii++){
////                if(tokenss.get(ii).checked) ba.add(tokenss.get(ii));
////            }
////            for(int ii = 0; ii< ba.size(); ii++){
////                sendMsg(data_body(true , ba.get(ii).token));
////            }
//        }
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            ObjectToken objectToken = (ObjectToken) adapterView.getItemAtPosition(i);
//            String selected_token = objectToken.token;
//            sendMsg(data_body(selected_token));
//        }
//    }
//}
