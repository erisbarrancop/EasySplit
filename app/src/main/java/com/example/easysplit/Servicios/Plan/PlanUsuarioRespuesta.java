package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Modelos.Plan;

import java.util.List;

public class PlanUsuarioRespuesta {

    private List<Plan> data;

    public PlanUsuarioRespuesta(List<Plan> data) {
        this.data = data;
    }

    public List<Plan> getData() {
        return data;
    }

    public void setData(List<Plan> data) {
        this.data = data;
    }
}
