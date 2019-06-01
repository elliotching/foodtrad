package kali.foodtrad;//package kali.foodtrad;
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import cz.msebera.android.httpclient.Header;
//
//
///**
// * Created by elliotching on 19-Feb-17.
// */
//
//class AsyncHTTPPost {
//
//    private RequestParams params;
////    RequestParams params = new RequestParams();
//    private final String URL;
//    protected static InterfaceAsyncHTTPPost ui_thread = null;
//
//    AsyncHTTPPost(String url, RequestParams p, boolean returnJSON, InterfaceAsyncHTTPPost s) {
//        URL = url;
//        params = p;
//        ui_thread = s;
//        if (!returnJSON) makeHTTPCall();
//        else makeHTTPReturnJSON();
//    }
//
//    // Make Http call to upload Image to Php server
//    private void makeHTTPCall() {
//        final AsyncHttpClient client = new AsyncHttpClient();
//        // Don't forget to change the IP address to your LAN address. Port no as well.
//        client.post(URL, params, new MyAsyncHttpRespondHandler());
//    }
//
//    private void makeHTTPReturnJSON() {
//        final AsyncHttpClient client = new AsyncHttpClient();
//        // Don't forget to change the IP address to your LAN address. Port no as well.
//        client.post(URL, params, new MyJsonHttpRespondHandler());
//    }
//
//
//    private class MyAsyncHttpRespondHandler extends AsyncHttpResponseHandler {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            if (statusCode == 200) {
//                // Hide Progress Dialog
//                String echoResult = new String(responseBody);
//                ui_thread.notifyHTTPSuccess(statusCode, echoResult);
//                System.out.println("onCompleted: " + echoResult);
//            }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            // When Http response code is '404'
//            if (statusCode == 404) {
//                ui_thread.notifyHTTPFailure(statusCode,"Requested resource not found");
//            }
//
//            // When Http response code is '500'
//            else if (statusCode == 500) {
//                ui_thread.notifyHTTPFailure(statusCode,"Something went wrong at server end");
//            }
//
//            // When Http response code other than 404, 500
//            else {
//                ui_thread.notifyHTTPFailure(statusCode, "Error Occured n Most Common Error: \n" +
//                        "1. Device not connected to Internet\n" +
//                        "2. Web App is not deployed in App server\n" +
//                        "3. App server is not running\n"+
//                        "HTTP Status code : " + statusCode);
//            }
//        }
//    }
//
//    private static class MyJsonHttpRespondHandler extends JsonHttpResponseHandler {
//
//        MyJsonHttpRespondHandler() {
//            super();
//        }
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//            super.onSuccess(statusCode, headers, response);
//            ArrayList<JSONObject> arrayList = new ArrayList<>();
//            try {
//                for (int c = 0; c < response.length(); c++) {
//                    arrayList.add(response.getJSONObject(c));
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("elliot: "+response);
//            ui_thread.notifyJSONArraySuccess(response);
//            System.out.println(response);
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//            ui_thread.notifyHTTPFailure(statusCode, responseString);
//            System.out.println(responseString);
//        }
//    }
//}
