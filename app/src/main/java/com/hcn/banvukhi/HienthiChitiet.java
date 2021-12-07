package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcn.banvukhi.model.Vukhi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class HienthiChitiet extends AppCompatActivity {
    EditText edtTenCt, edtGiaCt, edtMauCt, edtPhanLoaiCt;
    FloatingActionButton fabQuayVe;
    ImageView imgHinhCt;
    final int REQUEST_CODE_GALLERY = 999;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbBanVuKhi.sqlite";
    Vukhi vukhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hienthi_chitiet);
        Intent Chitiet = getIntent();
        vukhi = (Vukhi) Chitiet.getSerializableExtra("Chitiet");
        byte[] img = vukhi.getHinhanh();

        addControls();
        addEvents();

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgHinhCt.setImageBitmap(bitmap);
        edtTenCt.setText(vukhi.getTen().toString());
        edtGiaCt.setText(vukhi.getGia().toString());
        edtMauCt.setText(vukhi.getMau().toString());
        edtPhanLoaiCt.setText(vukhi.getPhanloai().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinhCt.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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

    private void addEvents() {
        fabQuayVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HienthiChitiet.this, TrangChu.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        edtTenCt = findViewById(R.id.edtTenCt);
        edtGiaCt = findViewById(R.id.edtGiaCt);
        edtMauCt = findViewById(R.id.edtMauCt);
        edtPhanLoaiCt = findViewById(R.id.edtPhanLoaiCt);
        imgHinhCt = findViewById(R.id.imgHinhCt);
        fabQuayVe = findViewById(R.id.fabQuayVe);
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
                Intent intent1 = new Intent(HienthiChitiet.this, TrangChu.class);
                startActivity(intent1);
                break;
            case R.id.menuEditprofile:
                Intent intent2 = new Intent(HienthiChitiet.this, About.class);
                startActivity(intent2);
                break;
            case R.id.menuDanhMuc:
                Intent intent3 = new Intent(HienthiChitiet.this, DanhMuc.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}