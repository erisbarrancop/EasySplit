package com.example.easysplit.Modelos;

import java.util.List;

public class Gasto {

    private int id;
    private String concepto;
    private float importe;
    private int id_plan;
    private int id_pagador;
    private String fecha;
    List<Integer> usuarios;

    public Gasto(String concepto, float importe, int id_pagador, List<Integer> selectedUserIds) {
        this.concepto = concepto;
        this.importe = importe;
        this.id_pagador = id_pagador;
        this.usuarios = selectedUserIds;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public int getId_pagador() {
        return id_pagador;
    }

    public void setId_pagador(int id_pagador) {
        this.id_pagador = id_pagador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
