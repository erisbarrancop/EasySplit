package com.example.easysplit.Servicios.ParticipaGasto;

import com.example.easysplit.Modelos.ParticipaGasto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParticipaGastoRespuesta {
    private ParticipaGasto data;

    public ParticipaGastoRespuesta(ParticipaGasto data) {
        this.data = data;
    }

    public ParticipaGasto getData() {
        return data;
    }

    public void setData(ParticipaGasto data) {
        this.data = data;
    }
}
