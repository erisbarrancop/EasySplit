package com.example.easysplit.Interfaces.Usuario;

import com.example.easysplit.Modelos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioService {
    @GET("/usuarios")
    Call<UsuarioRespuestaLista> obtenerListaUsuarios();

    @POST("/usuarios")
    Call<UsuarioRespuesta> crearUsuario(@Body Usuario usuario);

    @GET("/usuarios/{username}")
    Call<UsuarioRespuesta> obtenerUsuarioPorUsername(@Path("username") String username);
}
