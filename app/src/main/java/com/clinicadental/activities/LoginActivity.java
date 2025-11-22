/*
package com.clinicadental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.AuthApi;
import com.clinicadental.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText Email, Password;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1) Referencias a las vistas
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // 2) Click del botón (AQUÍ va el bloque que preguntas)
        btnLogin.setOnClickListener(v -> {
            String Email = etEmail.getText()!=null ? etEmail.getText().toString().trim() : "";
            String Password   = Password.getText()!=null ? Password.getText().toString().trim() : "";
            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(this, "Ingrese credenciales", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi api = ApiClient.getClient().create(AuthApi.class);
            api.login(new AuthApi.LoginRequest(Email, Password))
                    .enqueue(new Callback<AuthApi.LoginResponse>() {
                        @Override
                        public void onResponse(Call<AuthApi.LoginResponse> call,
                                               Response<AuthApi.LoginResponse> resp) {
                            if (resp.isSuccessful() && resp.body()!=null) {
                                String token = resp.body().token;
                                SessionManager.saveToken(LoginActivity.this, token);
                                ApiClient.setBearerToken(token);
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthApi.LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}

 */

package com.clinicadental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.AuthApi;
import com.clinicadental.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmailView, etPasswordView;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a vistas
        etEmailView = findViewById(R.id.etEmail);
        etPasswordView = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String emailStr = etEmailView.getText() != null ? etEmailView.getText().toString().trim() : "";
            String passwordStr = etPasswordView.getText() != null ? etPasswordView.getText().toString().trim() : "";

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "Ingrese credenciales", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi api = ApiClient.getClient().create(AuthApi.class);

            // Log útil para verificar nombres de campos y URL final
            Log.d("LOGIN", "POST " + com.clinicadental.utils.Constants.BASE_URL + "auth/login email=" + emailStr);

            api.login(new AuthApi.LoginRequest(emailStr, passwordStr))
                    .enqueue(new Callback<AuthApi.LoginResponse>() {
                        @Override
                        public void onResponse(Call<AuthApi.LoginResponse> call, Response<AuthApi.LoginResponse> resp) {
                            if (resp.isSuccessful() && resp.body() != null) {
                                String token = resp.body().token;
                                SessionManager.saveToken(LoginActivity.this, token);
                                ApiClient.setBearerToken(token);
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthApi.LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}

