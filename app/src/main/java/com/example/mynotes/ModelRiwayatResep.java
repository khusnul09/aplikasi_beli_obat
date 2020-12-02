package com.example.mynotes;

public class ModelRiwayatResep {
    private String waktu;
    private String nama_user;
    private String nama_penerima;
    private String handphone;
    private String alamat;
    private String detail_alamat;
    private String gambar_resep;
    private String invoice;
    private String waktu_bayar;
    private String waktu_kirim;
    String Harga;
    private int status;

    public int getStatus() {
        return status;
    }

    public String getStatusAsString() {
        switch (status) {
            case 0:
                return "Menunggu Konfirmasi Apotek";
            case 1:
                return "Menunggu Pembayaran";
            case 2:
                return "Pesanan dikemas";
            case 3:
                return  "Pesanan dikirim";
            case 4:
                return  "Pesanan diterima";
            default:
                return "-";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }


    public String getWaktu() { return waktu; }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public String getWaktu_bayar() {
        return waktu_bayar;
    }

    public void setWaktu_bayar(String waktu_bayar) {this.waktu_bayar =  waktu_bayar;}

    public String getWaktu_kirim() {
        return waktu_kirim;
    }

    public void setWaktu_kirim(String waktu_kirim) {this.waktu_kirim =  waktu_kirim;}


}
