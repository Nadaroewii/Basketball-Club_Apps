package com.lat1.sertifikasi_nada;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lat1.sertifikasi_nada.DB.MyDatabaseHelper;
import com.lat1.sertifikasi_nada.models.Biodata;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Nama, Alamat, NoHp;
    Button Submit, Upload, LocatioButton;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    LocationRequest locationRequest;
    RadioGroup radioGroup;
    ImageView objectImageView;
    RadioButton perempuan,Pria,radioButton;
    TextView AddressText, HasilFoto;
    String Gender;
    int checkgroup;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nama = findViewById(R.id.nama);
        Alamat = findViewById(R.id.alamat);
        NoHp = findViewById(R.id.phone);
        objectImageView = findViewById(R.id.foto);
        radioGroup =findViewById(R.id.radiogroup);
        AddressText = findViewById(R.id.Hasil);
        HasilFoto = findViewById(R.id.resultfoto);
        LocatioButton = findViewById(R.id.btn1);
        Upload = findViewById(R.id.btn2);
        Submit = findViewById(R.id.btn3);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

      //  dbSource = new DBSource(this);
       // dbSource.open();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                checkgroup = checkedButtonId;
                switch (checkedButtonId) {
                    case R.id.perempuan:
                        Gender = "Perempuan";
                        break;
                    case R.id.Pria:
                        Gender = "Laki-laki";
                        break;
                }
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();


                }
            }
        });
        LocatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }

        });

        Submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn3:
                if(TextUtils.isEmpty(Nama.getText().toString().trim())
                        && TextUtils.isEmpty((Alamat.getText().toString().trim()))
                        && TextUtils.isEmpty(NoHp.getText().toString().trim())
                ){

                    Nama.setError("Mohon Isi Data");
                    Alamat.setError("Mohon Isi Data");
                    NoHp.setError("Mohon Isi Data");
//                    AddressText.setError("Mohon Ambil Data");
                }else if(TextUtils.isEmpty((Nama.getText().toString().trim())) ){
                    Nama.setError("Mohon Isi Nama");

                }else if(TextUtils.isEmpty((Alamat.getText().toString().trim())) ){
                    Alamat.setError("Mohon Isi Alamat");

                }else if(TextUtils.isEmpty((NoHp.getText().toString().trim())) ){
                    NoHp.setError("Mohon Isi NoHp");

                }else if(checkgroup <= 0){
                    Toast.makeText(MainActivity.this, "Mohon Isi Gender", Toast.LENGTH_LONG).show();

//                }else if(TextUtils.isEmpty((AddressText.getText().toString().trim())) ){
//                    AddressText.setError("Mohon Ambil Data");

                } else {
                    TambahAnggota();
                    //KonfirmasiTambah();
                    Toast.makeText(MainActivity.this, "Berhasil ditambah", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    startActivity(intent);
                }
                            break;
                    }
            }
        });
    }
    private void TambahAnggota() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String nama = null;
        String alamat = null;
        String gender= null;
        String lokasi = null;
        String nohp = null;
        String detail = null;
        //String gambar = null;
        //Biodata biodata = null;
        if(selectedId != -1) {
            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);

            // do what you want with radioButtonText (save it to database in your case)
            gender = radioButton.getText().toString().trim();
        }
        nama = Nama.getText().toString().trim();
       nohp = NoHp.getText().toString().trim();
        alamat = Alamat.getText().toString().trim();
        lokasi = AddressText.getText().toString().trim();
        detail = HasilFoto.getText().toString().trim();
        //biodata = dbSource.createBiodata(nama, alamat, nohp, gender, lokasi, gambar);
        myDB.addMember(nama, alamat, nohp, gender, lokasi, imageToStore, detail);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Untuk Maps
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }
        //Untuk Galeri
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied..!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Untuk lokasi
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
        //Untuk Gambar
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
                objectImageView.setImageBitmap(imageToStore);
                File path = new File(data.getDataString());
                String filename = path.getName();
                HasilFoto.setText(filename+".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        String hasillokasi = "Latitude : " + latitude + "\n" + "Longitude : " + longitude;
                                        AddressText.setText(hasillokasi);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
//    @Override
//    public void onBackPressed() {
//
//        super.onBackPressed();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            KonfirmasiTambah();
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        dbSource.close();
//    }
}