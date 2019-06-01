package kali.foodtrad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by elliotching on 02-Jul-17.
 */

public class AsyncBlackBackgrImage {


    static Uri createBackground(Context context, Uri imguri) {

        InputStream image_stream = null;
        try {
            image_stream = context.getContentResolver().openInputStream(imguri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap b = BitmapFactory.decodeStream(image_stream);

        int bmpWidth = b.getWidth();
        int bmpHeight = b.getHeight();

        int backgrH = bmpHeight;
        int backgrW = (int)Math.ceil((double)backgrH / 3.0 * 4.0);


        try {
            BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(imguri.getEncodedPath(), false);

            int partitionSizeBackH = (int)Math.ceil((double)backgrH / 10.0);

            int[] px = new int[backgrW * partitionSizeBackH];
            for (int i = 0; i < px.length; i++) {
                px[i] = ContextCompat.getColor(context, R.color.c_black);
            }

            Bitmap imgBlackBackgr = Bitmap.createBitmap(px, backgrW, partitionSizeBackH, Bitmap.Config.ARGB_8888);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap b01 = brd.decodeRegion(createRect(backgrW, 0, partitionSizeBackH-1),null);
            b01.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap postB01 = ImageUpload.mark(imgBlackBackgr, b01, new Point( (int)Math.ceil(((double)backgrW / 2.0)-((double)bmpWidth / 2.0)) , 0));
            postB01.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            brd = BitmapRegionDecoder.newInstance(imguri.getEncodedPath(), false);
            Bitmap b02 = brd.decodeRegion(createRect(backgrW, partitionSizeBackH, (partitionSizeBackH*2)-1 ) , null);
            b02.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap postB02 = ImageUpload.mark(imgBlackBackgr, b02, new Point( (int)Math.ceil(((double)backgrW / 2.0)-((double)bmpWidth / 2.0)) , 0));
            postB02.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            brd = BitmapRegionDecoder.newInstance(imguri.getEncodedPath(), false);
            Bitmap b03 = brd.decodeRegion(createRect(backgrW, partitionSizeBackH*2, (partitionSizeBackH*3)-1 ) , null);
            b03.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap postB03 = ImageUpload.mark(imgBlackBackgr, b03, new Point( (int)Math.ceil(((double)backgrW / 2.0)-((double)bmpWidth / 2.0)) , 0));
            postB03.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap b04 = brd.decodeRegion(createRect(backgrW, partitionSizeBackH*3, (partitionSizeBackH*4)-1 ) , null);
            b04.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap postB04 = ImageUpload.mark(imgBlackBackgr, b04, new Point( (int)Math.ceil(((double)backgrW / 2.0)-((double)bmpWidth / 2.0)) , 0));
            postB04.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap b05 = brd.decodeRegion(createRect(backgrW, partitionSizeBackH*4, (partitionSizeBackH*5)-1 ) , null);
            b05.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Bitmap postB05 = ImageUpload.mark(imgBlackBackgr, b05, new Point( (int)Math.ceil(((double)backgrW / 2.0)-((double)bmpWidth / 2.0)) , 0));
            postB05.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


            // ori image
            Bitmap resultBitmap = Bitmap.createBitmap(backgrW, (partitionSizeBackH*5)-1, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(postB01, 0, 0, null);
            canvas.drawBitmap(postB01, 0, partitionSizeBackH, null);
            canvas.drawBitmap(postB01, 0, partitionSizeBackH*2, null);
            canvas.drawBitmap(postB01, 0, partitionSizeBackH*3, null);
            canvas.drawBitmap(postB01, 0, partitionSizeBackH*4, null);
//            canvas.drawBitmap(watermark, location.x, location.y, null);



            return getImageUri(context,resultBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*For testing,
        * saved in phone before upload*/

        return null;

    }

    static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    static Uri getImageUri(Context context, Uri imgUri) {
        InputStream image_stream = null;
        try {
            image_stream = context.getContentResolver().openInputStream(imgUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    static Rect createRect(int width, int top, int bottom){
        return new Rect(0, top, width-1, bottom);
    }
}
