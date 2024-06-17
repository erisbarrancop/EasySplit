package com.example.easysplit.Servicios.Usuario;

import com.example.easysplit.Modelos.PlanUsuario;
import com.example.easysplit.Modelos.Usuario;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsuarioPorPlanRespuesta {

    private List<PlanUsuario> data;

    public UsuarioPorPlanRespuesta(List<PlanUsuario> data) {
        this.data = data;
    }

    public List<PlanUsuario> getData() {
        return data;
    }

    public void setData(List<PlanUsuario> data) {
        this.data = data;
    }
}

