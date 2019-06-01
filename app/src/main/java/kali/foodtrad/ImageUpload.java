package kali.foodtrad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


/**
 * Created by elliotching on 19-Feb-17.
 */

class ImageUpload {

    private static final String url_upload = ResFR.URL_UPLOAD;
    private static Context context;

    private static InterfaceUploadListener finished;

    // AsyncTask - To convert Image to String
    public static void encodeImagetoString(Context con, String imgPath, String filename, InterfaceUploadListener f) {

        context = con;
        finished = f;

        new AsyncTask<String, Void, String[]>() {

            Bitmap bitmap;

            @Override
            protected String[] doInBackground(String... imgPath) {

                //imgPath[0] = complete path of image
                //imgPath[1] = <filename>.jpg

                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
//                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath[0],
                        options);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 82, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                String encodedString = Base64.encodeToString(byte_arr, 0);
                return new String[]{encodedString, imgPath[1]};
            }

            @Override
            protected void onPostExecute(String... result) {

                String[][] data = new String[][]{
                        {"image", result[0]},
                        {"filename", result[1]}
                };

                System.out.println("image StringLength: \n"+result[0].length());
                // Trigger Image upload
                triggerImageUpload(data);
            }
        }.execute(imgPath, filename);
    }

    private static void triggerImageUpload(String[][] data) {
        makeHTTPCall(data);
    }

    private static class HTTP_POST implements InterfaceCustomHTTP{
        HTTP_POST(Context c, String[][] d, String url){
            CustomHTTP cc = new CustomHTTP(c, d, url);
            cc.ui = this;
            cc.execute();
        }
        @Override
        public void onCompleted(String result) {

            //return this result value back to UI.
            finished.onFinishedUpload();
        }

        @Override
        public void onCompleted(String result, CustomHTTP http) {

        }
    }

    // Make Http call to upload Image to Php server
    private static void makeHTTPCall(String[][] data) {
        new HTTP_POST(context, data, url_upload);
    }

    public static Bitmap mark(Bitmap src, Bitmap watermark, Point location) {
        int w = src.getWidth();
        int h = src.getHeight();

        // ori image
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

//        Paint paint = new Paint();
//        paint.setColor(color);
//        paint.setAlpha(alpha);
//        paint.setTextSize(size);
//        paint.setAntiAlias(true);
//        paint.setUnderlineText(underline);
//        canvas.drawText(watermark, location.x, location.y, paint);
        canvas.drawBitmap(watermark, location.x, location.y, null);

        return result;
    }
}
