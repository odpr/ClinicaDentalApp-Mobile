
package com.clinicadental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clinicadental.R;
import com.clinicadental.adapters.PacienteAdapter;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.PacienteApi;
import com.clinicadental.models.Paciente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacientesActivity extends AppCompatActivity {

    private RecyclerView recyclerPacientes;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private FloatingActionButton fabAgregarPaciente;

    private PacienteAdapter adapter;

    // Códigos de request para saber de dónde viene el resultado
    private static final int REQ_NUEVO_PACIENTE = 1001;
    private static final int REQ_DETALLE_PACIENTE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);   // <-- tu layout actual

        recyclerPacientes = findViewById(R.id.recyclerPacientes);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        // ⚠️ Asegúrate de que en tu XML exista un FAB con este id (lo creamos luego)
        fabAgregarPaciente = findViewById(R.id.fabAgregarPaciente);

        recyclerPacientes.setLayoutManager(new LinearLayoutManager(this));

        // Click en botón flotante: crear nuevo paciente
        fabAgregarPaciente.setOnClickListener(v -> {
            Intent intent = new Intent(PacientesActivity.this, PacienteFormActivity.class);
            // En este caso, sin extras porque será "nuevo"
            startActivityForResult(intent, REQ_NUEVO_PACIENTE);
        });

        cargarPacientes();
    }

    private void cargarPacientes() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        recyclerPacientes.setVisibility(View.GONE);

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);
        api.getPacientes().enqueue(new Callback<List<Paciente>>() {
            @Override
            public void onResponse(@NonNull Call<List<Paciente>> call,
                                   @NonNull Response<List<Paciente>> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Paciente> lista = response.body();

                    if (lista.isEmpty()) {
                        tvEmpty.setText("Sin pacientes registrados");
                        tvEmpty.setVisibility(View.VISIBLE);
                        recyclerPacientes.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        recyclerPacientes.setVisibility(View.VISIBLE);

                        // Adapter con click que abre detalle del paciente
                        adapter = new PacienteAdapter(lista, paciente -> {
                            // Aquí dejamos de usar solo Toast y abrimos la pantalla de detalle
                            Intent intent = new Intent(
                                    PacientesActivity.this,
                                    PacienteDetalleActivity.class
                            );

                            // Pasamos el ID del paciente (ajusta getId() si tu modelo usa otro nombre)
                            intent.putExtra("pacienteId", paciente.getId());

                            // Si quieres, también puedes pasar el nombre para mostrar rápido en detalle
                            // intent.putExtra("pacienteNombre", paciente.getNombreCompleto());

                            startActivityForResult(intent, REQ_DETALLE_PACIENTE);
                        });

                        recyclerPacientes.setAdapter(adapter);
                    }
                } else {
                    tvEmpty.setText("Error al cargar pacientes (" + response.code() + ")");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Paciente>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText("Error de red: " + t.getMessage());
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Siempre que volvamos de crear/editar/eliminar, recargamos la lista
        if ((requestCode == REQ_NUEVO_PACIENTE || requestCode == REQ_DETALLE_PACIENTE)
                && resultCode == RESULT_OK) {

            cargarPacientes();
        }
    }
}