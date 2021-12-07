package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class About extends AppCompatActivity implements OnMapReadyCallback {
    ImageButton ibtnPhone;
    TextView txtSDT;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addControls();
        addEvents();
    }

    private void addEvents() {
        ibtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    makePhonecall();
                } else {
                    requestPermissions();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng DHCNSG = new LatLng(10.737990732005878, 106.67781410326936);
        mMap.addMarker(new MarkerOptions().position(DHCNSG).title("Đại học Công nghệ Sài Gòn"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DHCNSG));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DHCNSG, 17));
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(
                About.this,
                Manifest.permission.CALL_PHONE
        );
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                About.this,
                Manifest.permission.CALL_PHONE
        )) {
            Toast.makeText(
                    About.this,
                    "Vui long cap quyen trong App Setting",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            ActivityCompat.requestPermissions(
                    About.this,
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    123  // requestCode duoc sinh ra tu quy dinh
            );
        }
    }

    private void makePhonecall() {
        String phoneNum = txtSDT.getText().toString();
        Intent intent = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel: " + phoneNum)
        );
        startActivity(intent);
    }


    private void addControls() {
        txtSDT = findViewById(R.id.txtSDT);
        ibtnPhone = findViewById(R.id.ibtnPhone);
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
                Intent intent1 = new Intent(About.this, TrangChu.class);
                startActivity(intent1);
                break;
            case R.id.menuDanhMuc:
                Intent intent2 = new Intent(About.this, DanhMuc.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}