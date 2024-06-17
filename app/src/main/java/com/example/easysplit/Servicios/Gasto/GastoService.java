package com.example.easysplit.Servicios.Gasto;

import com.example.easysplit.Modelos.Gasto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface GastoService {

    @GET("/gastos/{id_plan}")
    Call<GastoRespuesta> obtenerGastosPorPlan(@Path("id_plan") int idPlan);

    @POST("gastos/{id_plan}")
    Call<Void> crearGasto(
            @Path("id_plan") int idPlan,
            @Body Gasto gasto
    );


}
