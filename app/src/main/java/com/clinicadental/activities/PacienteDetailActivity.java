package com.clinicadental.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.models.Paciente;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.PacienteApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteDetailActivity extends AppCompatActivity {

    private TextView txtNombre, txtTelefono, txtEmail, txtCedula, txtDireccion;
    private Button btnEditar, btnEliminar;

    private Paciente paciente;
    private static final int REQ_EDIT = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_detail);

        txtNombre = findViewById(R.id.txtNombreDetalle);
        txtTelefono = findViewById(R.id.txtTelefonoDetalle);
        txtEmail = findViewById(R.id.txtEmailDetalle);
        //txtCedula = findViewById(R.id.txtCedulaDetalle);
        txtDireccion = findViewById(R.id.txtDireccionDetalle);

        btnEditar = findViewById(R.id.btnEditarPaciente);
        btnEliminar = findViewById(R.id.btnEliminarPaciente);

        paciente = (Paciente) getIntent().getSerializableExtra("paciente");
        if (paciente == null) {
            Toast.makeText(this, "Paciente no encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarDatos();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(PacienteDetailActivity.this, CreateEditPacienteActivity.class);
            intent.putExtra("paciente", paciente);
            startActivityForResult(intent, REQ_EDIT);
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void mostrarDatos() {
        txtNombre.setText(paciente.getNombre());
        txtTelefono.setText(paciente.getTelefono());
        txtEmail.setText(paciente.getEmail());
        //txtCedula.setText(paciente.getCedula());
        txtDireccion.setText(paciente.getDireccion());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar paciente")
                .setMessage("¿Seguro que deseas eliminar este paciente?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarPaciente())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarPaciente() {
        ApiService api = RetrofitClient.getApiService();
        api.eliminarPaciente(paciente.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PacienteDetailActivity.this, "Paciente eliminado.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PacienteDetailActivity.this, "No se pudo eliminar el paciente.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PacienteDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Si se editó el paciente, actualizamos los datos en pantalla
        if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null) {
            Paciente actualizado = (Paciente) data.getSerializableExtra("pacienteActualizado");
            if (actualizado != null) {
                paciente = actualizado;
                mostrarDatos();
            }
        }
    }
}