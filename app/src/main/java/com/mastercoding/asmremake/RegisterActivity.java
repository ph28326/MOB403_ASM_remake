package com.mastercoding.asmremake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mastercoding.asmremake.API.ApiService;
import com.mastercoding.asmremake.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText usernameRegister, addressRegister, emailRegister, passwordRegister, confirmPasswordRegister;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameRegister= findViewById(R.id.usernameRegister);
        addressRegister= findViewById(R.id.addressRegister);
        emailRegister= findViewById(R.id.emailRegister);
        passwordRegister= findViewById(R.id.passwordRegister);
        confirmPasswordRegister= findViewById(R.id.confirmPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameRegister.getText().toString();
                String password = passwordRegister.getText().toString();
                String email = emailRegister.getText().toString();
                String address = addressRegister.getText().toString();
                registerNewUser(name, password, email, address);

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerNewUser(String name, String password, String email, String address){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);

        ApiService.apiService.registerUser(user).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    // Xử lý lỗi khi thêm dữ liệu
                    Toast.makeText(RegisterActivity.this, "Lỗi khi tạo tài khoản", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();

            }
        });
    }

}