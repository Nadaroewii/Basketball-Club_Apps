package com.lat1.sertifikasi_nada;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.lat1.sertifikasi_nada.DB.DBSource;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
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

public class UbahActivity extends AppCompatActivity {

    EditText Nama, Alamat, Nohp;
    TextView Lokasi, Foto;
    RadioGroup radioGroup;
     RadioButton perempuan,Pria,radioButton;
    Button update_button, delete_button, Upload, LocatioButton;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    LocationRequest locationRequest;
    ImageView objectImageView;
    int checkgroup;
    String Gender, Genderr;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
//    long id;
    String id, nama, alamat, nohp, gender, lokasi, gambar, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Nama = findViewById(R.id.nama);
        Alamat = findViewById(R.id.alamat);
        Nohp = findViewById(R.id.phone);
        radioGroup =findViewById(R.id.radiogroup);
        perempuan = (RadioButton) findViewById(R.id.Rb_perempuan);
        Pria = (RadioButton) findViewById(R.id.Rb_Pria);
        Lokasi = findViewById(R.id.Hasil);
        Foto = findViewById(R.id.resultfoto);
        LocatioButton = findViewById(R.id.btn1);
        Upload = findViewById(R.id.btn2);
        objectImageView = findViewById(R.id.fotobaru);


        update_button = findViewById(R.id.btn_submit);
        delete_button = findViewById(R.id.btn_delete);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        //First we call this
        getAndSetIntentData();
        Log.d("gender", Genderr);

        if (Genderr == "perempuan") {
            perempuan.setChecked(true);
        } else {
            Pria.setChecked(true);

        }
        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nama);
        }
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

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UbahActivity.this);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                nama = Nama.getText().toString().trim();
                alamat = Alamat.getText().toString().trim();
                nohp = Nohp.getText().toString().trim();
                //gender = Gender.getText().toString.trim();
                if(selectedId != -1) {
                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);

                    // do what you want with radioButtonText (save it to database in your case)
                    gender = radioButton.getText().toString().trim();
                }
                lokasi = Lokasi.getText().toString().trim();
                detail = Foto.getText().toString().trim();
                myDB.updateData(id, nama, alamat, nohp, gender, lokasi, imageToStore, detail);
                Intent intent = new Intent(UbahActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nama") &&
                getIntent().hasExtra("alamat") && getIntent().hasExtra("nohp")&& getIntent().hasExtra("gender")&&
                getIntent().hasExtra("lokasi") &&
                getIntent().hasExtra("gambar") && getIntent().hasExtra("detail")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            nama = getIntent().getStringExtra("nama");
            alamat = getIntent().getStringExtra("alamat");
            nohp = getIntent().getStringExtra("nohp");
           gender = getIntent().getStringExtra("gender");
            lokasi = getIntent().getStringExtra("lokasi");
           // gambar = getIntent().getStringExtra("gambar");
            detail = getIntent().getStringExtra("detail");

            //Setting Intent Data
            Nama.setText(nama);
            Alamat.setText(alamat);
            Nohp.setText(nohp);
            Genderr = gender;
            Lokasi.setText(lokasi);
            Foto.setText(detail);
            Log.d("stev", nama+" "+alamat+" "+nohp+" "+ gender + " " +lokasi+" "+gambar+ " "+detail);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
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
                Foto.setText(filename+".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(UbahActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(UbahActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(UbahActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        String hasillokasi = "Latitude : " + latitude + "\n" + "Longitude : " + longitude;
                                        Lokasi.setText(hasillokasi);
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
                                resolvableApiException.startResolutionForResult(UbahActivity.this, 2);
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
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus " + nama + " ?");
        builder.setMessage("Apakah ingin menghapus " + nama + " ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UbahActivity.this);
                myDB.deleteOneRow(id);
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