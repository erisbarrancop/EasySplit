package com.example.easysplit.Interfaces;

import java.util.List;

public class UsuarioRespuestaLista {

    public UsuarioRespuestaLista(List<UsuarioService> data) {
        this.data = data;
    }

    List<UsuarioService> data;

    public List<UsuarioService> getData() {
        return data;
    }

    public void setData(List<UsuarioService> data) {
        this.data = data;
    }


}
