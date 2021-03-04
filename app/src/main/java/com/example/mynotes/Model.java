package com.example.mynotes;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.io.Serializable;

@ParcelablePlease
public class Model implements Serializable, Parcelable {
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
    @SerializedName("apriori")
    private int apriori;

    protected Model(Parcel in) {
        idObat = in.readString();
        jenisKategori = in.readString();
        kodeObat = in.readString();
        namaObat = in.readString();
        satuan = in.readString();
        hargaBeli = in.readInt();
        hargaJual = in.readInt();
        image = in.readInt();
        gambar = in.readString();
        quantity = in.readInt();
        apriori = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idObat);
        dest.writeString(jenisKategori);
        dest.writeString(kodeObat);
        dest.writeString(namaObat);
        dest.writeString(satuan);
        dest.writeInt(hargaBeli);
        dest.writeInt(hargaJual);
        dest.writeInt(image);
        dest.writeString(gambar);
        dest.writeInt(quantity);
        dest.writeInt(apriori);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

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

    public void setApriori(int apriori) {this.apriori = apriori;}

    public int getApriori() {
        return apriori;
    }
};
