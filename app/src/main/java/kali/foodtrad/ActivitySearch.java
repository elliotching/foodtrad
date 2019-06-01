package kali.foodtrad;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elliot on 19-Aug-16.
 */
public class ActivitySearch extends MyCustomActivity {

    private String TAG = this.getClass().getSimpleName();

    private EditText editSearch;
    private Listener listener = null;
    private ArrayList<FoodListingObject> foodArray;

    private ListView listView;
    private AdapterSearch adapter;
    private Dialog_Progress progRunningSearch;
    private CustomHTTP httpSearch = null;

    private FusedLocationTracker locationTracker;
//    private ItemClicked adapterClickedListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_search, R.id.toolbar);

        listener = new Listener();
        editSearch = (EditText) findViewById(R.id.edit_search);
        editSearch.setOnEditorActionListener(listener);

        listView = (ListView) findViewById(R.id.list_view);

        locationTracker = new FusedLocationTracker(context, listener);
    }

    private class Listener implements FusedLocationDataInterface, InterfaceCustomHTTP, AdapterView.OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FoodListingObject food = (FoodListingObject) adapter.getItem(position);
            ActivityFoodDetail.viewingFood = food;
            Intent i = new Intent(context, ActivityFoodDetail.class);
//            locationTracker.stopLocationUpdates();
            startActivity(i);
        }

        @Override
        public void getFusedLocationData(Location location) {
            Log.d(TAG, "fusedLocationChanged.");

            AdapterSearch.myLocation = new double[]{location.getLatitude(), location.getLongitude()};

            if(foodArray != null){
                refreshList(location);
            }
        }

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {
            if(progRunningSearch != null){
                progRunningSearch.dismiss();
                progRunningSearch = null;
            }

            Log.d("OnCompleteGetAllFood", result);

            if (http == httpSearch) {
                onCompleteGetFoodDoArrayList(result);
            }
        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(v==editSearch ){

                fireSearch();
                return true;
            }
            else{
                Log.d("actionID", String.valueOf(actionId));
            }
            return false;
        }
    }

    private void fireSearch() {
        if(progRunningSearch != null){
            progRunningSearch.dismiss();
            progRunningSearch = null;
        }
        progRunningSearch = new Dialog_Progress(activity, R.string.s_prgdialog_title_search, R.string.s_prgdialog_searching, true);

        String searchKey = editSearch.getText().toString();
        String[][] data = new String[][]{
                {"act", "search"},
                {"key", searchKey}
        };

        httpSearch = new CustomHTTP(context, data, ResFR.URL);
        httpSearch.ui = listener;
        httpSearch.execute();
    }

    private void onCompleteGetFoodDoArrayList(String result) {
        if (isJSONArray(result)) {
            try {

                foodArray = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
//                    String date_time = json.optString("date_time");
//                    String username = json.optString("username");
//                    String image_file_name = json.optString("image_file_name");
//                    String food_name = json.optString("food_name");
//                    String food_price = json.optString("food_price");
//                    String seller_location_lat = json.optString("seller_location_lat");
//                    String seller_location_lng = json.optString("seller_location_lng");
//                    String seller_name = json.optString("seller_name");
//                    String is_seller = json.optString("is_seller");
//                    String food_comment = json.optString("food_comment");
                    
                    //edited 01.05.2019
                    String date_time = json.optString("date_time");
                    String username = json.optString("username");
                    String image_file_name = json.optString("image_file_name");
                    String food_name = json.optString("food_name");
                    String food_price = json.optString("food_price");
                    String seller_location_lat = json.optString("seller_location_lat");
                    String seller_location_lng = json.optString("seller_location_lng");
                    String seller_name = json.optString("seller_name");
                    String is_seller = json.optString("is_seller");
                    String food_comment = json.optString("food_comment");

                    FoodListingObject food = new FoodListingObject(date_time, username, image_file_name, food_name, food_price, seller_location_lat, seller_location_lng, seller_name, is_seller, food_comment);
                    food.distanceString = ResFR.string(context, R.string.s_listview_calc_distance);
                    foodArray.add(food);
                }

                populateList();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (isJSONObject(result)) {
            showDialogError(result);
        } else {
            showDialogError(result);
        }
    }

    private void showDialogError(String result) {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, result).setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    private void refreshList(Location location) {
        Log.d(TAG, "List refreshing");

        for(int i = 0; i < foodArray.size(); i++) {
            updateFoodDistanceStringAndDouble(foodArray.get(i), location.getLatitude(), location.getLongitude());
        }

        listView.invalidateViews();
    }

    private void populateList() {
        Log.d(TAG, "List populated. set-up.");
        adapter = new AdapterSearch(context, foodArray);
        listView.setAdapter(adapter);
    }

    boolean isJSONObject(String result) {
        try {
            JSONObject json = new JSONObject(result);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    boolean isJSONArray(String result) {
        try {
            JSONArray json = new JSONArray(result);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    void backButtonPressed(){
        locationTracker.stopLocationUpdates();
        super.backButtonPressed();
    }
}