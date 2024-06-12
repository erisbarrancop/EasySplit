package com.example.easysplit.Servicios.Gasto;

import com.example.easysplit.Modelos.Gasto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GastoRespuesta {
    @SerializedName("data")
    private List<Gasto> data;

    public List<Gasto> getData() {
        return data;
    }

    public void setData(List<Gasto> data) {
        this.data = data;
    }
}
