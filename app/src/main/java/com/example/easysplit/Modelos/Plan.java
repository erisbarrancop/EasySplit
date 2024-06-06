package com.example.easysplit.Modelos;

public class Plan {

    private int id;

    private String nombre;
    private String moneda;
    private int id_admin;

    public Plan(String nombre, String moneda, int id_admin) {
        this.nombre = nombre;
        this.moneda = moneda;
        this.id_admin = id_admin;
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

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }
}
