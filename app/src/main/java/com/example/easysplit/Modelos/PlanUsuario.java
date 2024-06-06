package com.example.easysplit.Modelos;

public class PlanUsuario {
    private int id_usuario;
    private int id_plan;

    public PlanUsuario(int id_usuario, int id_plan) {
        this.id_usuario = id_usuario;
        this.id_plan = id_plan;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }
}

