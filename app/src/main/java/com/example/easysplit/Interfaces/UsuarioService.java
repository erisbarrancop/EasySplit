package com.example.easysplit.Interfaces;

import com.example.easysplit.Modelos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UsuarioService {
    @GET("/usuarios")
    Call<UsuarioRespuestaLista> obtenerListaUsuarios();

    @POST("/usuarios")
    Call<UsuarioRespuesta>crearUsuario(@Body Usuario usuario);
}
