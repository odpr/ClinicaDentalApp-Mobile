package com.clinicadental.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    class LoginRequest {
        public String email, password;
        public LoginRequest(String e, String p){ email=e; password=p; }
    }
    class LoginResponse {
        public String token, Nombre, Rol;
    }
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}