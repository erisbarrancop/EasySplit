package com.example.easysplit.Servicios.ParticipaGasto;

import com.example.easysplit.Servicios.Gasto.GastoRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ParticipaGastoService {

    @GET("/participagastos/{id_gasto}")
    Call<ParticipaGastoRespuesta> obtenerDetallesGasto(@Path("id_gasto") int idGasto);

}
