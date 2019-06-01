package kali.foodtrad;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by elliotching on 23-Feb-17.
 */

class AsyncRequestData {

    Context context;

    URL url = null;
    private static final String urlString = "http://foodlocator.com.my/mobile/test_s.php";
    static InterfaceAsyncRequestData activityRequestData = null;
    static AsyncHttpClient client;

    public static void makeHTTPCall(Context context, String dialogMsg, RequestParams postParams) {

        client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(urlString, postParams, new MyJsonHttpRespondHandler());
    }

    public static void cancelRequest(){
        client.cancelAllRequests(true);
    }

    private static class MyJsonHttpRespondHandler extends JsonHttpResponseHandler {

        MyJsonHttpRespondHandler() {
            super();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            ArrayList<JSONObject> arrayList = new ArrayList<>();
            try {
                for (int c = 0; c < response.length(); c++) {
                    arrayList.add(response.getJSONObject(c));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
//            progressDialog.dismiss();
            activityRequestData.getArrayListJSONResult(arrayList);
        }

    }
}
