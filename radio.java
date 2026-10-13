package com.hiennd325.ontap1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtMa, edtTen, edtGia;
    RadioGroup rgLoai;
    RadioButton rbDienTu, rbGiaDung, rbThucPham;
    Button btnThem, btnSua, btnThongKe,xoa;
    ListView lvSanPham;
    ArrayList<SanPham> list = new ArrayList<>();
    ArrayAdapter<SanPham> adapter;

    int viTriChon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        rgLoai = findViewById(R.id.rgLoai);
        rbDienTu = findViewById(R.id.rbDienTu);
        rbGiaDung = findViewById(R.id.rbGiaDung);
        rbThucPham = findViewById(R.id.rbThucPham);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnThongKe = findViewById(R.id.btnThongKe);
        lvSanPham = findViewById(R.id.lvSanPham);
        xoa = findViewById(R.id.xoa);

        list.add(new SanPham("DT01", "Điện thoại Samsung", 10000000, "Điện tử"));
        list.add(new SanPham("GD01", "Tủ lạnh Panasonic", 12000000, "Gia dụng"));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvSanPham.setAdapter(adapter);

        // a) Thêm
        btnThem.setOnClickListener(v -> {
            RadioButton rb = findViewById(rgLoai.getCheckedRadioButtonId());
            list.add(new SanPham(
                edtMa.getText().toString(),
                edtTen.getText().toString(),
                Long.parseLong(edtGia.getText().toString()),
                rb.getText().toString()
            ));
            adapter.notifyDataSetChanged();
        });

        // b) Chọn trên ListView
        lvSanPham.setOnItemClickListener((p, view, pos, id) -> {
            viTriChon = pos;
            SanPham sp = list.get(pos);
            edtMa.setText(sp.ma);
            edtTen.setText(sp.ten);
            edtGia.setText(String.valueOf(sp.gia));

            if (sp.loai.equals("Điện tử")) rbDienTu.setChecked(true);
            else if (sp.loai.equals("Gia dụng")) rbGiaDung.setChecked(true);
            else rbThucPham.setChecked(true);
        });

        // b) Sửa
        btnSua.setOnClickListener(v -> {
            if (viTriChon != -1) {
                SanPham sp = list.get(viTriChon);
                sp.ma = edtMa.getText().toString();
                sp.ten = edtTen.getText().toString();
                sp.gia = Long.parseLong(edtGia.getText().toString());
                RadioButton rb = findViewById(rgLoai.getCheckedRadioButtonId());
                sp.loai = rb.getText().toString();
                adapter.notifyDataSetChanged();
            }
        });

        // c) Thống kê
        btnThongKe.setOnClickListener(v -> {
            long tongGia = 0;
            for (SanPham sp : list) tongGia += sp.gia;
            new AlertDialog.Builder(this)
                .setTitle("Thống kê")
                .setMessage("Tổng số SP: " + list.size() + "\nTổng tiền: " + tongGia)
                .setPositiveButton("OK", null)
                .show();
        });
        xoa.setOnClickListener(v -> {
            list.remove(viTriChon);
            adapter.notifyDataSetChanged();
        });
    }
}
