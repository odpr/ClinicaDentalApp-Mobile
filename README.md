# ClinicaDentalApp-Mobile (Android, Java)

App móvil para la **Clínica Dental Dra. Yocaina Pérez**. Conecta con la API del sistema web (ASP.NET Core MVC) y permite gestionar pacientes y visualizar información básica.

## Requisitos
- Android Studio Jellyfish / Koala
- SDK 34, minSdk 24
- JDK 17

## Configuración rápida
1. Clonar este repositorio.
2. Abrir en Android Studio.
3. Editar `Constants.java` y poner la URL del servidor .NET:
   ```java
   public static final String BASE_URL = "http://10.0.2.2:5099/"; // o http://IP_LOCAL:PUERTO/
   ```
   - Para emulador Android y backend en la misma máquina, usa `10.0.2.2`.
4. Sincronizar Gradle y ejecutar en emulador/dispositivo.

## Endpoints esperados (ejemplo)
- `GET /api/pacientes` → Lista de pacientes (JSON)

## Estructura
```
app/src/main/java/com/clinicadental
 ├─ activities (Login, Dashboard, Pacientes)
 ├─ adapters (RecyclerView Adapter)
 ├─ models (Paciente POJO)
 ├─ api (Retrofit client + interfaces)
 └─ utils (Constants, SessionManager)
```

## Próximos pasos (Aun no implementados)
- Implementar autenticación real vía API.
- Crear Activities para Citas, Tratamientos, Pagos.
- Añadir Room para modo offline y sincronización.
- Notificaciones con FCM.
```

