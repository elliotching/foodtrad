package kali.foodtrad;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Elliot on 19-Aug-16.
 */
public class ActivityFoodListingListViewElliot extends MyCustomActivity {

    private String TAG = this.getClass().getSimpleName();
    Context context = this;
    AppCompatActivity activity = this;
    ListView mListView;
    ListView listView;
    Toolbar mToolbar;

    private Button buttonsharedbycustomer;
    private Button buttonfoodmenulist;

    AdapterFoodListingListViewElliot adapter;

    private FusedLocationTracker locationTracker;

    private Dialog_Progress progGetAllFoods = null;

    private ArrayList<FoodListingObject> foodArray;

    private Listener allListener;
    private CustomHTTP httpGetAllFoodUser;
    private CustomHTTP httpGetAllFoodSeller;

    private final static String SELLER = "get_seller_foods_only";
    private final static String USER = "get_user_foods_only";
    private final static String ALL = "get_all_foods";
    //    private LocationListener locationListener;
//    private ItemClicked adapterClickedListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_food_listing_list_view, R.id.m_list_view_toolbar);

        changeMenu(false, true, false, false, true);
        listView = (ListView) findViewById(R.id.m_list_view);

        Log.d("FoodListing", "Context = " + context + "\n Activity.class = " + ActivityFoodListingListViewElliot.class + "this = "+ this);

        buttonsharedbycustomer = (Button) findViewById(R.id.button_sharedbycustomer);
        buttonfoodmenulist = (Button) findViewById(R.id.button_foodmenu);

        allListener = new Listener();

        buttonsharedbycustomer.setOnClickListener(allListener);
        buttonfoodmenulist.setOnClickListener(allListener);

        listView.setOnItemClickListener(allListener);

        locationTracker = new FusedLocationTracker(context, allListener);

        getAllFoodUser();
    }

    private void getAllFoodUser() {
        if(progGetAllFoods != null){
            progGetAllFoods.dismiss();
        }

        progGetAllFoods = new Dialog_Progress(activity, R.string.s_prgdialog_title_loading, R.string.s_prgdialog_loading_all_food, true);

        String[][] data = new String[][]{
                {"act", "getallfood"},
                {"mode", "mobile"}
        };
        httpGetAllFoodUser = new CustomHTTP(context, data, ResFR.URL);
        httpGetAllFoodUser.ui = allListener;
        httpGetAllFoodUser.execute();
    }

    private void getAllFoodSeller() {
        if(progGetAllFoods != null){
            progGetAllFoods.dismiss();
        }
        progGetAllFoods = new Dialog_Progress(activity, R.string.s_prgdialog_title_loading, R.string.s_prgdialog_loading_all_food, true);
    
        String[][] data = new String[][]{
                {"act", "getallfood"},
                {"mode", "mobile"}
        };
        httpGetAllFoodSeller = new CustomHTTP(context, data, ResFR.URL);
        httpGetAllFoodSeller.ui = allListener;
        httpGetAllFoodSeller.execute();
    }
//    void f(){
//
//        adapter = new AdapterFoodListingListViewElliot(context, createList());
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new ListViewOnClick());
//    }

    private class Listener implements FusedLocationDataInterface, InterfaceCustomHTTP, AdapterView.OnItemClickListener, View.OnClickListener {

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

            AdapterFoodListingListViewElliot.myLocation = new double[]{location.getLatitude(), location.getLongitude()};

            if(foodArray != null){
                refreshList(location);
            }
        }

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {
            progGetAllFoods.dismiss();

            Log.d("OnCompleteGetAllFood", result);

            if (http == httpGetAllFoodUser) {
                onCompleteGetFoodDoArrayList(result, USER);
            }
            if (http == httpGetAllFoodSeller) {
                onCompleteGetFoodDoArrayList(result, SELLER);
            }
        }


        @Override
        public void onClick(View v) {
            if(v==buttonsharedbycustomer){
                getAllFoodUser();
            }
            if(v==buttonfoodmenulist){
                getAllFoodSeller();
            }
        }
    }

    private void openSearchView(){
//        LinearLayout linearLayoutButtons = ;
//        LinearLayout linearLayoutSearch = ;
    }

    private ArrayList<FoodListingObject> sortArray(ArrayList<FoodListingObject> lists){
        Collections.sort(lists, new Comparator<FoodListingObject>() {
            @Override
            public int compare(FoodListingObject t2, FoodListingObject t1) {
                return Double.compare(t2.distanceDouble,t1.distanceDouble);
            }
        });
        return lists;
    }

    private void onCompleteGetFoodDoArrayList(String result, String get_menu_user_or_seller) {
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
                    // edited 01.05.2019
                    String date_time = json.optString("date_time");
                    String username = json.optString("user");
                    String image_file_name = json.optString("imgfname");
                    String food_name = json.optString("fdname");
                    String food_price = json.optString("fdprice");
                    String seller_location_lat = json.optString("slloclat");
                    String seller_location_lng = json.optString("slloclng");
                    String seller_name = json.optString("slname");
                    String is_seller = json.optString("isseller");
                    String food_comment = json.optString("fdcomment");



                    // Filter the food is from Seller OR User!!!
                    // when user clicked button "shared from customer" show oonly user shared for eg..
                    if(get_menu_user_or_seller.equals(USER)) {
                        if (is_seller.equals("0")) {
                            FoodListingObject food = new FoodListingObject(date_time, username, image_file_name, food_name, food_price, seller_location_lat, seller_location_lng, seller_name, is_seller, food_comment);
                            food.distanceString = ResFR.string(context, R.string.s_listview_calc_distance);
                            foodArray.add(food);
                        }
                    }else if(get_menu_user_or_seller.equals(SELLER)){
                        if (is_seller.equals("1") || is_seller.equals("2")) {
                            FoodListingObject food = new FoodListingObject(date_time, username, image_file_name, food_name, food_price, seller_location_lat, seller_location_lng, seller_name, is_seller, "");
                            food.distanceString = ResFR.string(context, R.string.s_listview_calc_distance);
                            foodArray.add(food);
                        }
                    }else{
                        FoodListingObject food = new FoodListingObject(date_time, username, image_file_name, food_name, food_price, seller_location_lat, seller_location_lng, seller_name, is_seller, food_comment);
                        food.distanceString = ResFR.string(context, R.string.s_listview_calc_distance);
                        foodArray.add(food);
                    }
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
        sortArray(foodArray);
        listView.invalidateViews();
    }

    private void populateList() {
        Log.d(TAG, "List populated. set-up.");
        sortArray(foodArray);
        adapter = new AdapterFoodListingListViewElliot(context, foodArray);
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

//        listView.setLayoutManager(new LinearLayoutManager(context));

//        listView.setOnScrollListener(new HidingScrollListener(context) {
//            @Override
//            public void onMoved(int distance) {
//                mToolbar.setTranslationY(-distance);
//            }
//
//            @Override
//            public void onShow() {
//
//            }
//
//            @Override
//            public void onHide() {
//
//            }
//        });