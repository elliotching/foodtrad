package kali.foodtrad;//package kali.foodtrad;
//
///**
// * Created by elliotching on 08-Dec-16.
// */
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Elliot on 12-Aug-16.
// */
//public class ActivityAddFood_ extends AppCompatActivity {
//
//    Context context = this;
//    AppCompatActivity activity = this;
//    private static final int PICK_IMAGE_CODE = 9123;
//
//    // pick location code
//    public static final int PICK_LOC_CODE = 768;
//
//
//    private EditText editTextFoodName;
//    private EditText editTextFoodPrice;
//    private Button buttonPickImage;
//    private Button buttonChosenLocation1;
//    private Button buttonChosenLocation2;
//    private Button buttonSubmit;
//    private CheckBox checkBoxMobileSeller;
//
//
//    private String[] arrayImage = null;
//    // arrayImage[0] is FULL PATH (/sdcard/.../foordieroute-wq8123.jpg)
//    // arrayImage[1] is IMAGE NAME
//
//    private double[] pickedLocation = null;
//
//    UploadListener uplaodListener = new UploadListener();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        new UniversalLayoutInitToolbarAndTheme(this, R.layout.activity_add_food, R.id.toolbar_add_food, true);
//
//        editTextFoodPrice = (EditText) findViewById(R.id.edittext_food_price);
//        editTextFoodName = (EditText) findViewById(R.id.edittext_food_name);
////        buttonChosenLocation1 = (Button) findViewById(R.id.button_choose_location);
//        buttonPickImage = (Button) findViewById(R.id.button_pick);
//        buttonSubmit = (Button) findViewById(R.id.button_submit);
//
//
//        buttonChosenLocation1 = (Button) findViewById(R.id.button_choose_location);
//        buttonChosenLocation2 = (Button) findViewById(R.id.button_choose_location_2);
//
//        checkBoxMobileSeller = (CheckBox) findViewById(R.id.checkbox_mobile_seller);
//
//        buttonChosenLocation1.setVisibility(View.VISIBLE);
//        buttonChosenLocation2.setVisibility(View.GONE);
//
//        buttonPickImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, ActivityPickImage.class);
//                startActivityForResult(i, PICK_IMAGE_CODE);
//            }
//        });
//
//
//        View.OnClickListener onMapClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                 go to map
//                Intent i = new Intent(context, ActivityMaps.class);
//                startActivityForResult(i, PICK_LOC_CODE);
//
//            }
//        };
//        buttonChosenLocation1.setOnClickListener(onMapClick);
//
//        checkBoxMobileSeller.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    // YES, I'm moving everyday.
//                    buttonChosenLocation1.setVisibility(View.GONE);
//                    buttonChosenLocation2.setVisibility(View.VISIBLE);
//                } else {
//                    // NO, I got static selling location.
//                    buttonChosenLocation1.setVisibility(View.VISIBLE);
//                    buttonChosenLocation2.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String food_name = editTextFoodName.getText().toString();
//                String food_price = editTextFoodPrice.getText().toString();
//
//                if(arrayImage == null || food_name.equals("") || food_price.equals("") ||
//                        (!checkBoxMobileSeller.isChecked() && pickedLocation == null)){
//                    new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_please_fill_all)
//                            .setPositiveKey(R.string.s_dialog_btn_ok, null);
//                }
//                else {
//                    dialog_progress_submit = new Dialog_Progress(activity, R.string.s_prgdialog_title_upload, R.string.s_prgdialog_uploading, false);
//                    uploadImage();
//                }
//            }
//        });
//    }
//
//    Dialog_Progress dialog_progress_submit;
//
//// not using so far
////    private void adjustImageView(){
////        LinearLayout.LayoutParams layoutParams =
////                (LinearLayout.LayoutParams) pickedImage.getLayoutParams();
////
////        DisplayMetrics d = context.getResources().getDisplayMetrics();
////        // w_dp = 200, means i want 200 dp
////        float w_dp = 200; //200dp
////        float h_dp = w_dp * 3 / 4;
////        int w = Screen.getPixels(context, w_dp);
////        int h = Screen.getPixels(context, h_dp);
////
////        layoutParams.width = w;
////        layoutParams.height = h;
////        layoutParams.gravity = Gravity.CENTER;
////
////        pickedImage.setLayoutParams(layoutParams);
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // return from pick image activity
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE) {
//            arrayImage = data.getExtras().getStringArray("filenamearray");
//            if (arrayImage != null) {
//                buttonPickImage.setText(arrayImage[1]);
//            }
//        } else if (requestCode == PICK_IMAGE_CODE) {
//            if (arrayImage == null) {
//                setButtonDefaultTextNoImageChoosen(buttonPickImage);
//            }
//        }
//
//
//        // return from map activity
//        if (resultCode == RESULT_OK && requestCode == PICK_LOC_CODE) {
//            pickedLocation = data.getExtras().getDoubleArray("savedlocation");
//            if (pickedLocation == null) {
//                setButtonDefaultTextNoLocationChoosen(buttonChosenLocation1);
//            } else {
//                Log.d("Elliot", "returned Location success.");
//                buttonChosenLocation1.setText("" + String.format("%.10f", pickedLocation[0]) + " , " +
//                        String.format("%.10f", pickedLocation[1]));
//            }
//        } else if (resultCode == RESULT_CANCELED && requestCode == PICK_LOC_CODE) {
//            if (pickedLocation == null) {
//                setButtonDefaultTextNoLocationChoosen(buttonChosenLocation1);
//            }
//        }
//    }
//
//    private void setButtonDefaultTextNoImageChoosen(Button s) {
//        s.setText(R.string.s_button_no_image_choosen);
//    }
//
//    private void setButtonDefaultTextNoLocationChoosen(Button s) {
//        s.setText(R.string.s_button_no_location_choosen);
//    }
//
//
//    // When ImageUpload button is clicked
//    void uploadImage() {
//        // When Image is selected from Gallery
//
//        // arrayImage
//        // from onActivityResult
//        if (arrayImage != null) {
//            String imgPath = arrayImage[0];
//            String filename = arrayImage[1];
//            if (imgPath != null && !imgPath.isEmpty()) {
//
//                // Convert image to String using Base64
//                ImageUpload.encodeImagetoString(context, imgPath, filename, uplaodListener);
//                // When Image is not selected from Gallery
//            } else {
//                Toast.makeText(
//                        getApplicationContext(),
//                        "Can't found image",
//                        Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    "You must select image from gallery before you try to upload",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//
//        finish();
//        return super.onSupportNavigateUp();
//    }
//
//
//    private class UploadListener implements InterfaceUploadListener {
//        @Override
//        public void onFinishedUpload() {
//            // when upload onFinishedUpload
//            // when upload finish
//            // do submit food data.
//
//            dialog_progress_submit.setTitle(R.string.s_prgdialog_title_submit_food);
//            dialog_progress_submit.setMessage(R.string.s_prgdialog_submitting_food);
//
//            // get image from array instead of button text
//            String image_file_name = arrayImage[1];
//            String food_name = editTextFoodName.getText().toString();
//            String food_price = editTextFoodPrice.getText().toString();
//            String seller_location_lat = "";
//            String seller_location_lng = "";
//            if(!checkBoxMobileSeller.isChecked()) {
//                seller_location_lat = String.format("%.10f", pickedLocation[0]);
//                seller_location_lng = String.format("%.10f", pickedLocation[1]);
//            }
//            String username = ResFR.getPrefString(context, ResFR.USERNAME);
//
//            String[][] data = new String[][]{
//                    {"pass", "!@#$"},
//                    {"username", username},
//                    {"image_file_name", image_file_name},
//                    {"food_name", food_name},
//                    {"food_price", food_price},
//                    {"seller_location_lat", seller_location_lat},
//                    {"seller_location_lng", seller_location_lng}
//            };
//
//
//            CustomHTTP c = new CustomHTTP(context, data, ResFR.URL_add_food);
//            c.ui = new InterfaceCustomHTTP() {
//                @Override
//                public void onCompleted(String result) {
//                    dialog_progress_submit.dismiss();
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        String success = json.optString("success", "");
//
//                        if(success.equals("1")){
//                            new Dialog_AlertNotice(context, R.string.s_dialog_title_success, R.string.s_dialog_add_food_success)
//                                    .setPositiveKey(R.string.s_dialog_btn_ok, null);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                        new Dialog_AlertNotice(context, "Error", result)
//                                .setPositiveKey(R.string.s_dialog_btn_ok, null);
//
//                    }
//                }
//
//                @Override
//                public void onCompleted(String result, CustomHTTP customHTTP) {
//
//                }
//            };
//
//            c.execute();
//        }
//    }
//}
