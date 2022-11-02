package com.lat1.sertifikasi_nada;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lat1.sertifikasi_nada.DB.MyDatabaseHelper;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    MyDatabaseHelper myDB;
    ArrayList<String> anggota_id, anggota_nama, anggota_alamat, anggota_nohp, anggota_gender, anggota_lokasi, anggota_detail;
    CustomAdapter customAdapter;
    Bitmap objectBitmap;
    ArrayList<Bitmap> anggota_gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.rv_item);
        add_button = findViewById(R.id.fab_add);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(ResultActivity.this);
        anggota_id = new ArrayList<>();
        anggota_nama = new ArrayList<>();
        anggota_alamat = new ArrayList<>();
        anggota_nohp = new ArrayList<>();
        anggota_gender = new ArrayList<>();
        anggota_lokasi = new ArrayList<>();
        anggota_gambar = new ArrayList<>();
        anggota_detail = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(ResultActivity.this,this, anggota_id, anggota_nama, anggota_alamat, anggota_nohp, anggota_gender, anggota_lokasi, anggota_gambar, anggota_detail);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                anggota_id.add(cursor.getString(0));
                anggota_nama.add(cursor.getString(1));
                anggota_alamat.add(cursor.getString(2));
                anggota_nohp.add(cursor.getString(3));
                anggota_gender.add(cursor.getString(4));
                anggota_lokasi.add(cursor.getString(5));
              //  anggota_gambar.add(cursor.getBlob(6));
                byte [] imageBytes= cursor.getBlob(6);
                objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0,imageBytes.length);
                anggota_gambar.add(objectBitmap);
                anggota_detail.add(cursor.getString(7));

            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Semua?");
        builder.setMessage("Apakah Kamu Ingin Hapus Semua?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ResultActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}

