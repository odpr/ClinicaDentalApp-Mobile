package com.clinicadental.api;

import com.clinicadental.models.Paciente;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PacienteApi {

    // LISTA
    @GET("Pacientes/list")
    Call<List<Paciente>> getPacientes();

    // DETALLE
    @GET("Pacientes/{id}")
    Call<Paciente> getPaciente(@Path("id") int id);

    // CREAR
    @POST("Pacientes")
    Call<Paciente> crearPaciente(@Body Paciente paciente);

    // EDITAR
    @PUT("Pacientes/{id}")
    Call<Void> actualizarPaciente(@Path("id") int id, @Body Paciente paciente);

    // ELIMINAR
    @DELETE("Pacientes/{id}")
    Call<Void> eliminarPaciente(@Path("id") int id);
}