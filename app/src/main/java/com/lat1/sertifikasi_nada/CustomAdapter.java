package com.lat1.sertifikasi_nada;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lat1.sertifikasi_nada.models.Biodata;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList anggota_id, anggota_nama, anggota_alamat, anggota_nohp, anggota_gender, anggota_lokasi, anggota_gambar, anggota_detail;
    CustomAdapter(Activity activity, Context context, ArrayList anggota_id,
                  ArrayList anggota_nama,
                  ArrayList anggota_alamat,
                  ArrayList anggota_nohp,
                  ArrayList anggota_gender,
                  ArrayList anggota_lokasi,
                  ArrayList anggota_gambar,
                  ArrayList anggota_detail){
        this.activity = activity;
        this.context = context;
        this.anggota_id = anggota_id;
        this.anggota_nama = anggota_nama;
        this.anggota_alamat = anggota_alamat;
        this.anggota_nohp = anggota_nohp;
        this.anggota_gender = anggota_gender;
        this.anggota_lokasi = anggota_lokasi;
        this.anggota_gambar = anggota_gambar;
        this.anggota_detail = anggota_detail;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_id.setText(String.valueOf(anggota_id.get(position)));
        holder.tv_nama.setText(String.valueOf(anggota_nama.get(position)));
        holder.tv_alamat.setText(String.valueOf(anggota_alamat.get(position)));
        holder.tv_nohp.setText(String.valueOf(anggota_nohp.get(position)));
        holder.tv_gender.setText(String.valueOf(anggota_gender.get(position)));
        holder.iv_foto.setImageBitmap((Bitmap) anggota_gambar.get(position));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbahActivity.class);
                intent.putExtra("id", String.valueOf(anggota_id.get(position)));
                intent.putExtra("nama", String.valueOf(anggota_nama.get(position)));
                intent.putExtra("alamat", String.valueOf(anggota_alamat.get(position)));
                intent.putExtra("nohp", String.valueOf(anggota_nohp.get(position)));
                intent.putExtra("gender", String.valueOf(anggota_gender.get(position)));
                intent.putExtra("lokasi", String.valueOf(anggota_lokasi.get(position)));
                intent.putExtra("gambar", String.valueOf(anggota_gambar.get(position)));
                intent.putExtra("detail", String.valueOf(anggota_detail.get(position)));

                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return anggota_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id, tv_nama, tv_alamat, tv_nohp, tv_gender;
        ImageView iv_foto;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_nohp = itemView.findViewById(R.id.tv_nohp);
            tv_gender = itemView.findViewById(R.id.tv_gender);
            iv_foto = itemView.findViewById(R.id.iv_foto);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}
