package com.example.sipmobile.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

public class Maintenance implements Parcelable {
    private String Kode, TanggalMaintenance, VendorMaintenance, StaffPIC, NamaInventaris, FotoInventaris;

    public Maintenance(String kode, String tanggalMaintenance, String vendorMaintenance, String staffPIC, String namaInventaris, String fotoInventaris) {
        Kode = kode;
        TanggalMaintenance = tanggalMaintenance;
        VendorMaintenance = vendorMaintenance;
        StaffPIC = staffPIC;
        NamaInventaris = namaInventaris;
        FotoInventaris = fotoInventaris;
    }

    protected Maintenance(Parcel in) {
        Kode = in.readString();
        TanggalMaintenance = in.readString();
        VendorMaintenance = in.readString();
        StaffPIC = in.readString();
        NamaInventaris = in.readString();
        FotoInventaris = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Kode);
        dest.writeString(TanggalMaintenance);
        dest.writeString(VendorMaintenance);
        dest.writeString(StaffPIC);
        dest.writeString(NamaInventaris);
        dest.writeString(FotoInventaris);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Maintenance> CREATOR = new Creator<Maintenance>() {
        @Override
        public Maintenance createFromParcel(Parcel in) {
            return new Maintenance(in);
        }

        @Override
        public Maintenance[] newArray(int size) {
            return new Maintenance[size];
        }
    };

    public String getKode() {
        return Kode;
    }

    public String getTanggalMaintenance() {
        return TanggalMaintenance;
    }

    public String getVendorMaintenance() {
        return VendorMaintenance;
    }

    public String getStaffPIC() {
        return StaffPIC;
    }

    public String getNamaInventaris() {
        return NamaInventaris;
    }

    public String getFotoInventaris() {
        return FotoInventaris;
    }
}
