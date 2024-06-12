package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlanService {
    @GET("usuariosplanes/{id_usuario}")
    Call<PlanRespuesta> obtenerPlanesPorUsuario(@Path("id_usuario") int idUsuario);
}
