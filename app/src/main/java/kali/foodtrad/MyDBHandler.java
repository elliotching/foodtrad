package kali.foodtrad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by elliotching on 11-Nov-16.
*/
class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "YourDB2.db";
    private static final String TABLE_NAME = "Msgs";

    public static final String TABLE_COLUMN_1_TIME = "time";
    public static final String TABLE_COLUMN_2_MSG = "msg";

    public final String rawQuery_CreateTable(){
        return "CREATE TABLE " + TABLE_NAME + " ( " +
                TABLE_COLUMN_1_TIME + " TEXT, " +
                TABLE_COLUMN_2_MSG + " TEXT )";
    }

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create foods table
        db.execSQL(rawQuery_CreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older foods table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // create fresh foods table
        this.onCreate(db);
    }

    public void addProduct(NotiMsg notiMsg) {

        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMN_1_TIME , notiMsg._time);
        values.put(TABLE_COLUMN_2_MSG , notiMsg._msg);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<NotiMsg> getAllProducts() {
        ArrayList<NotiMsg> notiMsgs = new ArrayList<NotiMsg>();

        String query = "SELECT * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        NotiMsg notiMsg = null;
        if (cursor.moveToFirst()) {
            do {
                notiMsg = new NotiMsg();
                notiMsg._time = (cursor.getString(0));
                notiMsg._msg = (cursor.getString(1));

                // Add book to books
                notiMsgs.add(notiMsg);
            } while (cursor.moveToNext());
        }

//        System.out.println("elliot: getAllBooks(): " + notiMsgs.toString());

        db.close();
        // return books
        return notiMsgs;
    }
//
//    public NotiMsg findProduct(String productname) {
//        String query = "Select * FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_1_NAME + " = \"" + productname + "\"";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(query, null);
//
//        NotiMsg product = new NotiMsg();
//
//        if (cursor.moveToFirst()) {
//            product.setID(Integer.parseInt(cursor.getString(0)));
//            product.setProductName(cursor.getString(1));
//            product.setQuantity(Integer.parseInt(cursor.getString(2)));
//            cursor.close();
//        } else {
//            product = null;
//        }
//        db.close();
//        return product;
//    }

//    public boolean deleteProduct(String productname) {
//
//        boolean result = false;
//
//        String query = "Select * FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_1_NAME + " = \"" + productname + "\"";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(query, null);
//
//        NotiMsg product = new NotiMsg();
//
//        if (cursor.moveToFirst()) {
//            product.setID(Integer.parseInt(cursor.getString(0)));
//            db.delete(TABLE_NAME, TABLE_COLUMN_0_ID + " = ?",
//                    new String[] { String.valueOf(product.getID()) });
//            cursor.close();
//            result = true;
//        }
//        db.close();
//        return result;
//    }
}