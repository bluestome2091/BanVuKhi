package com.hcn.banvukhi.model;

import android.widget.EditText;

public class DanhMucVuKhi extends Vukhi {
    private String maDM;
    private String phanloaiDM;

    public DanhMucVuKhi(EditText edtMaPL) {
    }

    public DanhMucVuKhi(String maDM, String phanloaiDM) {
        this.maDM = maDM;
        this.phanloaiDM = phanloaiDM;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getPhanloaiDM() {
        return phanloaiDM;
    }

    public void setPhanloaiDM(String phanloaiDM) {
        this.phanloaiDM = phanloaiDM;
    }

    @Override
    public String toString() {
        return "Mã: " + maDM + " - " + "Tên phân loại: " + phanloaiDM;
    }
}
