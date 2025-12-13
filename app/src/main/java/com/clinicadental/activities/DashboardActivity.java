package com.clinicadental.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.clinicadental.R;
import com.clinicadental.api.ApiClient;
import android.widget.TextView;
import com.clinicadental.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

       String saved = SessionManager.getToken(this);
       if (saved != null) {ApiClient.setBearerToken(saved);}

        // 1. Referencia al TextView
        TextView tvUsuarioLogueado = findViewById(R.id.tvUsuarioLogueado);

        // 2. Obtenemos el email guardado
        String email = SessionManager.getUserEmail(this);

        // 3. Derivamos un "nombre" a partir del email (antes del @) o usamos "Usuario"
        String nombreMostrado;
        if (email != null && email.contains("@")) {
            nombreMostrado = email.substring(0, email.indexOf("@"));
        } else if (email != null) {
            nombreMostrado = email;
        } else {
            nombreMostrado = "Usuario";
        }

        // 4. Mostramos el saludo
        tvUsuarioLogueado.setText("Hola, " + nombreMostrado);

        // Al tocar el nombre, aparecerá el menú "Cerrar sesión"
        tvUsuarioLogueado.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(DashboardActivity.this, v);
            popup.getMenuInflater().inflate(R.menu.menu_usuario, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opCerrarSesion) {

                    // Limpiar la sesión
                    SessionManager.clearSession(DashboardActivity.this);

                    // Ir al Login
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    return true;
                }
                return false;
            });

            popup.show();
        });


        MaterialCardView cardPacientes = findViewById(R.id.cardPacientes);
        cardPacientes.setOnClickListener(v -> startActivity(new Intent(this, PacientesActivity.class)));
    }
}