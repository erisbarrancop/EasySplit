package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlanUsuarioService {

    // Define el endpoint para obtener los planes de un usuario específico
    @GET("usuariosplanes/usuario/{id_usuario}")
    Call<PlanRespuesta> obtenerPlanesPorUsuario(@Path("id_usuario") int idUsuario);
}
