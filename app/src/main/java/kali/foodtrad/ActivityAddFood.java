package kali.foodtrad;

/**
 * Created by elliotching on 08-Dec-16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Elliot on 12-Aug-16.
 */
public class ActivityAddFood extends MyCustomActivity {

    Context context = this;
    AppCompatActivity activity = this;
    private static final int PICK_IMAGE_CODE = 9123;

    // pick location code
    public static final int PICK_LOC_CODE = 768;


    private EditText editTextFoodName;
    private EditText editTextFoodPrice;
    private EditText editComment;
    private EditText editShopname;
    private Button buttonPickImage;
    private Button buttonChosenLocation1;
    private ImageView image_pickedImageView;
    boolean isSeller = false;

    private CustomHTTP httpAddFood;

    private String[] arrayImage = null;
    // arrayImage[0] is FULL PATH (/sdcard/.../foordieroute-wq8123.jpg)
    // arrayImage[1] is IMAGE NAME

    private double[] pickedLocation = null;

    UploadListener uplaodListener = new UploadListener();
    Dialog_Progress dialog_progress_submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMyView(R.layout.activity_add_food, R.id.toolbar_add_food);
        changeMenu(false, true, false);

        editTextFoodPrice = (EditText) findViewById(R.id.edittext_food_price);
        editTextFoodName = (EditText) findViewById(R.id.edittext_food_name);
//        buttonChosenLocation1 = (Button) findViewById(R.id.button_choose_location);
        buttonPickImage = (Button) findViewById(R.id.button_pick);
        Button buttonSubmit = (Button) findViewById(R.id.button_submit);

        image_pickedImageView = (ImageView) findViewById(R.id.imageview_addfood);
        buttonChosenLocation1 = (Button) findViewById(R.id.button_choose_location);

//        checkBoxMobileSeller = (CheckBox) findViewById(R.id.checkbox_mobile_seller);

        LinearLayout linearLayoutFoodLocation = (LinearLayout) findViewById(R.id.linearlayout_addfoodlocation);

        editComment = (EditText) findViewById(R.id.edittext_comment);
        editShopname = (EditText) findViewById(R.id.edittext_shopname);

        String is_seller = ResFR.getPrefString(context, ResFR.IS_SELLER);
        if (is_seller.equals("1") || is_seller.equals("2")) {
            isSeller = true;
            linearLayoutFoodLocation.setVisibility(View.GONE);
        } else {
            isSeller = false;
            activity.getSupportActionBar().setTitle(R.string.s_title_toolbar_share_food);
        }


        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityPickImage.class);
                startActivityForResult(i, PICK_IMAGE_CODE);
            }
        });


        View.OnClickListener onMapClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 go to map
                Intent i = new Intent(context, ActivityMaps.class);
                startActivityForResult(i, PICK_LOC_CODE);

            }
        };
        buttonChosenLocation1.setOnClickListener(onMapClick);

//        checkBoxMobileSeller.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    // YES, I'm moving everyday.
//                    buttonChosenLocation1.setVisibility(View.GONE);
//                } else {
//                    // NO, I got static selling location.
//                    buttonChosenLocation1.setVisibility(View.VISIBLE);
//                    buttonChosenLocation2.setVisibility(View.GONE);
//                }
//            }
//        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String food_name = editTextFoodName.getText().toString();
                String food_price = editTextFoodPrice.getText().toString();
                String food_comment = editComment.getText().toString();
                String seller_name = editShopname.getText().toString();

//                        (!checkBoxMobileSeller.isChecked() && pickedLocation == null)
                if (isSeller) {
                    if (arrayImage == null || food_name.equals("") || food_price.equals("")) {
                        showDialogFillInAll();
                    } else {
                        uploadImage();
                    }
                } else {
                    if (arrayImage == null || food_name.equals("") || food_price.equals("") || pickedLocation == null || seller_name.equals("")) {
                        showDialogFillInAll();
                    } else {
                        uploadImage();
                    }
                }
            }
        });
    }

    private void showDialogFillInAll() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_please_fill_all)
                .setPositiveKey(R.string.s_dialog_btn_ok, null);
    }


// not using so far
//    private void adjustImageView(){
//        LinearLayout.LayoutParams layoutParams =
//                (LinearLayout.LayoutParams) pickedImage.getLayoutParams();
//
//        DisplayMetrics d = context.getResources().getDisplayMetrics();
//        // w_dp = 200, means i want 200 dp
//        float w_dp = 200; //200dp
//        float h_dp = w_dp * 3 / 4;
//        int w = Screen.getPixels(context, w_dp);
//        int h = Screen.getPixels(context, h_dp);
//
//        layoutParams.width = w;
//        layoutParams.height = h;
//        layoutParams.gravity = Gravity.CENTER;
//
//        pickedImage.setLayoutParams(layoutParams);
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // return from pick image activity
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            arrayImage = data.getExtras().getStringArray("filenamearray");
            if (arrayImage != null) {
                buttonPickImage.setText(arrayImage[1]);
                createBitmapDisplayOnImageView(image_pickedImageView, arrayImage[2]);
            }
        } else if (requestCode == PICK_IMAGE_CODE) {
            if (arrayImage == null) {
                setButtonDefaultTextNoImageChoosen(buttonPickImage);
            }
        }


        // return from map activity
        if (resultCode == RESULT_OK && requestCode == PICK_LOC_CODE) {
            pickedLocation = data.getExtras().getDoubleArray(ResFR.INTENT_ACTIVITY_RESULT_PUT_EXTRA_LOCATION_KEY);
            if (pickedLocation == null) {
                setButtonDefaultTextNoLocationChoosen(buttonChosenLocation1);
            } else {
                Log.d("Elliot", "returned Location success.");
                buttonChosenLocation1.setText("" + stringOf(pickedLocation[0]) + " , " + stringOf(pickedLocation[1]));
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == PICK_LOC_CODE) {
            if (pickedLocation == null) {
                setButtonDefaultTextNoLocationChoosen(buttonChosenLocation1);
            }
        }
    }

    private void setButtonDefaultTextNoImageChoosen(Button s) {
        s.setText(R.string.s_button_no_image_choosen);
    }

    private void setButtonDefaultTextNoLocationChoosen(Button s) {
        s.setText(R.string.s_button_no_location_choosen);
    }


    // When ImageUpload button is clicked
    public void uploadImage() {

        dialog_progress_submit = new Dialog_Progress(activity, R.string.s_prgdialog_title_upload, R.string.s_prgdialog_uploading, false);
        // When Image is selected from Gallery

        // arrayImage
        // from onActivityResult
        if (arrayImage != null) {
            String imgPath = arrayImage[0];
            String filename = arrayImage[1];
            if (imgPath != null && !imgPath.isEmpty()) {



                // Convert image to String using Base64
                ImageUpload.encodeImagetoString(context, imgPath, filename, uplaodListener);
                // When Image is not selected from Gallery
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Can't found image",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class UploadListener implements InterfaceUploadListener {
        @Override
        public void onFinishedUpload() {
            // when upload onFinishedUpload
            // when upload finish
            // do submit food data.


            deleteFoodieImage(arrayImage[0]);

            dialog_progress_submit.setTitle(R.string.s_prgdialog_title_submit_food);
            dialog_progress_submit.setMessage(R.string.s_prgdialog_submitting_food);

            // get image from array instead of button text
            String image_file_name = arrayImage[1];
            String food_name = editTextFoodName.getText().toString();
            String food_price = editTextFoodPrice.getText().toString();

            String food_comment = editComment.getText().toString();
            String seller_location_lat = "";
            String seller_location_lng = "";
            String seller_name = editShopname.getText().toString();
            if (isSeller) {
                seller_location_lat = ResFR.getPrefLocationLat(context).equals(ResFR.DEFAULT_EMPTY) ? "" : ResFR.getPrefLocationLat(context);
                seller_location_lng = ResFR.getPrefLocationLng(context).equals(ResFR.DEFAULT_EMPTY) ? "" : ResFR.getPrefLocationLng(context);
                seller_name = ResFR.getPrefString(context, ResFR.SELLER_NAME).equals(ResFR.DEFAULT_EMPTY) ? "" : ResFR.getPrefString(context, ResFR.SELLER_NAME);
            } else {
                if (pickedLocation != null) {
                    seller_location_lat = stringOf(pickedLocation[0]);
                    seller_location_lng = stringOf(pickedLocation[1]);
                }
            }
            String username = ResFR.getPrefString(context, ResFR.USERNAME);
            String is_seller = ResFR.getPrefString(context, ResFR.IS_SELLER);

            String[][] data = new String[][]{
                    {"act", "addfood"},
                    {"mode","mobile"},
                    {"user", username},
                    {"imgfname", image_file_name},
                    {"fdname", food_name},
                    {"fdprice", food_price},
                    {"slloclat", seller_location_lat},
                    {"slloclng", seller_location_lng},
                    {"slname", seller_name},
                    {"isseller", is_seller},
                    {"fdcomment", food_comment}
            };


            httpAddFood = new CustomHTTP(context, data, ResFR.URL);
            httpAddFood.ui = new ConnectionListener();
            httpAddFood.execute();
        }
    }

    private class ConnectionListener implements InterfaceCustomHTTP{

        @Override
        public void onCompleted(String result) {

        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {
            if(http == httpAddFood){
                dialog_progress_submit.dismiss();
                try {
                    JSONObject json = new JSONObject(result);
                    String success = json.optString("success", "");

                    if (success.equals("1")) {
                        showDialogSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    showDialogError(result);

                }
            }
        }
    }

    private void showDialogError(String result) {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_error, result).setPositiveKey(R.string.s_dialog_btn_ok, null);
    }

    private void showDialogSuccess() {
        new Dialog_AlertNotice(context, R.string.s_dialog_title_success, R.string.s_dialog_add_food_success).setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
    }

    private boolean deleteFoodieImage(String fullPathFileString){
        File fdelete = new File(fullPathFileString);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                return true;
            } else {
                System.out.println("file not Deleted :" + fullPathFileString);
                return false;
            }
        }
        return false;
    }

}
