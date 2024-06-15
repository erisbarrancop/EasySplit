package com.example.easysplit.Servicios.Usuario;

import com.example.easysplit.Modelos.Usuario;
import java.util.List;

public class UsuarioRespuestaLista {

    private List<Usuario> data;

    public UsuarioRespuestaLista(List<Usuario> data) {
        this.data = data;
    }

    public List<Usuario> getUsuarios() {
        return data;
    }

    public void setUsuarios(List<Usuario> data) {
        this.data = data;
    }
}
