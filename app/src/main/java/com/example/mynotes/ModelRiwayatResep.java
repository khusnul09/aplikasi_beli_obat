package com.example.mynotes;

public class ModelRiwayatResep {
    private String waktu;

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_penerima() {
        return nama_penerima;
    }

    public void setNama_penerima(String nama_penerima) {
        this.nama_penerima = nama_penerima;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDetail_alamat() {
        return detail_alamat;
    }

    public void setDetail_alamat(String detail_alamat) {
        this.detail_alamat = detail_alamat;
    }

    public String getGambar_resep() {
        return gambar_resep;
    }

    public void setGambar_resep(String gambar_resep) {
        this.gambar_resep = gambar_resep;
    }

    private String nama_user;
    private String nama_penerima;
    private String handphone;
    private String alamat;
    private String detail_alamat;
    private String gambar_resep;


    public String getWaktu() { return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

}
