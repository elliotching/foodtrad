package kali.foodtrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elliot on 19-Aug-16.
 */
public class ActivityFoodMenu extends MyCustomActivity {

    private String TAG = this.getClass().getSimpleName();
    Context context = this;
    AppCompatActivity activity = this;
    ListView listView;
    AdapterFoodMenu adapter;
    private Dialog_CustomNotice dialog;
    private Dialog_AlertNotice alertDialog;
    private Dialog_Progress progDeleteFood;

    private ListView dialogDeleteListView;

    private FusedLocationTracker locationTracker;

    private Dialog_Progress progGetAllFoods = null;

    private ArrayList<FoodListingObject> foodArray;

    private static FoodListingObject staticFood = null;

    private Listener allListener;
    private CustomHTTP httpGetAllMyFood;
    private CustomHTTP httpDelete;
//
//    private final static String SELLER = "get_seller_foods_only";
//    private final static String USER = "get_user_foods_only";
//    private final static String ALL = "get_all_foods";
    //    private LocationListener locationListener;
//    private ItemClicked adapterClickedListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_food_menu, R.id.toolbar);

        changeMenu(false, true, false, false, false);
        listView = (ListView) findViewById(R.id.listview);

        allListener = new Listener();

        listView.setOnItemClickListener(allListener);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodListingObject food = (FoodListingObject) adapter.getItem(i);
                showDialogListToDelete(food);
                return true;
            }
        });

        startGetAllMyFood();
    }

    private void startGetAllMyFood() {
        if(progGetAllFoods != null){
            progGetAllFoods.dismiss();
        }
        progGetAllFoods = new Dialog_Progress(activity, R.string.s_prgdialog_title_loading, R.string.s_prgdialog_loading_all_food, false);

        String username = ResFR.getPrefString(context, ResFR.USERNAME);

        String[][] data = new String[][]{
                {"act", "getsellermenu"},
                {"mode", "mobile"},
                {"user", username}
        };
        httpGetAllMyFood = new CustomHTTP(context, data, ResFR.URL);
        httpGetAllMyFood.ui = allListener;
        httpGetAllMyFood.execute();
    }
//    void f(){
//
//        adapter = new AdapterFoodListingListViewElliot(context, createList());
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new ListViewOnClick());
//    }

    private class Listener implements  InterfaceCustomHTTP, AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if(parent == dialogDeleteListView){
//                showDialogConfirmDelete(foodArray.get(position));
//            }
//            else {
                FoodListingObject food = (FoodListingObject) adapter.getItem(position);
                ActivityFoodDetail.viewingFood = food;
                Intent i = new Intent(context, ActivityFoodDetail.class);
                startActivity(i);
//            }
        }

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {


            if (http == httpGetAllMyFood) {
                progGetAllFoods.dismiss();

                Log.d("OnCompleteGetAllFood", result);
                onCompleteGetFoodDoArrayList(result);
            }
// && httpDelete != null
            if(http == httpDelete){
                dialog.dismiss();
                progDeleteFood.dismiss();
                if(isJSONObject(result)){
                    try {
                        JSONObject json = new JSONObject(result);
                        String success = json.optString("success");
                        if(success.equals("1")){
                            startGetAllMyFood();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void createListViewDelete(View v, Dialog_CustomNotice d) {
        // after food long pressed / clicked...

        dialogDeleteListView = (ListView) v.findViewById(R.id.listview_deletefooddialog);
        dialogDeleteListView.setAdapter(new Adapter());
        dialogDeleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialogConfirmDelete(staticFood);
            }
        });
    }

    private void showDialogListToDelete(FoodListingObject food) {
        staticFood = food;
        dialog = new Dialog_CustomNotice(context, R.string.s_dialog_title_delete, R.layout.dialog_menu_edit_delete, new InterfaceDialog() {
            @Override
            public void onCreateDialogView(View v, Dialog_CustomNotice d) {
                createListViewDelete(v, d);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showDialogConfirmDelete(FoodListingObject food) {
        alertDialog = new Dialog_AlertNotice(context, R.string.s_dialog_title_delete, R.string.s_dialog_msg_confirmdelete)
        .setPositiveKey(R.string.s_dialog_btn_ok, clickToPerformDelete(), false)
        .setNegativeKey(R.string.s_dialog_btn_cancel, null, true);
    }

    private DialogInterface.OnClickListener clickToPerformDelete() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doHttpDelete();
            }
        };
    }

    private void doHttpDelete() {
        if(progDeleteFood != null) {
            progDeleteFood.dismiss();
        }
        progDeleteFood = new Dialog_Progress(activity, R.string.s_dialog_title_delete, R.string.s_prgdialog_deleting_food, true);

        String[][] data = new String[][]{
                {"mode", "mobile"},
                {"act", "delete"}
//                {"datetime", staticFood.date_time_raw}
        };

        httpDelete = new CustomHTTP(context, data, ResFR.URL);
        httpDelete.ui = allListener;
        httpDelete.execute();
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

                    FoodListingObject food = new FoodListingObject(date_time, username, image_file_name, food_name, food_price, seller_location_lat, seller_location_lng, seller_name, is_seller, food_comment);
                    food.distanceString = "";
                    foodArray.add(food);
                }

                populateList();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ((result).equals("")) {
            showDialogMenuIsEmpty(/*R.string.s_dialog_msg_menuisempty*/);
        } else {
            showDialogError(result);
        }
    }

    private void showDialogMenuIsEmpty() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_menuisempty).setPositiveKey(R.string.s_dialog_btn_ok, clickToEndActivity());
    }

    private void showDialogError(String result) {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, result).setPositiveKey(R.string.s_dialog_btn_ok, clickToEndActivity());
    }

    private class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int i) {
            return ResFR.string(context, R.string.s_deletethisfood);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            Log.d("FoodListView", "position = "+position+" is viewed");
            TextView tv;

            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null/* parent */);
            tv = (TextView) view.findViewById(android.R.id.text1);

            tv.setText(R.string.s_deletethisfood);

            return view;
        }
    }
//
//    private void refreshList(Location location) {
//        Log.d(TAG, "List refreshing");
//
//        for(int i = 0; i < foodArray.size(); i++) {
//            updateFoodDistanceStringAndDouble(foodArray.get(i), location.getLatitude(), location.getLongitude());
//        }
//
//        listView.invalidateViews();
//    }

    private void populateList() {
        Log.d(TAG, "List populated. set-up.");
        adapter = new AdapterFoodMenu(context, foodArray);
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