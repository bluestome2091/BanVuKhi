package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hcn.banvukhi.model.Vukhi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemVuKhi extends AppCompatActivity {
    EditText edtTen, edtGia, edtMau, edtPhanLoaiThem;
    Button btnThemHinh, btnThemVK, btnTroVe;
    ImageView imgHinh;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    final int REQUEST_CODE_GALLERY = 999;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbBanVuKhi.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_vu_khi);

        addControls();
        addEvents();
    }

    private void addControls() {
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        edtMau = findViewById(R.id.edtMau);
        edtPhanLoaiThem = findViewById(R.id.edtPhanLoaithem);
        btnThemHinh = findViewById(R.id.btnThemHinh);
        btnThemVK = findViewById(R.id.btnThemVK);
        btnTroVe = findViewById(R.id.btnTroVe);
        imgHinh = findViewById(R.id.imgHinh);
    }

    private void addEvents() {
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemVuKhi.this, TrangChu.class);
                startActivity(intent);
            }
        });

        btnThemHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ThemVuKhi.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });
        btnThemVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString();
                String gia = edtGia.getText().toString();
                String mau = edtMau.getText().toString();
                String phanloai = edtPhanLoaiThem.getText().toString();
                Vukhi vukhi = new Vukhi(1, ten, phanloai, imageViewTOByte(imgHinh), gia, mau);
                Toast.makeText(ThemVuKhi.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                ghiDuLieuDb(vukhi);
            }
        });
    }

    private void ghiDuLieuDb(Vukhi vukhi) {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues valuesDong = new ContentValues();
        valuesDong.put("ten", vukhi.getTen());
        valuesDong.put("phanloai", vukhi.getPhanloai());
        valuesDong.put("hinhanh", vukhi.getHinhanh());
        valuesDong.put("gia", vukhi.getGia());
        valuesDong.put("mau", vukhi.getMau());
        long addID = database.insert("BanVuKhi", null, valuesDong);
        database.close();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "Đã từ chối quyền", Toast.LENGTH_LONG).show();
            }
        }
    }

    private byte[] imageViewTOByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu); // R.menu.menu là lấy phần menu trong res, còn menu sau là biến menu tạo ở trên
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTrangChu:
                Intent intent1 = new Intent(ThemVuKhi.this, TrangChu.class);
                startActivity(intent1);
                break;
            case R.id.menuEditprofile:
                Intent intent2 = new Intent(ThemVuKhi.this, About.class);
                startActivity(intent2);
                break;
            case R.id.menuDanhMuc:
                Intent intent3 = new Intent(ThemVuKhi.this, DanhMuc.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}