package com.example.easysplit.Servicios.Gasto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GastoService {

    @GET("/gastos/{id_plan}")
    Call<GastoRespuesta> obtenerGastosPorPlan(@Path("id_plan") int idPlan);
}
