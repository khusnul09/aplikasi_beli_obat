package com.example.mynotes;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Model implements Serializable {
    @SerializedName("id_obat")
    private String idObat;
    @SerializedName("jenis_kategori")
    private String jenisKategori;
    @SerializedName("kode_obat")
    private String kodeObat;
    @SerializedName("nama_obat")
    private String namaObat;
    @SerializedName("satuan")
    private String satuan;
    @SerializedName("harga_beli")
    private int hargaBeli;
    @SerializedName("harga_jual")
    private int hargaJual;
    @SerializedName("image")
    private int image;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("quantity")
    private int quantity;

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getIdObat() {
        return idObat;
    }

    public void setIdObat(String idObat) {
        this.idObat = idObat;
    }

    public String getJenisKategori() {
        return jenisKategori;
    }

    public void setJenisKategori(String jenisKategori) {
        this.jenisKategori = jenisKategori;
    }

    public String getKodeObat() {
        return kodeObat;
    }

    public void setKodeObat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(int hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public int getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }
};
