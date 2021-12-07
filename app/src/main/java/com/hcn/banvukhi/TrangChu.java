package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcn.banvukhi.adapter.listviewadapter;
import com.hcn.banvukhi.model.DanhMucVuKhi;
import com.hcn.banvukhi.model.Vukhi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class TrangChu extends AppCompatActivity {
    ListView lvVuKhi;
    ArrayList<Vukhi> dsVuKhis;
    listviewadapter lvadapter;
    DanhMucVuKhi danhMucVuKhi;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbBanVuKhi.sqlite";
    FloatingActionButton fabThem;

    TextView txttv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        copyDbFromAssets();
        addControls();
        addEvents();
        readDSVuKhiTuDb();
        Intent intent = getIntent();
        danhMucVuKhi = (DanhMucVuKhi) intent.getSerializableExtra("key");
        readVuKhiTheoDanhMuc();
    }

    private void readVuKhiTheoDanhMuc() {
        ArrayList<Vukhi> vukhiArrayList = new ArrayList<>();
        if (danhMucVuKhi != null) {
            for (int i = 0; i < dsVuKhis.size(); i++) {
                if (dsVuKhis.get(i).getPhanloai().equals(danhMucVuKhi.getMaDM())) {
                    vukhiArrayList.add(dsVuKhis.get(i));
                }
            }
            dsVuKhis.clear();
            for (int i = 0; i < vukhiArrayList.size(); i++) {
                dsVuKhis.add(vukhiArrayList.get(i));
            }
            lvadapter.notifyDataSetChanged();
        }
    }

    private void copyDbFromAssets() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                File dbDir = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbDir.exists()) dbDir.mkdir();

                InputStream is = getAssets().open(DB_NAME);
                String outputFilePath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void readDSVuKhiTuDb() {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * From BanVuKhi", null);
        dsVuKhis.clear();
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phanloai = cursor.getString(2);
            byte[] hinhanh = cursor.getBlob(3);
            String gia = cursor.getString(4);
            String mau = cursor.getString(5);
            Vukhi vuKhi = new Vukhi(ma, ten, phanloai, hinhanh, gia, mau);
            dsVuKhis.add(vuKhi);
        }
        lvadapter = new listviewadapter(TrangChu.this, dsVuKhis, R.layout.listviewadapter);
        lvVuKhi.setAdapter(lvadapter);
        lvadapter.notifyDataSetChanged();
        cursor.close();
        database.close();
    }

    private void addEvents() {
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this, ThemVuKhi.class);
                startActivity(intent);
            }
        });
//        lvVuKhi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(TrangChu.this, ChitietVuKhi.class);
//                intent.putExtra("intentChitiet", dsVuKhis.get(position));
//                startActivity(intent);
//            }
//        });
        lvVuKhi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrangChu.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Vukhi vukhi = dsVuKhis.get(position);
                        SQLiteDatabase database = openOrCreateDatabase(
                                DB_NAME,
                                MODE_PRIVATE,
                                null
                        );
                        int deletedRowCount = database.delete(
                                "BanVuKhi",
                                "Ma= ?",
                                new String[]{vukhi.getMa() + ""}
                        );
                        Toast.makeText(TrangChu.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT
                        ).show();
                        readDSVuKhiTuDb();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    private void addControls() {
        lvVuKhi = findViewById(R.id.lvVuKhi);
        dsVuKhis = new ArrayList<>();
        fabThem = findViewById(R.id.fabThem);
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
            case R.id.menuEditprofile:
                Intent intent1 = new Intent(TrangChu.this, About.class);
                startActivity(intent1);
                break;
            case R.id.menuDanhMuc:
                Intent intent2 = new Intent(TrangChu.this, DanhMuc.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
