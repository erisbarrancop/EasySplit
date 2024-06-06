package com.example.easysplit.Interfaces.Usuario;

import com.example.easysplit.Modelos.Usuario;

public class UsuarioRespuesta {

    Usuario data;

    public UsuarioRespuesta(Usuario data) {
        this.data = data;
    }

    public Usuario getData() {
        return data;
    }

    public void setData(Usuario data) {
        this.data = data;
    }
}
