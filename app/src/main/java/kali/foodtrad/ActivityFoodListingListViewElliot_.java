package kali.foodtrad;//package kali.foodtrad;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
///**
// * Created by Elliot on 19-Aug-16.
// */
//class ActivityFoodListingListViewElliot_ extends AppCompatActivity {
//
//    Context context = this;
//    AppCompatActivity mActivity = this;
//    ListView mListView;
//    ListView listView;
//    Toolbar mToolbar;
//    AdapterFoodListingListViewElliot mAdapter;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        new UniversalLayoutInitToolbarAndTheme(this, R.layout.activity_food_listing_list_view, R.id.m_list_view_toolbar, true);
//        listView = (ListView) findViewById(R.id.m_list_view);
//        mAdapter = new AdapterFoodListingListViewElliot(context, createList());
//        listView.setAdapter(mAdapter);
//
//        listView.setOnItemClickListener(new ListViewOnClick());
//    }
//
//
//    class ListViewOnClick implements AdapterView.OnItemClickListener {
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            FoodListingObject f = (FoodListingObject) listView.getItemAtPosition(i);
//            Intent intent = new Intent(context, ActivityFoodDetail.class);
//            intent.putExtra("FoodListingObjectText", f.foodName);
//            intent.putExtra("FoodListingObjectImage", f.photoName);
//            intent.putExtra("FoodListingObjectPrice", f.foodPrice);
//            startActivity(intent);
//        }
//    }
//
//
//
//    private ArrayList<FoodListingObject> createList(int count) {
//        ArrayList<FoodListingObject> arrayList = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
////            arrayList.add(new FoodListingObject("Item " + i, i % 2 == 0 ? R.color.c_yellow : R.color.c_light_cyan));
//        }
//        return arrayList;
//    }
//
//    private ArrayList<FoodListingObject> createList() {
//        ArrayList<FoodListingObject> arrayList = new ArrayList<>();
////        for (int i = 0; i < 5; i++) {
//        arrayList.add(new FoodListingObject("KoloMee", "foordieroute-1488038128659.jpg", "3.50"));
//        arrayList.add(new FoodListingObject("Kampua", "foordieroute-1490412218180.jpg", "3.50"));
//        arrayList.add(new FoodListingObject("Tang Hung Soup", "foordieroute-1490412792086.jpg", "4.00"));
//        arrayList.add(new FoodListingObject("Sarawak Laksa", "foordieroute-1490412523398.jpg", "5.00"));
//        arrayList.add(new FoodListingObject("Sarawak Kompia", "foordieroute-1490414425737.jpg", "2.50"));
////        }
//        return arrayList;
//    }
//
//
//}
//
////        listView.setLayoutManager(new LinearLayoutManager(context));
//
////        listView.setOnScrollListener(new HidingScrollListener(context) {
////            @Override
////            public void onMoved(int distance) {
////                mToolbar.setTranslationY(-distance);
////            }
////
////            @Override
////            public void onShow() {
////
////            }
////
////            @Override
////            public void onHide() {
////
////            }
////        });