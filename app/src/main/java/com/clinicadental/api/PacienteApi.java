package com.clinicadental.api;

import com.clinicadental.models.Paciente;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PacienteApi {
    @GET("api/pacientes")
    Call<List<Paciente>> getPacientes();
}
