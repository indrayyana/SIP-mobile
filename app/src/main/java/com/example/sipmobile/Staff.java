package com.example.sipmobile;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Staff implements Parcelable {
    private String Nama, Jabatan, Tipe;
    private int Id, Gaji, TahunBergabung;

    public Staff(int id, String nama, String jabatan, String tipe, int gaji, int tahunBergabung) {
        Id = id;
        Nama = nama;
        Jabatan = jabatan;
        Tipe = tipe;
        Gaji = gaji;
        TahunBergabung = tahunBergabung;
    }

    protected Staff(Parcel in) {
        Id = in.readInt();
        Nama = in.readString();
        Jabatan = in.readString();
        Tipe = in.readString();
        Gaji = in.readInt();
        TahunBergabung = in.readInt();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    public String getNama() {
        return Nama;
    }

    public String getJabatan() {
        return Jabatan;
    }

    public String getTipe() {
        return Tipe;
    }

    public int getId() {
        return Id;
    }

    public int getGaji() {
        return Gaji;
    }

    public int getTahunBergabung() {
        return TahunBergabung;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nama);
        parcel.writeString(Jabatan);
        parcel.writeString(Tipe);
        parcel.writeInt(Gaji);
        parcel.writeInt(TahunBergabung);
    }
}
