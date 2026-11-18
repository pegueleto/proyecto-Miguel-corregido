package com.estudiantes.model;

public class Student {

    private int id;
    private String nombre;
    private String correo;
    private String numeroTelefono;
    private String idioma;

    public Student() {
    }

    public Student(int id, String nombre, String correo, String numeroTelefono, String idioma) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.numeroTelefono = numeroTelefono;
        this.idioma = idioma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
