package kali.foodtrad;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elliotching on 15-Mar-17.
 */

class CustomHTTP extends AsyncTask<String, Void, String>
{
    Context context;
    private static final int CONNECTION_TIMEOUT=0;// 0 = infinity
    private static final int READ_TIMEOUT=0;// 0 = infinity
//    ProgressDialog pdLoading;

    private String FAIL_CONNECT;
    private String NOT_FOUND;
//    some of the others attributes...
//    private String CONTENT_TYPE_K = "Content-Type";
//    private String CONTENT_TYPE_V = "application/json";
//    private String AUTHORIZATION_K = "Authorization";
//    private String AUTHORIZATION_V = "key=AAAAwbp2NJ0:APA91bHxLB7W_cnedvCwdcFI_-ji4bbljFzlM.....";

    HttpURLConnection conn;
    private URL url = null;
    private final String urlString;

    InterfaceCustomHTTP ui=null;

    String[][] keyvalues=null;


    CustomHTTP(Context c, String[][] keyvalue, String URL){
        context = c;
        this.keyvalues = keyvalue;
        this.urlString = URL;
        FAIL_CONNECT = ResFR.string(context, R.string.s_dialog_msg_failedconnect);
        NOT_FOUND = ResFR.string(context, R.string.s_dialog_msg_connection_not_found);
    }
    // DO OPEN HTTP CONNECTIONS AND MAKE POST REQUEST etc.

    @Override
    protected String doInBackground(String... params) {
        try {

            // Enter URL address where your php file resides
            url = new URL(urlString);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return "exception";
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

//            conn.setRequestProperty(CONTENT_TYPE_K, CONTENT_TYPE_V);
//            conn.setRequestProperty(AUTHORIZATION_K, AUTHORIZATION_V);

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String query = "";
            if(keyvalues != null) {
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder();
                for (int i = 0; i < keyvalues.length; i++) {
                    builder.appendQueryParameter(keyvalues[i][0], keyvalues[i][1]);
                }
                query = builder.build().getEncodedQuery();
            }

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            System.err.println("elliot: failed URL");
            e1.printStackTrace();
            return FAIL_CONNECT;
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                int line ;

                while ((line = reader.read()) != -1) {
                    result.append((char)line);
                }

                // Pass data to onPostExecute method
                return(result.toString());

            }else{
                return(NOT_FOUND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return FAIL_CONNECT;
        } finally {
            conn.disconnect();
        }


    }

    @Override
    protected void onPostExecute(String result) {

//        result = result.replace("<br />", "\n");
//        result = result.replace("<br>", "\n");
//        result = result.replace("<b>", "*");
//        result = result.replace("</b>", "*");

        //to unescape UTF-8 Unicode Character, i.e. convert '\u5c3d' to '尽'.
//        result = StringEscapeUtils.unescapeJava(result);

        //this method will be running on UI thread
        if(ui!=null){
            ui.onCompleted(result);
            ui.onCompleted(result, this);
        }
        Log.d(this.getClass().getSimpleName(), result);
    }



//    private class CreateJSON{
//        JSONObject json;
//        CreateJSON(){
//            json = new JSONObject();
//        }
//        public CreateJSON putJSON(String k, String v){
//            try {
//                json.put(k, v);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//        @Override
//        public String toString() {
//            json.toString();
//            return super.toString();
//        }
//    }

}
