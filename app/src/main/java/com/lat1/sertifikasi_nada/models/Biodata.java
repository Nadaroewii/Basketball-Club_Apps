package com.lat1.sertifikasi_nada.models;

public class Biodata {
    private long id;
    private String nama;
    private String alamat;
    private String nohp;
    private String gender;
    private String lokasi;
    private String gambar;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return nohp;
    }

    public void setNoHp(String nohp) { this.nohp = nohp; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }


    @Override
    public String toString() {
        return "Nama = " + nama + "/n" +
                ", Alamat = " + alamat + "/n" +
                ", No Hp = " + nohp + "/n" +
                "Gender = " + gender  + "/n" +
                ", Lokasi = " + lokasi + "/n" +
                "Gambar = " + gambar ;
    }


    public Biodata() {
    }

}