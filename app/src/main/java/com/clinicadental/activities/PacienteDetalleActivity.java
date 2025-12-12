package com.clinicadental.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.PacienteApi;
import com.clinicadental.models.Paciente;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteDetalleActivity extends AppCompatActivity {

    public static final String EXTRA_PACIENTE_ID = "pacienteId";

    private TextView tvNombreDetalle, tvTelefonoDetalle, tvEmailDetalle,
            tvCedulaDetalle, tvDireccionDetalle, tvErrorDetalle;
    private ProgressBar progressBarDetalle;
    private LinearLayout layoutDatosPaciente;
    private Button btnEditarPaciente, btnEliminarPaciente;

    private int pacienteId;
    private Paciente paciente;

    private static final int REQ_EDITAR_PACIENTE = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_detalle);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarDetallePaciente);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Detalle del paciente");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Obtener id del Intent
        pacienteId = getIntent().getIntExtra(EXTRA_PACIENTE_ID, -1);
        if (pacienteId == -1) {
            Toast.makeText(this, "Paciente no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Referencias UI
        layoutDatosPaciente = findViewById(R.id.layoutDatosPaciente);
        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvTelefonoDetalle = findViewById(R.id.tvTelefonoDetalle);
        tvEmailDetalle = findViewById(R.id.tvEmailDetalle);
        tvCedulaDetalle = findViewById(R.id.tvCedulaDetalle);
        tvDireccionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvErrorDetalle = findViewById(R.id.tvErrorDetalle);

        progressBarDetalle = findViewById(R.id.progressBarDetalle);

        btnEditarPaciente = findViewById(R.id.btnEditarPaciente);
        btnEliminarPaciente = findViewById(R.id.btnEliminarPaciente);

        // Acciones botones
        btnEditarPaciente.setOnClickListener(v -> abrirEdicion());
        btnEliminarPaciente.setOnClickListener(v -> confirmarEliminacion());

        cargarDetallePaciente();
    }

    private void cargarDetallePaciente() {
        mostrarCargando(true);
        tvErrorDetalle.setVisibility(View.GONE);

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);

        api.getPaciente(pacienteId).enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(@NonNull Call<Paciente> call,
                                   @NonNull Response<Paciente> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null) {
                    paciente = response.body();
                    mostrarDatos(paciente);
                } else {
                    tvErrorDetalle.setText("Error al cargar el paciente (" + response.code() + ")");
                    tvErrorDetalle.setVisibility(View.VISIBLE);
                    layoutDatosPaciente.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Paciente> call, @NonNull Throwable t) {
                mostrarCargando(false);
                tvErrorDetalle.setText("Error de red: " + t.getMessage());
                tvErrorDetalle.setVisibility(View.VISIBLE);
                layoutDatosPaciente.setVisibility(View.GONE);
            }
        });
    }

    private void mostrarCargando(boolean cargando) {
        progressBarDetalle.setVisibility(cargando ? View.VISIBLE : View.GONE);
        layoutDatosPaciente.setVisibility(cargando ? View.GONE : View.VISIBLE);
        btnEditarPaciente.setEnabled(!cargando);
        btnEliminarPaciente.setEnabled(!cargando);
    }

    private void mostrarDatos(Paciente p) {
        layoutDatosPaciente.setVisibility(View.VISIBLE);
        tvErrorDetalle.setVisibility(View.GONE);

        // Ajusta estos getters a tu modelo real
        // Ejemplo: p.getNombreCompleto(), p.getTelefono(), etc.
        tvNombreDetalle.setText(p.getNombreCompleto());
        tvTelefonoDetalle.setText(p.getTelefono());
        tvEmailDetalle.setText(p.getEmail());
        tvCedulaDetalle.setText(p.getCedula());
        tvDireccionDetalle.setText(p.getDireccion());
    }

    private void abrirEdicion() {
        Intent intent = new Intent(this, PacienteFormActivity.class);
        // Le pasamos el id para que el formulario cargue los datos desde la API
        intent.putExtra(EXTRA_PACIENTE_ID, pacienteId);
        startActivityForResult(intent, REQ_EDITAR_PACIENTE);
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
        mostrarCargando(true);

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);

        api.deletePaciente(pacienteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                mostrarCargando(false);
                if (response.isSuccessful()) {
                    Toast.makeText(PacienteDetalleActivity.this,
                            "Paciente eliminado correctamente",
                            Toast.LENGTH_SHORT).show();

                    // Avisamos a PacientesActivity que hubo cambios
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PacienteDetalleActivity.this,
                            "No se pudo eliminar el paciente (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                mostrarCargando(false);
                Toast.makeText(PacienteDetalleActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_EDITAR_PACIENTE && resultCode == RESULT_OK) {
            // Se editó el paciente en el formulario.
            // Podemos recargar sus datos desde la API
            cargarDetallePaciente();

            // Además, avisamos a PacientesActivity para que recargue la lista
            setResult(RESULT_OK);
        }
    }
}
