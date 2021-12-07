package com.hcn.banvukhi.model;

import java.io.Serializable;

public class Vukhi implements Serializable {
    private int ma;
    private String ten;
    private String phanloai;
    private byte[] hinhanh;
    private String gia;
    private String mau;

    public Vukhi() {
    }

    public Vukhi(int ma, String ten, String phanloai, byte[] hinhanh, String gia, String mau) {
        this.ma = ma;
        this.ten = ten;
        this.phanloai = phanloai;
        this.hinhanh = hinhanh;
        this.gia = gia;
        this.mau = mau;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(String phanloai) {
        this.phanloai = phanloai;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }
}
