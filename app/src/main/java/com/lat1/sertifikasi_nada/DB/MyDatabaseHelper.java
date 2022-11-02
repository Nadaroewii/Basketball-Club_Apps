package com.lat1.sertifikasi_nada.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;
    private static final String DATABASE_NAME = "AnggotaClub.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "anggota_list";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "nama_anggota";
    private static final String COLUMN_ALAMAT = "alamat_anggota";
    private static final String COLUMN_NOHP = "nohp_anggota";
    private static final String COLUMN_GENDER = "gender_anggota";
    private static final String COLUMN_LOKASI = "lokasi_anggota";
    private static final String COLUMN_GAMBAR = "gambar_anggota";
    private static final String COLUMN_DETAIL = "detail_anggota";



    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ALAMAT + " TEXT, " +
                        COLUMN_NOHP + " VARCHAR, " +
                        COLUMN_GENDER + " TEXT, " +
                        COLUMN_LOKASI + " TEXT, "+
                        COLUMN_GAMBAR + " BLOB, "+
                        COLUMN_DETAIL + " TEXT );";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMember(String nama, String alamat, String noHp, String gender, String lokasi, Bitmap gambar, String detail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Bitmap imageToStoreBitmap = gambar;

        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100,objectByteArrayOutputStream);

        imageInBytes=objectByteArrayOutputStream.toByteArray();

        cv.put(COLUMN_NAME, nama);
        cv.put(COLUMN_ALAMAT, alamat);
        cv.put(COLUMN_NOHP, noHp);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_LOKASI, lokasi);
        cv.put(COLUMN_GAMBAR, imageInBytes);
        cv.put(COLUMN_DETAIL, detail);


        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String nama, String alamat, String noHp, String gender, String lokasi, Bitmap gambar, String detail){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imageToStoreBitmap = gambar;
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100,objectByteArrayOutputStream);

        imageInBytes=objectByteArrayOutputStream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, nama);
        cv.put(COLUMN_ALAMAT, alamat);
        cv.put(COLUMN_NOHP, noHp);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_LOKASI, lokasi);
        cv.put(COLUMN_GAMBAR, imageInBytes);
        cv.put(COLUMN_DETAIL, detail);


        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
