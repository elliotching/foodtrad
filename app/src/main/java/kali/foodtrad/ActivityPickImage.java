package kali.foodtrad;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.theartofdev.edmodo.cropper.CropImage.getGalleryIntents;


/**
 * USING "ARTHUR_HUB" library
 * <p>
 * https://github.com/ArthurHub/Android-Image-Cropper
 * <p>
 * dependency compile 'com.theartofdev.edmodo:android-image-cropper:2.3.+'
 */
public class ActivityPickImage extends MyCustomActivity {

    Button mButtonCapture, mButtonPick, mButtonSave;
    TextView textInfoImage;

    Context context = this;
    AppCompatActivity activity = this;
    String[] selectedImageFilePath;
    String username;

    private static final int GALLERY_INTENT = 101;
    private static final int CAMERA_INTENT = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMyView(R.layout.activity_pick_image, R.id.toolbar);

        // check if user logged in
        if (!userHasLoggedIn()) {
            new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_not_logged_in)
                    .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
        }

        checkStoragePermission();

        mButtonCapture = (Button) findViewById(R.id.button_capture);
        mButtonPick = (Button) findViewById(R.id.button_pick_content);
        mButtonSave = (Button) findViewById(R.id.button_upload);

        mButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(cameraIntent(), CAMERA_INTENT);
            }
        });

        mButtonPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryIntent(), GALLERY_INTENT);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmImage();
            }
        });

        textInfoImage = (TextView) findViewById(R.id.text_image_info);

    }

    private boolean userHasLoggedIn() {
        // get username
        SharedPreferences pref = context.getSharedPreferences("FoodieRoute", Context.MODE_PRIVATE);
        String username = pref.getString("FR_username", "empty");

        if (username.equals("empty")) {
            return false;
        } else {
            this.username = username;
            return true;
        }
    }

    private static final int PERMISSION = 123;

    private void checkStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                // Request Location Permission
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_READ_PERMISSION);
//            }
//
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // Request Location Permission
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{}, STORAGE_WRITE_PERMISSION);
//            }


            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request Location Permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        // check all grantedResults from that array**
        for (int i = 0; i < grantResults.length; i++) {
                Log.d("onReqPermissionsResult", "grantResults[" + i + "]= " + grantResults[i] + " , Granted = " + PackageManager.PERMISSION_GRANTED);
        }

    }

    private void confirmImage() {

        // selectedImageFilePath FROM OnActivityResult

        if (selectedImageFilePath != null) {
            String imgPath = selectedImageFilePath[0];
            String filename = selectedImageFilePath[1];








            if (imgPath != null && !imgPath.isEmpty()) {

                Intent i = new Intent();
                i.putExtra("filenamearray", selectedImageFilePath);
                setResult(RESULT_OK, i);
                finish();

                // When Image is not selected from Gallery
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Can't found image",
                        Toast.LENGTH_LONG).show();

                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    "You must select image from gallery before you try to upload",
//                    Toast.LENGTH_LONG).show();
            new Dialog_AlertNotice(context, R.string.s_dialog_title_warning, R.string.s_dialog_msg_selectanimage)
                    .setPositiveKey(R.string.s_dialog_btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
//            startCropImageActivity(AsyncBlackBackgrImage.createBackground(context, imageUri));
            startCropImageActivity(imageUri);
        }

        if ((requestCode == GALLERY_INTENT || requestCode == CAMERA_INTENT) && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(context, data);
            Log.d(getLocalClassName() + "pick image", imageUri.toString());

//            startCropImageActivity(AsyncBlackBackgrImage.createBackground(context, imageUri));
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.imageview_myactivity)).setImageURI(result.getUri());

                Log.d("CropResultUri", result.getUri().toString());

                MyBmpInfo bmpinfo = getThumbnail(result.getUri());
                Bitmap b = bmpinfo.result;
                if (bmpinfo.warnUser) {
                    textInfoImage.setText("Warning: " + bmpinfo.warning + " ( " + b.getWidth() + " x " + b.getHeight() + " ) ");
                    textInfoImage.setTextColor(Color.parseColor("#FFFF0000"));
                } else textInfoImage.setText("");

                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show();

                b = watermark(b);
                // Save image as .jpg file in phone public "Picture" directory
                selectedImageFilePath = saveFile(b);
                selectedImageFilePath = new String[]{selectedImageFilePath[0], selectedImageFilePath[1], result.getUri().toString()};
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG).show();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private Bitmap watermark(Bitmap b) {
        int bmpWidth = b.getWidth();
        int bmpHeight = b.getHeight();
        int logoW = (int) (bmpWidth / 5.0);
        int logoH = (int) (bmpHeight / 3.75);

                /*For testing,
                * saved in phone before upload*/
        Bitmap logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_12_256p_r),
                logoW,
                logoH,
                false);

        int[] px = new int[logoW * logoH];
        for (int i = 0; i < px.length; i++) {
            px[i] = ContextCompat.getColor(context, R.color.c_vt_dark_cyan);
        }

        Bitmap logoBackgColor = Bitmap.createBitmap(px, logoW, logoH, Bitmap.Config.ARGB_8888);

        Bitmap createdLogo = ImageUpload.mark(logoBackgColor,
                logo,
                new Point(0, 0));

        Bitmap wtBitmap = ImageUpload.mark(b,
                createdLogo,
                new Point(bmpWidth - logoW - 20, bmpHeight - logoH - 20));

        return wtBitmap;
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {


        CropImage.ActivityBuilder a = CropImage.activity(imageUri);
        a.setFixAspectRatio(true);
        a.setAspectRatio(4, 3);
        a.setGuidelines(CropImageView.Guidelines.ON);
        a.setAllowCounterRotation(true);
        a.setAllowRotation(false);
        a.setMultiTouchEnabled(false);
        a.start(this, ActivityCrop.class);

    }

    /* In case there are images cropped too small */
    private class MyBmpInfo {
        String warning;
        Bitmap result;
        boolean warnUser;

        MyBmpInfo(Bitmap r, String w, boolean gotMsg) {
            warning = w;
            result = r;
            warnUser = gotMsg;
        }
    }


    /*
    * CAUTIONS::
    * Original METHODS RETRIEVED FROM:
    * http://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
    *
    * however method has been edited for a lot!!!!
    */
    // in activity, get thumbnail bitmap for display
    public MyBmpInfo getThumbnail(Uri uri) {
        int w = 1280;
        int h = 960;
        double TARGETTED_WIDTH = 1920.0;

        try {
            InputStream input = this.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalWidth = onlyBoundsOptions.outWidth;

            double ratio = 1.0;
            if (originalWidth > TARGETTED_WIDTH){

                /*
                * Ratio Sample Size:
                * if 1 , means bitmap is exactly stay as orginal.
                * if 2 or above, means 1/2 or smaller from the ori image.*/
                ratio = originalWidth / TARGETTED_WIDTH;
            }

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = (int)Math.round(ratio);

            Log.d("bitmap scaled info", "ratio = "+ratio+" , poweredRatio = "+bitmapOptions.inSampleSize+", originalWidth = "+originalWidth+" , TARGETTED_WIDTH = "+TARGETTED_WIDTH);

            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, )
            input.close();
            Log.d("elliot", "bitmap before resize: SIZE = " + bitmap.getWidth() + " x " + bitmap.getHeight());
            if (bitmap.getWidth() < w || bitmap.getHeight() < h) {
                return new MyBmpInfo(bitmap, "Image is too small", true);
            } else {
                return new MyBmpInfo(Bitmap.createScaledBitmap(bitmap, w, h, true), "", false);
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else {
            System.out.println("elliot: sample: " + k);
            return k;
        }
    }

    /*
    * END OF RETRIEVED CODE.
    */


    /* 1. http://stackoverflow.com/questions/649154/save-bitmap-to-location
    *  2. http://stackoverflow.com/questions/30934173/save-bitmap-to-android-default-pictures-directory
    *  3. http://stackoverflow.com/questions/37848645/how-to-get-the-default-directory-of-photo-made-by-camera-in-android
    */

    private String[] saveFile(Bitmap bmp) {
        FileOutputStream out = null;

        // image file name
        String filename = "foodieroute-" +
                username + "-" +
                System.currentTimeMillis() + ".jpg";

        // get device's PICTURE folder path
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // PICTURE path + /FoodieRoute Folder.
        File newFolderFromPath = new File(path.toString() + "/FoodieRoute");

        // check folder existsted, if not, create new folder.
        if (!newFolderFromPath.exists()) {
            newFolderFromPath.mkdir();
        }

        File filepathname = new File(newFolderFromPath, filename);

        // saving the image into device folder
        try {
            out = new FileOutputStream(filepathname);
            System.out.println("elliot: fileoutputstream: " + out.toString());
            System.out.println("elliot: filepathname: " + filepathname.toString());
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
//                    return Uri.fromFile(getFileStreamPath(filename));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String[]{filepathname.toString(), filename};
    }

    private Intent cameraIntent() {

        CharSequence title = context.getString(com.theartofdev.edmodo.cropper.R.string.pick_image_intent_chooser_title);
        boolean includeDocuments = false;

        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        List<Intent> allIntents = new ArrayList<>();

        // Determine Uri of camera image to  save.
        Uri outputFileUri = CropImage.getCaptureImageOutputUri(context);

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        // end of collect all camera intents.

        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Intent galleryIntent() {

        CharSequence title = context.getString(com.theartofdev.edmodo.cropper.R.string.pick_image_intent_chooser_title);
        boolean includeDocuments = false;

        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        List<Intent> allIntents = new ArrayList<>();

        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }

        allIntents.addAll(galleryIntents);

        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


}
