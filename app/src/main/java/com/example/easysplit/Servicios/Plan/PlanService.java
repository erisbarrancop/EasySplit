package com.example.easysplit.Servicios.Plan;

import com.example.easysplit.Modelos.Plan;
import com.example.easysplit.Servicios.Plan.PlanRespuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlanService {
    @POST("/planes")
    Call<PlanRespuesta> crearPlan(@Body Plan plan);
}
