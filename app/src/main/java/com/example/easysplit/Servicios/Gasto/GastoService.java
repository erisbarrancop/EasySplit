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

    @POST("/gastos/{planId}/crear")
    Call<Void> crearGasto(@Path("planId") int planId, @Body CrearGastoRequest request);

    class CrearGastoRequest {
        private Gasto gasto;
        private List<Integer> usuarios;

        public CrearGastoRequest(Gasto gasto, List<Integer> usuarios) {
            this.gasto = gasto;
            this.usuarios = usuarios;
        }

        public Gasto getGasto() {
            return gasto;
        }

        public void setGasto(Gasto gasto) {
            this.gasto = gasto;
        }

        public List<Integer> getUsuarios() {
            return usuarios;
        }

        public void setUsuarios(List<Integer> usuarios) {
            this.usuarios = usuarios;
        }
    }
}
