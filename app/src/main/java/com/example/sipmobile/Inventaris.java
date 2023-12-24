package com.example.sipmobile;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Inventaris implements Parcelable {
    private String Kode, Nama, Kategori, Tipe, Path;
    private int Jumlah, HargaBeli, TahunBeli;

    protected Inventaris(Parcel in) {
        Kode = in.readString();
        Nama = in.readString();
        Kategori = in.readString();
        Tipe = in.readString();
        Path = in.readString();
        Jumlah = in.readInt();
        HargaBeli = in.readInt();
        TahunBeli = in.readInt();
    }

    public static final Creator<Inventaris> CREATOR = new Creator<Inventaris>() {
        @Override
        public Inventaris createFromParcel(Parcel in) {
            return new Inventaris(in);
        }

        @Override
        public Inventaris[] newArray(int size) {
            return new Inventaris[size];
        }
    };

    public String getKode() {
        return Kode;
    }

    public String getNama() {
        return Nama;
    }

    public String getKategori() {
        return Kategori;
    }

    public String getTipe() {
        return Tipe;
    }

    public String getPath() {
        return Path;
    }

    public int getJumlah() {
        return Jumlah;
    }

    public int getHargaBeli() {
        return HargaBeli;
    }

    public int getTahunBeli() {
        return TahunBeli;
    }

    public Inventaris(String kode, String nama, String kategori, String tipe, String path, int jumlah, int hargaBeli, int tahunBeli) {
        Kode = kode;
        Nama = nama;
        Kategori = kategori;
        Tipe = tipe;
        Path = path;
        Jumlah = jumlah;
        HargaBeli = hargaBeli;
        TahunBeli = tahunBeli;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(Kode);
        parcel.writeString(Nama);
        parcel.writeString(Kategori);
        parcel.writeString(Tipe);
        parcel.writeString(Path);
        parcel.writeInt(Jumlah);
        parcel.writeInt(HargaBeli);
        parcel.writeInt(TahunBeli);
    }
}
