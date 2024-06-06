package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Modelos.Plan;

import java.util.List;

public class PlanRespuesta {

    private List<Plan> data;

    public PlanRespuesta(List<Plan> data) {
        this.data = data;
    }

    public List<Plan> getData() {
        return data;
    }

    public void setData(List<Plan> data) {
        this.data = data;
    }
}
