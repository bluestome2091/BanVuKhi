package com.hcn.banvukhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtDangNhap, edtPass;
    Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edtDangNhap.getText().toString();
                String Pass = edtPass.getText().toString();

                if (tenDangNhap.equals("1")) {
                    if (Pass.equals("1")) {
                        Intent intent = new Intent(MainActivity.this, TrangChu.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "mật khẩu sai rồi hahaha", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "sai rồi con ngu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        edtDangNhap = findViewById(R.id.edtDangNhap);
        edtPass = findViewById(R.id.edtPass);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
}