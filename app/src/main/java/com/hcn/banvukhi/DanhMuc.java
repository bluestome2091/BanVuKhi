package com.hcn.banvukhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hcn.banvukhi.model.DanhMucVuKhi;
import com.hcn.banvukhi.model.Vukhi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DanhMuc extends AppCompatActivity {
    Button btnThemPL, btnSuaPl;
    ListView lvDanhMuc;
    EditText edtMaPL, edtTenPL;
    ArrayAdapter<DanhMucVuKhi> danhMucVuKhiArrayAdapter;
    ArrayList<Vukhi> vukhiArrayList;
    DanhMucVuKhi DmVuKhi = null;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbBanVuKhi.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);
        vukhiArrayList = new ArrayList<>();
        copyDbFromAssets();
        addControls();
        readDMVuKhiTuDb();
        readDSVuKhiTuDb();
        addEvents();
    }

    private void readDMVuKhiTuDb() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("Select * From PhanLoai", null);
        danhMucVuKhiArrayAdapter.clear();
        while (cursor.moveToNext()) {
            String maDM = cursor.getString(0);
            String phanloaiDM = cursor.getString(1);
            danhMucVuKhiArrayAdapter.add(new DanhMucVuKhi(maDM, phanloaiDM));
        }
        cursor.close();
        database.close();
        danhMucVuKhiArrayAdapter.notifyDataSetChanged();
    }

    private  boolean ktraDMVuKh(DanhMucVuKhi DMvukhi){
        for (int i = 0; i < vukhiArrayList.size(); i++) {
            if (vukhiArrayList.get(i).getPhanloai().equals(DMvukhi.getMaDM())) {
                return true;
            }
        }
        return false;
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

    private void addControls() {
        btnThemPL = findViewById(R.id.btnThemPL);
        btnSuaPl = findViewById(R.id.btnSuaPL);
        edtMaPL = findViewById(R.id.edtMaPL);
        edtTenPL = findViewById(R.id.edtTenPL);
        lvDanhMuc = findViewById(R.id.lvDanhMuc);
        danhMucVuKhiArrayAdapter = new ArrayAdapter<>(DanhMuc.this, android.R.layout.simple_list_item_1);
        lvDanhMuc.setAdapter(danhMucVuKhiArrayAdapter);
        registerForContextMenu(lvDanhMuc);
    }

    private void addEvents() {
        btnThemPL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDM = edtMaPL.getText().toString();
                String phanloaiPL = edtTenPL.getText().toString();
                DanhMucVuKhi danhMucVuKhi = new DanhMucVuKhi(maDM, phanloaiPL);
                SQLiteDatabase database = openOrCreateDatabase(
                        DB_NAME,
                        MODE_PRIVATE,
                        null
                );
                ContentValues dulieudong = new ContentValues();
                dulieudong.put("Ma", danhMucVuKhi.getMaDM());
                dulieudong.put("PhanLoai", danhMucVuKhi.getPhanloaiDM());

                long insertedID = database.insert(
                        "PhanLoai",
                        null,
                        dulieudong
                );

                Toast.makeText(
                        DanhMuc.this, "Đã thêm mã: " + edtMaPL.getText(), Toast.LENGTH_LONG).show();
                edtMaPL.setText(null);
                edtTenPL.setText(null);
                database.close();
                readDMVuKhiTuDb();
            }
        });
//        lvDanhMuc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(DanhMuc.this);
//                builder.setIcon(android.R.drawable.ic_delete);
//                builder.setTitle("Thông báo");
//                builder.setMessage("Bạn có chắc muốn xóa không?");
//                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        DanhMucVuKhi danhMucVuKhi = (DanhMucVuKhi) danhMucVuKhiArrayAdapter.getItem(position);
//                        SQLiteDatabase database = openOrCreateDatabase(
//                                DB_NAME,
//                                MODE_PRIVATE,
//                                null
//                        );
//                        int deletedRowCount = database.delete(
//                                "PhanLoai",
//                                "Ma= ?",
//                                new String[]{danhMucVuKhi.getMaDM() + ""}
//                        );
//                        Toast.makeText(DanhMuc.this, "Đã xóa", Toast.LENGTH_SHORT
//                        ).show();
//                        readDMVuKhiTuDb();
//                    }
//                });
//                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//                return false;
//            }
//        });
//        lvDanhMuc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                DanhMucVuKhi DMVK = (DanhMucVuKhi) vukhiArrayAdapter.getItem(position);
//                SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
//                int xoaDong = database.delete(
//                        "PhanLoai", "Ma = ?", new String[]{DMVK.getMaDM() + ""});
//                Toast.makeText(DanhMuc.this, "Đã xóa " + xoaDong + "dòng", Toast.LENGTH_LONG).show();
//                database.close();
//                readDSVuKhiTuDb();
//                return true;
//            }
//        });
        lvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DmVuKhi = (DanhMucVuKhi) danhMucVuKhiArrayAdapter.getItem(position);
                edtMaPL.setText(DmVuKhi.getMaDM().toString());
                edtTenPL.setText(DmVuKhi.getPhanloaiDM().toString());
            }
        });
        btnSuaPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DmVuKhi != null) {
                    String maDM = edtMaPL.getText().toString();
                    String phanloaiDM =edtTenPL.getText().toString();
                    DanhMucVuKhi danhMucVuKhi = new DanhMucVuKhi(maDM, phanloaiDM);
                    SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
                    ContentValues row = new ContentValues();
//                    row.put("Ma", danhMucVuKhi.getMaDM());
                    row.put("Phanloai", danhMucVuKhi.getPhanloaiDM());
                    int updatedRowCount = database.update("PhanLoai", row, "Ma = ?", new String[]{DmVuKhi.getMaDM()});
                    Toast.makeText(DanhMuc.this, "Đã cập nhật thành công", Toast.LENGTH_LONG).show();
                    edtMaPL.setText("");
                    edtTenPL.setText("");
                    database.close();
                    readDMVuKhiTuDb();
                    DmVuKhi = null;
                }
            }
        });
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
                Intent intent1 = new Intent(DanhMuc.this, TrangChu.class);
                startActivity(intent1);
                break;
            case R.id.menuEditprofile:
                Intent intent2 = new Intent(DanhMuc.this, About.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvDanhMuc) {
            getMenuInflater().inflate(R.menu.menu_phanloai, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        DanhMucVuKhi DMvukhi = danhMucVuKhiArrayAdapter.getItem(index);
        switch (item.getItemId()) {
            case R.id.menu_phanloaiShow:
                Intent intent = new Intent(
                        DanhMuc.this,
                        TrangChu.class
                );
                intent.putExtra("key", DMvukhi);
                startActivity(intent);
                 break;
            case R.id.menu_phanloaiDelate:
                   xoaVuKhi(index);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaVuKhi(int index) {
        DanhMucVuKhi danhMucVuKhi = (DanhMucVuKhi) danhMucVuKhiArrayAdapter.getItem(index);
        if(ktraDMVuKh(danhMucVuKhi)){
            Toast.makeText(DanhMuc.this, "Có dữ liệu không thể xóa", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DanhMuc.this);
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa không?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DanhMucVuKhi danhMucVuKhi = (DanhMucVuKhi) danhMucVuKhiArrayAdapter.getItem(index);
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    int deletedRowCount = database.delete(
                            "PhanLoai",
                            "Ma= ?",
                            new String[]{danhMucVuKhi.getMaDM() + ""}
                    );
                    Toast.makeText(DanhMuc.this, "Đã xóa", Toast.LENGTH_SHORT
                    ).show();
                    readDMVuKhiTuDb();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    private void readDSVuKhiTuDb() {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * From BanVuKhi", null);
        vukhiArrayList.clear();
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phanloai = cursor.getString(2);
            byte[] hinhanh = cursor.getBlob(3);
            String gia = cursor.getString(4);
            String mau = cursor.getString(5);

            Vukhi vuKhi = new Vukhi(ma, ten, phanloai, hinhanh, gia, mau);
            vukhiArrayList.add(vuKhi);
        }
        cursor.close();
        database.close();
    }
}