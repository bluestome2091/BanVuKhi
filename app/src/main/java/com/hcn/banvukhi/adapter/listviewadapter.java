package com.hcn.banvukhi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcn.banvukhi.ChitietVuKhi;
import com.hcn.banvukhi.HienthiChitiet;
import com.hcn.banvukhi.R;
import com.hcn.banvukhi.model.Vukhi;

import java.util.ArrayList;

public class listviewadapter extends BaseAdapter {
    public listviewadapter(Activity context, ArrayList<Vukhi> vuKhis, int layout) {
        this.context = context;
        this.vuKhis = vuKhis;
        this.layout = layout;
    }

    Context context;

    private ArrayList<Vukhi> vuKhis;
    int layout;

    @Override
    public int getCount() {
        return vuKhis.size();
    }

    @Override
    public Object getItem(int position) {
        return vuKhis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        TextView txtName = convertView.findViewById(R.id.tvTenSP);
        TextView txtGia = convertView.findViewById(R.id.tvGia);
        ImageView imgHinh = convertView.findViewById(R.id.imgSanPham);
        Vukhi vukhi = vuKhis.get(position);
        txtName.setText(vukhi.getTen());
        txtGia.setText(String.valueOf(vukhi.getGia()));
        byte[] img = vukhi.getHinhanh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgHinh.setImageBitmap(bitmap);

        ImageButton ibtnedit = convertView.findViewById(R.id.ibtnedit);
        ibtnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChitietVuKhi.class);
                intent.putExtra("intentChitiet", vuKhis.get(position));
                context.startActivity(intent);
            }
        });

        ImageView imgSanPham = convertView.findViewById(R.id.imgSanPham);
        imgSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HienthiChitiet.class);
                intent.putExtra("Chitiet", vuKhis.get(position));
                context.startActivity(intent);
            }
        });

        imgSanPham.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        return convertView;
    }
}

