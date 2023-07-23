package com.mastercoding.asmremake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mastercoding.asmremake.API.ApiService;
import com.mastercoding.asmremake.Adapter.PhoneAdapter;
import com.mastercoding.asmremake.Model.Phone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PhoneAdapter.Callback {
    private RecyclerView rcvPhone;
    private PhoneAdapter adapter;
    private FloatingActionButton floatAdd;
    private List<Phone> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();

        rcvPhone = findViewById(R.id.rcvPhone);
        floatAdd = findViewById(R.id.floatAdd);

        floatAdd.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPhone.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvPhone.addItemDecoration(itemDecoration);

        adapter = new PhoneAdapter(new ArrayList<>(), this);
        rcvPhone.setAdapter(adapter);
        callApiGetList();
    }

    private void callApiGetList() {
        // lay danh sach
        ApiService.apiService.getPhone().enqueue(new Callback<List<Phone>>() {
            @Override
            public void onResponse(Call<List<Phone>> call, Response<List<Phone>> response) {
                if (response.isSuccessful()) {
                    List<Phone> item = response.body();
                    if (item != null) {
                        adapter.setTableItems(item);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Phone>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error" + t, Toast.LENGTH_SHORT).show();
                Log.d("loi", ""+t);
            }
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_phone, null);
        builder.setView(dialogView);
        builder.setView(dialogView);

        final EditText edtName = dialogView.findViewById(R.id.edtName);
        final EditText edtBrand = dialogView.findViewById(R.id.edtBrand);
        final EditText edtPrice = dialogView.findViewById(R.id.edtPrice);
        final EditText edtQuantity = dialogView.findViewById(R.id.edtquantiny);
        final Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        final Button btnCanel = dialogView.findViewById(R.id.btnCancel);


        final AlertDialog dialog = builder.create();
        dialog.show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String brand = edtBrand.getText().toString();
                int price = Integer.parseInt(edtPrice.getText().toString());
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                addNewData(name, brand, price, quantity);
                dialog.dismiss();
            }
        });
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                dialog.dismiss();
            }
        });
    }

    private void addNewData(String name,String brand, int price, int quantity) {
        Phone phone = new Phone();
        phone.setName(name);
        phone.setBrand(brand);
        phone.setPrice(price);
        phone.setQuantity(quantity);

        ApiService.apiService.addPhone(phone).enqueue(new Callback<List<Phone>>() {
            @Override
            public void onResponse(Call<List<Phone>> call, Response<List<Phone>> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(MainActivity.this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();

                    List<Phone> tableItems = response.body();
                    if (tableItems != null) {
                        adapter.setTableItems(tableItems);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Xử lý lỗi khi thêm dữ liệu
                    Toast.makeText(MainActivity.this, "Lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Phone>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void editPhone(Phone model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_phone, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        EditText edName = dialog.findViewById(R.id.edtName);
        EditText edtBrand = dialog.findViewById(R.id.edtBrand);
        EditText edPrice = dialog.findViewById(R.id.edtPrice);
        EditText edQuantity = dialog.findViewById(R.id.edtquantiny);
        Button btnEdit = dialog.findViewById(R.id.btnAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnEdit.setText("Sửa");
        edName.setText(model.getName());
        edtBrand.setText(model.getBrand());
        edPrice.setText(String.valueOf(model.getPrice()));
        edQuantity.setText(String.valueOf(model.getQuantity()));

        btnEdit.setOnClickListener(v -> {
            String name = edName.getText().toString().trim();
            String brand = edtBrand.getText().toString().trim();
            int price = Integer.parseInt(edPrice.getText().toString().trim());
            int quantity = Integer.parseInt(edQuantity.getText().toString().trim());

            updatePhone(model.getId(), name, brand, price, quantity);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void updatePhone(String id, String name, String brand, int price, int quantity) {
        Phone phone = new Phone();
        phone.setName(name);
        phone.setBrand(brand);
        phone.setPrice(price);
        phone.setQuantity(quantity);

        Call<Phone> call = ApiService.apiService.updatePhone(id, phone);
        call.enqueue(new Callback<Phone>() {
            @Override
            public void onResponse(Call<Phone> call, Response<Phone> response) {
                if (response.isSuccessful()) {
                    Phone phone1 = response.body();
                    Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    callApiGetList();
                } else {
                    Log.d("MAIN", "Respone Fail" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Phone> call, Throwable t) {
                Log.d("MAIN", "Respone Fail" + t.getMessage());
            }
        });
    }

    @Override
    public void deletePhone(Phone model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            deleteP(model);
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteP(Phone model) {
        String id = model.getId();
        Call<Void> call = ApiService.apiService.deletePhones(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    callApiGetList();
                } else {
                    Log.d("MAIN", "Respone Fail" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MAIN", "Respone Fail" + t.getMessage());
            }
        });
    }
}