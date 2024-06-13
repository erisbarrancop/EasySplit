package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Modelos.Plan;

public class PlanRespuesta {
    private Plan data;
    private String message;

    public Plan getData() {
        return data;
    }

    public void setData(Plan data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
