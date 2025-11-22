package com.clinicadental.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clinicadental.R;
import com.clinicadental.adapters.PacienteAdapter;
import com.clinicadental.api.ApiClient;
import com.clinicadental.api.PacienteApi;
import com.clinicadental.models.Paciente;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacientesActivity extends AppCompatActivity {

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        recycler = findViewById(R.id.recyclerPacientes);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        PacienteApi api = ApiClient.getClient().create(PacienteApi.class);
        api.getPacientes().enqueue(new Callback<List<Paciente>> () {
            @Override
            public void onResponse(Call<List<Paciente>> call, Response<List<Paciente>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    recycler.setAdapter(new PacienteAdapter(response.body()));
                } else {
                    Toast.makeText(PacientesActivity.this, "Sin datos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Paciente>> call, Throwable t) {
                Toast.makeText(PacientesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}