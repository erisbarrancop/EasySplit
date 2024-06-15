package com.example.easysplit.Servicios.Usuario;

import com.example.easysplit.Modelos.Usuario;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsuarioPorPlanRespuesta {

    @SerializedName("data")
    private List<Usuario> usuarios;

    public UsuarioPorPlanRespuesta(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
