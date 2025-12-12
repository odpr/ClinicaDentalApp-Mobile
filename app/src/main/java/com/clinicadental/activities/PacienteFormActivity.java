package com.clinicadental.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.PacienteApi;
import com.clinicadental.models.Paciente;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteFormActivity extends AppCompatActivity {

    private TextInputEditText edtNombre, edtTelefono, edtEmail, edtFechaNac, edtDireccion;
    private Button btnGuardarPaciente;
    private ProgressBar progressBarForm;

    // -1 = nuevo, != -1 = editar
    private int pacienteId = -1;
    private Paciente pacienteActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_form);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarPacienteForm);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Obtener id que viene del intent (si es edición)
        pacienteId = getIntent().getIntExtra(PacienteDetalleActivity.EXTRA_PACIENTE_ID, -1);

        if (pacienteId == -1) {
            toolbar.setTitle("Nuevo paciente");
        } else {
            toolbar.setTitle("Editar paciente");
        }

        // Referencias UI
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtEmail = findViewById(R.id.edtEmail);
        edtFechaNac = findViewById(R.id.edtFechaNac);
        edtDireccion = findViewById(R.id.edtDireccion);
        btnGuardarPaciente = findViewById(R.id.btnGuardarPaciente);
        progressBarForm = findViewById(R.id.progressBarForm);

        btnGuardarPaciente.setOnClickListener(v -> {
            if (validarCampos()) {
                if (pacienteId == -1) {
                    crearPaciente();
                } else {
                    actualizarPaciente();
                }
            }
        });

        // Si se quiere editar, cargamos los datos desde la API
        if (pacienteId != -1) {
            cargarPaciente();
        }
    }

    private void mostrarCargando(boolean cargando) {
        progressBarForm.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnGuardarPaciente.setEnabled(!cargando);
        edtNombre.setEnabled(!cargando);
        edtTelefono.setEnabled(!cargando);
        edtEmail.setEnabled(!cargando);
        edtFechaNac.setEnabled(!cargando);
        edtDireccion.setEnabled(!cargando);
    }

    private boolean validarCampos() {
        boolean ok = true;

        if (edtNombre.getText() == null || edtNombre.getText().toString().trim().isEmpty()) {
            edtNombre.setError("Requerido");
            ok = false;
        }

        // Podemos agregar más validaciones si deseamos. Por ejemplo:
        // - formato de email
        // - longitud de teléfono
        // - etc.

        return ok;
    }

    private void cargarPaciente() {
        mostrarCargando(true);

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);
        api.getPaciente(pacienteId).enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(@NonNull Call<Paciente> call,
                                   @NonNull Response<Paciente> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null) {
                    pacienteActual = response.body();
                    rellenarFormulario(pacienteActual);
                } else {
                    Toast.makeText(PacienteFormActivity.this,
                            "Error al cargar paciente (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    finish(); // No tiene sentido seguir en formulario sin datos
                }
            }

            @Override
            public void onFailure(@NonNull Call<Paciente> call, @NonNull Throwable t) {
                mostrarCargando(false);
                Toast.makeText(PacienteFormActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void rellenarFormulario(Paciente p) {

        edtNombre.setText(p.getNombreCompleto());
        edtTelefono.setText(p.getTelefono());
        edtEmail.setText(p.getEmail());
        edtFechaNac.setText(p.getFechaNacimiento());
        edtDireccion.setText(p.getDireccion());
    }

    private void crearPaciente() {
        mostrarCargando(true);

        Paciente nuevo = new Paciente();

        nuevo.setNombreCompleto(edtNombre.getText().toString().trim());
        nuevo.setTelefono(edtTelefono.getText().toString().trim());
        nuevo.setEmail(edtEmail.getText().toString().trim());
        nuevo.setFechaNacimiento(edtFechaNac.getText().toString().trim());
        nuevo.setDireccion(edtDireccion.getText().toString().trim());

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);

        api.createPaciente(nuevo).enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(@NonNull Call<Paciente> call,
                                   @NonNull Response<Paciente> response) {
                mostrarCargando(false);

                if (response.isSuccessful()) {
                    Toast.makeText(PacienteFormActivity.this,
                            "Paciente creado correctamente",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PacienteFormActivity.this,
                            "No se pudo crear el paciente (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Paciente> call, @NonNull Throwable t) {
                mostrarCargando(false);
                Toast.makeText(PacienteFormActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarPaciente() {
        if (pacienteActual == null) {
            // Por seguridad: si por alguna razón no cargó antes
            pacienteActual = new Paciente();
            pacienteActual.setId(pacienteId);
        }

        mostrarCargando(true);

        // Actualizamos datos del objeto
        pacienteActual.setNombreCompleto(edtNombre.getText().toString().trim());
        pacienteActual.setTelefono(edtTelefono.getText().toString().trim());
        pacienteActual.setEmail(edtEmail.getText().toString().trim());
        pacienteActual.setFechaNacimiento(edtFechaNac.getText().toString().trim());
        pacienteActual.setDireccion(edtDireccion.getText().toString().trim());

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);

        api.updatePaciente(pacienteId, pacienteActual).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                mostrarCargando(false);

                if (response.isSuccessful()) {
                    Toast.makeText(PacienteFormActivity.this,
                            "Paciente actualizado correctamente",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PacienteFormActivity.this,
                            "No se pudo actualizar el paciente (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                mostrarCargando(false);
                Toast.makeText(PacienteFormActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }