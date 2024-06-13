package com.example.easysplit.Servicios.Plan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlanUsuarioService {

    // Define el endpoint para obtener los planes de un usuario específico
    @GET("usuariosplanes/usuario/{id_usuario}")
    Call<PlanUsuarioRespuesta> obtenerPlanesPorUsuario(@Path("id_usuario") int idUsuario);

    @POST("/usuariosplanes/{planId}/{userId}")
    Call<Void> crearRelacionUsuarioPlan(@Path("planId") int planId, @Path("userId") int userId);
}
