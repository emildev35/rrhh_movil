package com.example.franzemil.rrhhmovil.models;


import android.graphics.Bitmap;

/**
 * Clase de gestion para los usuarios de la aplicaciones moviles
 */

public class Usuario {

    private String ci;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String fullName;
    private int relacionLaboral;
    private String cargo;
    private String token;
    private Bitmap fotografia;
    private String unidad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Bitmap getFotografia() {
        return fotografia;
    }

    public void setFotografia(Bitmap fotografia) {
        this.fotografia = fotografia;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRelacionLaboral() {
        return relacionLaboral;
    }

    public void setRelacionLaboral(int relacionLaboral) {
        this.relacionLaboral = relacionLaboral;
    }
}
