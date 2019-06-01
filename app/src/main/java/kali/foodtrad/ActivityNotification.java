package kali.foodtrad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by elliotching on 11-Nov-16.
 */
public class ActivityNotification extends AppCompatActivity {

    private Context context = this;
    private ListView listView;
//
//    private DatePickerDialog datePickerDialog;
//    private DatePickerDialog.OnDateSetListener myDateListener;
//
//    private TimePickerDialog timePickerDialog;
//    private TimePickerDialog.OnTimeSetListener myTimeListener;


    private ArrayList<NotiMsg> arrayListMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resetFCMCount();
        setContentView(R.layout.activity_notification_result);

        listView = (ListView) findViewById(R.id.list_view);
        arrayListMessage = new ArrayList<>();

        reloadAllItems();
    }

    private void reloadAllItems(){
        MyDBHandler dbHandler = new MyDBHandler(context);
        arrayListMessage = dbHandler.getAllProducts();
        listView.setAdapter(new AdapterNotificationMsgListView(context, arrayListMessage));
    }

    private boolean resetFCMCount(){
        SharedPreferences pref = getSharedPreferences("FCM", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("notification_number", 1);
        edit.commit();
        return true;
    }

//
//    protected void showDate(){
//        int y,m,d;
//        Calendar c = Calendar.getInstance();
//        y = c.get(Calendar.YEAR);
//        m = c.get(Calendar.MONTH);
//        d = c.get(Calendar.DAY_OF_MONTH);
//        myDateListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
//                // get date data here...
//            }
//        };
//        datePickerDialog = new DatePickerDialog(context, myDateListener, y, m, d);
//        datePickerDialog.show();
//    }
//
//    protected void showTime(){
//        int y,m,d;
//        Calendar c = Calendar.getInstance();
//        myTimeListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                //
//            }
//        };
//        timePickerDialog = new TimePickerDialog(context,myTimeListener,13,45,true);
//        timePickerDialog.show();
//    }


//
//    public void newProduct () {
//        MyDBHandler dbHandler = new MyDBHandler(context);
//
//        int quantity =
//                Integer.parseInt(quantityBox.getText().toString());
//
//        NotiMsg notiMsg =
//                new NotiMsg(productBox.getText().toString(), quantity);
//
//        dbHandler.addProduct(notiMsg);
//
//        /*
//         * AFTER ADD PRODUCT
//         * FOLLOWING reloadAllItems() WILL SELECT ALL ITEMS IN DB
//         * AND SHOW IN LIST VIEW
//         */
//        productBox.setText("");
//        quantityBox.setText("0");
//
//        reloadAllItems();
//    }

}