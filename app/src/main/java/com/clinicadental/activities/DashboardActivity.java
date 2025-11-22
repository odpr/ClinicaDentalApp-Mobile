package com.clinicadental.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import com.clinicadental.utils.SessionManager;

import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String saved = SessionManager.getToken(this);
        if (saved != null) {ApiClient.setBearerToken(saved);}

        MaterialButton btnPacientes = findViewById(R.id.btnPacientes);
        btnPacientes.setOnClickListener(v -> startActivity(new Intent(this, PacientesActivity.class)));
    }
}
