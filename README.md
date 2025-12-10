# Clínica Dental Dra. Yocaina Pérez – Aplicación Móvil Android

## Descripción del Proyecto

La aplicación móvil **Clínica Dental Dra. Yocaina Pérez** complementa la plataforma web desarrollada en **ASP.NET Core MVC + API**, permitiendo que pacientes, doctores y personal administrativo accedan a servicios clínicos directamente desde sus dispositivos Android.

La app ha sido diseñada para ofrecer una experiencia **moderna, rápida y segura**, permitiendo:

* Autenticación real mediante **API con JWT**
* Consulta de pacientes desde el backend
* Interfaz visual con **Material Design**
* Navegación móvil sencilla y adaptable

Esta aplicación se conecta directamente con el backend clínico, garantizando datos actualizados en tiempo real.

---

## Exposición del Problema

Aunque existe un sistema web funcional, depender exclusivamente del navegador limita la accesibilidad y la rapidez con que los usuarios interactúan con la clínica.

La versión móvil permite:

* Acceso inmediato a citas, pacientes e información clínica
* Mayor portabilidad
* Mayor eficiencia para doctores y personal administrativo
* Extracción de datos mediante API sin abrir un navegador

> **Meta:** ofrecer acceso rápido, seguro y centralizado desde un dispositivo móvil.

---

## Plataforma y Tecnologías

###  **Aplicación Móvil (Android)**

Debido al alcance del proyecto actual, la app está desarrollada en:

* **Lenguaje:** Java
* **IDE:** Android Studio
* **Diseño:** Material Design (Material Components)
* **Red:** Retrofit2 + Gson
* **UI Dinámica:** RecyclerView + Adapters
* **Persistencia:** SharedPreferences (para token JWT)

### **Backend Integrado**

* ASP.NET Core 8 – API REST
* Entity Framework Core
* SQL Server
* Identity (Usuarios y Roles)
* Autenticación por **JWT**
* Endpoint especial para Android

---

## Autenticación y Seguridad

El inicio de sesión se realiza mediante:

```
POST /api/auth/login
```

Si las credenciales son válidas, el backend genera un **token JWT**, el cual se almacena en la app mediante `SessionManager`.

Este token se usa para las peticiones protegidas al backend.

---

## Módulo Actual: Gestión de Pacientes

La aplicación tiene implementado un módulo funcional que:

* Consume el endpoint

  ```
  GET /api/pacientes/list
  ```
* Obtiene lista de pacientes desde SQL Server
* Los muestra en tarjetas Material Design
* Incluye manejo de errores
* Muestra loader mientras se cargan datos
* Soporta clic sobre cada paciente para funciones futuras

### Modelo de Paciente (Android)

```java
public class Paciente {
    private int id;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String fechaNacimiento;
    private String direccion;
}
```

### Conexión API (Retrofit)

```java
@GET("pacientes/list")
Call<List<Paciente>> getPacientes();
```

### URL Base para emulador

```java
public static final String BASE_URL = "http://10.0.2.2:5099/api/";
```

---

## Interfaz de Usuario

### **Funcionalidades visuales implementadas:**

* Tarjetas modernas con **MaterialCardView**
* Listas dinámicas con **RecyclerView**
* Indicador de carga (**ProgressBar**)
* Mensajes para:

  * Sin conexión
  * Sin datos
  * Error del servidor
* Pantallas optimizadas para móvil

### Pantallas Implementadas

1. **LoginActivity**

   * Autenticación en API
   * Guardado de token
   * Redirección a Dashboard

2. **DashboardActivity**

   * Acceso a módulo (Pacientes)

3. **PacientesActivity**

   * Lista de pacientes cargada desde backend real
   * Vista moderna con Material Design
   * Loader + manejo de errores

---

## Backend – Endpoint Especial para Android

```csharp
// LISTA Pacientes
[HttpGet("list")]
public async Task<ActionResult<IEnumerable<PacienteApiDto>>> GetPacientesApi()
{
    var pacientes = await _context.Pacientes
        .OrderBy(p => p.Nombres)
        .Select(p => new PacienteApiDto {
            Id = p.Id,
            NombreCompleto = p.Nombres,
            Telefono = p.Telefono ?? "",
            Email = p.Email ?? "",
            FechaNacimiento = p.FechaNacimiento?.ToString("yyyy-MM-dd") ?? "",
            Direccion = p.Direccion ?? ""
        })
        .ToListAsync();

    return Ok(pacientes);
}

// DETALLE
[HttpGet("{id:int}")]
[AllowAnonymous] // o [Authorize(AuthenticationSchemes = "Bearer")] si lo quieres protegido
public async Task<ActionResult<PacienteApiDto>> GetPaciente(int id)
{
    var p = await _context.Pacientes.FindAsync(id);
    if (p == null) return NotFound();

    var dto = new PacienteApiDto
    {
        Id = p.Id,
        NombreCompleto = p.Nombre,
        Telefono = p.Telefono ?? "",
        Email = p.Email ?? "",
        FechaNacimiento = p.FechaNacimiento.HasValue
            ? p.FechaNacimiento.Value.ToString("yyyy-MM-dd")
            : "",
        Direccion = p.Direccion ?? ""
    };

    return Ok(dto);
}

// CREAR
[HttpPost]
[AllowAnonymous] // o Authorize si ya usas JWT en la app
public async Task<ActionResult<PacienteApiDto>> CreatePaciente([FromBody] PacienteCreateUpdateDto model)
{
    if (!ModelState.IsValid) return BadRequest(ModelState);

    var paciente = new Paciente
    {
        Nombre = model.NombreCompleto,
        Telefono = model.Telefono,
        Email = model.Email,
        Direccion = model.Direccion,
        FechaNacimiento = string.IsNullOrWhiteSpace(model.FechaNacimiento)
            ? null
            : DateTime.Parse(model.FechaNacimiento)
    };

    _context.Pacientes.Add(paciente);
    await _context.SaveChangesAsync();

    var dto = new PacienteApiDto
    {
        Id = paciente.Id,
        NombreCompleto = paciente.Nombre,
        Telefono = paciente.Telefono ?? "",
        Email = paciente.Email ?? "",
        FechaNacimiento = paciente.FechaNacimiento?.ToString("yyyy-MM-dd") ?? "",
        Direccion = paciente.Direccion ?? ""
    };

    return CreatedAtAction(nameof(GetPaciente), new { id = paciente.Id }, dto);
}

// EDITAR
[HttpPut("{id:int}")]
[AllowAnonymous]
public async Task<IActionResult> UpdatePaciente(int id, [FromBody] PacienteCreateUpdateDto model)
{
    var paciente = await _context.Pacientes.FindAsync(id);
    if (paciente == null) return NotFound();

    paciente.Nombre = model.NombreCompleto;
    paciente.Telefono = model.Telefono;
    paciente.Email = model.Email;
    paciente.Direccion = model.Direccion;
    paciente.FechaNacimiento = string.IsNullOrWhiteSpace(model.FechaNacimiento)
        ? null
        : DateTime.Parse(model.FechaNacimiento);

    await _context.SaveChangesAsync();
    return NoContent();
}

// ELIMINAR
[HttpDelete("{id:int}")]
[AllowAnonymous]
public async Task<IActionResult> DeletePaciente(int id)
{
    var paciente = await _context.Pacientes.FindAsync(id);
    if (paciente == null) return NotFound();

    _context.Pacientes.Remove(paciente);
    await _context.SaveChangesAsync();
    return NoContent();
}

```

---

## Estructura del Repositorio

```
ClinicaDentalApp-Mobile/
│
├── app/
│   ├── java/com/clinicadental/
│   │   ├── activities/       # Pantallas
│   │   ├── adapters/         # RecyclerView Adapters
│   │   ├── api/              # Retrofit Client + Interfaces
│   │   ├── models/           # Modelos Java
│   │   └── utils/            # Token y constantes
│   └── res/
│       └── layout/           # XML de interfaz
│
├── docs/                     # Documentación adicional
├── api/                      # Información de conexión con backend
├── design/                   # Mockups o diseños (si aplica)
├── build.gradle              
└── README.md
```

---

## Funcionalidades Futuras

### Próximas mejoras en la app:

* CRUD móvil de pacientes
* Gestión de citas desde Android
* Notificaciones push (FCM)
* Implementación de roles (doctor / admin)
* Sincronización offline
* Diseño mejorado con transiciones

---

## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/ClinicaDentalApp-Mobile
```

### 2. Abrir en Android Studio

### 3. Ejecutar backend ASP.NET Core

```bash
dotnet run
```

Debe correr en:

```
http://localhost:5099
```

### 4. Ejecutar la app en emulador Android

El emulador se conectará usando:

```
http://10.0.2.2:5099/api/
```

---

## Autor

**Osvaldo Darwin Perez**
Aplicación móvil integrada para la plataforma Clínica Dental Dra. Yocaina Pérez.
