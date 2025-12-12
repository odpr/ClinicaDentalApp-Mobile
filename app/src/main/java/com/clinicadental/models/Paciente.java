package com.clinicadental.models;

public class Paciente {
    private int id;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String fechaNacimiento;
    private String direccion;

    public int getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getDireccion() { return direccion; }

    public void setId(int id) { this.id = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
