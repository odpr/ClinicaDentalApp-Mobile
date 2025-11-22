package com.clinicadental.api;

import android.util.Log;

import com.clinicadental.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static volatile Retrofit retrofit;
    private static volatile String bearerToken; // se setea tras login

    public static void setBearerToken(String token) {
        bearerToken = token;
        // no reiniciamos Retrofit; el interceptor leerá el valor actual
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {

                    // 1) Logs de red a nivel BODY (verás request/response + JSON)
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
                        // Evita loguear contraseñas en producción
                        Log.d("OKHTTP", message);
                    });
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // 2) Interceptor para el Authorization (si hay token)
                    Interceptor authInterceptor = chain -> {
                        Request req = chain.request();
                        if (bearerToken != null && !bearerToken.isEmpty()) {
                            req = req.newBuilder()
                                    .addHeader("Authorization", "Bearer " + bearerToken)
                                    .build();
                        }
                        return chain.proceed(req);
                    };

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .addInterceptor(authInterceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.BASE_URL) // DEBE terminar con '/'
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();

                    Log.d("API_BASE", "Retrofit apuntando a: " + Constants.BASE_URL);
                }
            }
        }
        return retrofit;
    }
}
