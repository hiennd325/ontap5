package com.hiennd325.ontap3;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText ma,ten,gia;
    Button them,sua,thongke;
    Spinner spinner;
    ListView listView;
    ArrayList<sanpham> list = new ArrayList<>();
    ArrayAdapter<sanpham> adapter;
    int vitri = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ma = findViewById(R.id.ma);
        ten = findViewById(R.id.ten);
        gia = findViewById(R.id.gia);
        spinner = findViewById(R.id.spinner);
        them = findViewById(R.id.them);
        sua = findViewById(R.id.sua);
        thongke = findViewById(R.id.thongke);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        //them
        them.setOnClickListener(v -> {
            list.add(new sanpham(
                    ma.getText().toString(),
                    ten.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    Long.parseLong(gia.getText().toString())
            ));
            adapter.notifyDataSetChanged();
        });
        //chon
        listView.setOnItemClickListener((parent, view, position, id) -> {
            vitri = position;
            sanpham sp = list.get(position);
            ma.setText(sp.ma);
            ten.setText(sp.ten);
            gia.setText(String.valueOf(sp.gia));
            ArrayAdapter loaiap = (ArrayAdapter) spinner.getAdapter();
            spinner.setSelection(loaiap.getPosition(sp.loai));
        });
        //sua
        sua.setOnClickListener(v -> {
            if(vitri != -1){
                sanpham sp = list.get(vitri);
                sp.ma = ma.getText().toString();
                sp.ten = ten.getText().toString();
                sp.gia = Long.parseLong(gia.getText().toString());
                sp.loai = spinner.getSelectedItem().toString();
                adapter.notifyDataSetChanged();
            }
        });
        //thongke
        thongke.setOnClickListener(v -> {
            long tong = 0;
            for (sanpham sp : list) tong += sp.gia;
            new AlertDialog.Builder(this)
                    .setTitle("thong ke")
                    .setMessage("Tong cong co "+ list.size()+" san pham \n"+ "Gia tri: "+ tong)
                    .setPositiveButton("Ok", null)
                    .show();
        });
    }
}