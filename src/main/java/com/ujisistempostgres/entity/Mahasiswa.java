package com.ujisistempostgres.entity;

public class Mahasiswa {
    private int nim;
    private String nama;
    private String alamat;

    public Mahasiswa() {
    }

    public Mahasiswa(int nim, String nama, String alamat) {
        this.nim = nim;
        this.nama = nama;
        this.alamat = alamat;
    }

    public int getNim() {
        return nim;
    }

    public void setNim(int nim) {
        this.nim = nim;
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

    @Override
    public String toString() {
        return "Mahasiswa [nim=" + nim + ", nama=" + nama + ", alamat=" + alamat + "]";
    }
}
