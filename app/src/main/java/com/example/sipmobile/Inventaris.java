package com.example.sipmobile;

public class Inventaris {
    private String Kode, Nama, Kategori, Tipe, Path;
    private int Jumlah, HargaBeli, TahunBeli;

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
}
