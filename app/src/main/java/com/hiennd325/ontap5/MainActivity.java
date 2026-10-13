package com.hiennd325.ontap5;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtTenChuHo, edtSoKWh;
    RadioGroup rgKhuVuc;
    RadioButton rbKV1, rbKV2, rbKV3;
    Button btnThemMoi, btnSua, btnThongKe;
    ListView lvDanhSach;

    ArrayList<String[]> danhSach; // [tenChuHo, khuVuc, soKWh]
    MyAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        edtTenChuHo = findViewById(R.id.edtTenChuHo);
        edtSoKWh = findViewById(R.id.edtSoKWh);
        rgKhuVuc = findViewById(R.id.rgKhuVuc);
        rbKV1 = findViewById(R.id.rbKV1);
        rbKV2 = findViewById(R.id.rbKV2);
        rbKV3 = findViewById(R.id.rbKV3);
        btnThemMoi = findViewById(R.id.btnThemMoi);
        btnSua = findViewById(R.id.btnSua);
        btnThongKe = findViewById(R.id.btnThongKe);
        lvDanhSach = findViewById(R.id.lvDanhSach);

        // Khởi tạo danh sách với 3 dữ liệu mẫu, mặc định Khu vực 1
        danhSach = new ArrayList<>();
        danhSach.add(new String[]{"Trần Đình Tùng", "Khu vực 1", "240"});
        danhSach.add(new String[]{"Hoàng Mỹ Linh", "Khu vực 2", "125"});
        danhSach.add(new String[]{"Phạm Quốc Tuấn", "Khu vực 1", "330"});

        adapter = new MyAdapter();
        lvDanhSach.setAdapter(adapter);

        // Mặc định chọn Khu vực 1
        rbKV1.setChecked(true);

        // Câu 3: Chọn 1 dòng trên ListView -> hiển thị ngược lại thông tin lên form
        lvDanhSach.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            String[] item = danhSach.get(position);
            edtTenChuHo.setText(item[0]);

            // Set RadioButton theo khu vực
            switch (item[1]) {
                case "Khu vực 1":
                    rbKV1.setChecked(true);
                    break;
                case "Khu vực 2":
                    rbKV2.setChecked(true);
                    break;
                case "Khu vực 3":
                    rbKV3.setChecked(true);
                    break;
            }

            edtSoKWh.setText(item[2]);
        });

        // Câu 2: Nút Thêm mới
        btnThemMoi.setOnClickListener(v -> {
            String tenChuHo = edtTenChuHo.getText().toString().trim();
            String soKWhStr = edtSoKWh.getText().toString().trim();

            // Kiểm tra hợp lệ
            if (tenChuHo.isEmpty()) {
                Toast.makeText(this, "Tên chủ hộ không được trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soKWhStr.isEmpty()) {
                Toast.makeText(this, "Số kWh không được trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soKWh;
            try {
                soKWh = Integer.parseInt(soKWhStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số kWh phải là số nguyên!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soKWh < 0) {
                Toast.makeText(this, "Số kWh phải >= 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy khu vực
            String khuVuc = getSelectedKhuVuc();

            // Thêm vào danh sách
            danhSach.add(new String[]{tenChuHo, khuVuc, String.valueOf(soKWh)});
            adapter.notifyDataSetChanged();

            // Xóa form
            clearForm();

            Toast.makeText(this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
        });

        // Câu 3: Nút Sửa
        btnSua.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Vui lòng chọn một dòng để sửa!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tenChuHo = edtTenChuHo.getText().toString().trim();
            String soKWhStr = edtSoKWh.getText().toString().trim();

            // Kiểm tra hợp lệ
            if (tenChuHo.isEmpty()) {
                Toast.makeText(this, "Tên chủ hộ không được trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soKWhStr.isEmpty()) {
                Toast.makeText(this, "Số kWh không được trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soKWh;
            try {
                soKWh = Integer.parseInt(soKWhStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số kWh phải là số nguyên!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soKWh < 0) {
                Toast.makeText(this, "Số kWh phải >= 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            String khuVuc = getSelectedKhuVuc();

            // Cập nhật dòng tương ứng
            danhSach.set(selectedPosition, new String[]{tenChuHo, khuVuc, String.valueOf(soKWh)});
            adapter.notifyDataSetChanged();

            clearForm();
            selectedPosition = -1;

            Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
        });

        // Câu 4: Nút Thống kê
        btnThongKe.setOnClickListener(v -> {
            if (danhSach.isEmpty()) {
                Toast.makeText(this, "Danh sách rỗng!", Toast.LENGTH_SHORT).show();
                return;
            }

            int tongSoHo = danhSach.size();

            // Tìm hộ có mức tiêu thụ điện cao nhất
            String hoMax = danhSach.get(0)[0];
            int max = Integer.parseInt(danhSach.get(0)[2]);

            for (int i = 1; i < danhSach.size(); i++) {
                int kWh = Integer.parseInt(danhSach.get(i)[2]);
                if (kWh > max) {
                    max = kWh;
                    hoMax = danhSach.get(i)[0];
                }
            }

            String noiDung = "• Tổng số hộ: " + tongSoHo
                    + "\n• Cao nhất: " + hoMax + " (" + max + " kWh)";

            new AlertDialog.Builder(this)
                    .setTitle("Thống kê tiêu thụ điện")
                    .setMessage(noiDung)
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        // Đóng dialog, quay về màn hình chính
                        dialog.dismiss();
                    })
                    .setNegativeButton("Đóng", (dialog, which) -> {
                        // Thoát ứng dụng
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private String getSelectedKhuVuc() {
        int checkedId = rgKhuVuc.getCheckedRadioButtonId();
        if (checkedId == R.id.rbKV2) return "Khu vực 2";
        if (checkedId == R.id.rbKV3) return "Khu vực 3";
        return "Khu vực 1";
    }

    private void clearForm() {
        edtTenChuHo.setText("");
        edtSoKWh.setText("");
        rbKV1.setChecked(true);
    }

    // Custom Adapter cho ListView với 3 cột
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return danhSach.size();
        }

        @Override
        public Object getItem(int position) {
            return danhSach.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.list_item, parent, false);
            }

            TextView tvChuHo = convertView.findViewById(R.id.tvChuHo);
            TextView tvKhuVuc = convertView.findViewById(R.id.tvKhuVuc);
            TextView tvSoKWh = convertView.findViewById(R.id.tvSoKWh);

            String[] item = danhSach.get(position);
            tvChuHo.setText(item[0]);
            tvKhuVuc.setText(item[1]);
            tvSoKWh.setText(item[2] + " kWh");

            return convertView;
        }
    }
}
