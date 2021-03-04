package com.example.mynotes;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

@ParcelablePlease
public class ModelKeranjang implements Parcelable {
    String nama_obat;
    String harga;
    String quantity;
    String gambar;
    String id_obat;

    public ModelKeranjang() {

    }

    protected ModelKeranjang(Parcel in) {
        nama_obat = in.readString();
        harga = in.readString();
        quantity = in.readString();
        gambar = in.readString();
        id_obat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama_obat);
        dest.writeString(harga);
        dest.writeString(quantity);
        dest.writeString(gambar);
        dest.writeString(id_obat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelKeranjang> CREATOR = new Creator<ModelKeranjang>() {
        @Override
        public ModelKeranjang createFromParcel(Parcel in) {
            return new ModelKeranjang(in);
        }

        @Override
        public ModelKeranjang[] newArray(int size) {
            return new ModelKeranjang[size];
        }
    };

    public String getId_obat() {
        return id_obat;
    }

    public void setId_obat(String id_obat) {
        this.id_obat = id_obat;
    }

    public String getNama_obat() {
        return nama_obat;
    }

    public void setNama_obat(String nama_obat) {
        this.nama_obat = nama_obat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getTotalHarga() {
        return Integer.parseInt(harga) * Integer.parseInt(quantity);
    }
}
