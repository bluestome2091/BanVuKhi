package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcn.banvukhi.model.Vukhi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChitietVuKhi extends AppCompatActivity {
    EditText edtTen, edtGia, edtMau, edtPhanLoai;
    Button btnChonhinh, btnSua;
    FloatingActionButton fabQuayVe;
    ImageView imgHinh;
    final int REQUEST_CODE_GALLERY = 999;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbBanVuKhi.sqlite";
    Vukhi vukhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_vu_khi);
        Intent intentChitiet = getIntent();
        vukhi = (Vukhi) intentChitiet.getSerializableExtra("intentChitiet");
        byte[] img = vukhi.getHinhanh();

        addControls();
        addEvents();

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgHinh.setImageBitmap(bitmap);
        edtTen.setText(vukhi.getTen().toString());
        edtGia.setText(vukhi.getGia().toString());
        edtMau.setText(vukhi.getMau().toString());
        edtPhanLoai.setText(vukhi.getPhanloai().toString());
    }

    private void addEvents() {
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ten = edtTen.getText().toString();
                    String gia = edtGia.getText().toString();
                    String mau = edtMau.getText().toString();
                    String phanloai = edtPhanLoai.getText().toString();

                    Vukhi vukhi1 = new Vukhi(vukhi.getMa(), ten, phanloai, imageViewTOByte(imgHinh), gia, mau);
                    Toast.makeText(ChitietVuKhi.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    readDuLieu(vukhi1);
                    Intent intent = new Intent(ChitietVuKhi.this, TrangChu.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnChonhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ChitietVuKhi.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });
        fabQuayVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChitietVuKhi.this, TrangChu.class);
                startActivity(intent);
            }
        });
    }

    private void readDuLieu(Vukhi vukhi1) {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues dulieuDong = new ContentValues();
        dulieuDong.put("ten", vukhi1.getTen());
        dulieuDong.put("gia", vukhi1.getGia());
        dulieuDong.put("mau", vukhi1.getMau());
        dulieuDong.put("phanloai", vukhi1.getPhanloai());
        dulieuDong.put("hinhanh", vukhi1.getHinhanh());
        int capnhatDong = database.update("BanVuKhi", dulieuDong, "Ma = ?", new String[]{vukhi1.getMa() + ""});
        database.close();
    }

    private void addControls() {
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        edtMau = findViewById(R.id.edtMau);
        edtPhanLoai = findViewById(R.id.edtPhanLoai);
        imgHinh = findViewById(R.id.imgHinh);
        btnChonhinh = findViewById(R.id.btnChonhinh);
        btnSua = findViewById(R.id.btnCapNhat);
        fabQuayVe = findViewById(R.id.fabQuayVe);
    }

    public static final int PICK_NAME = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
                Intent intent1 = new Intent(ChitietVuKhi.this, TrangChu.class);
                startActivity(intent1);
                break;
            case R.id.menuEditprofile:
                Intent intent2 = new Intent(ChitietVuKhi.this, About.class);
                startActivity(intent2);
                break;
            case R.id.menuDanhMuc:
                Intent intent3 = new Intent(ChitietVuKhi.this, DanhMuc.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}