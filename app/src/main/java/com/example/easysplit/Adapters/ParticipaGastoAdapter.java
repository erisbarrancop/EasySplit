package com.example.easysplit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.easysplit.Modelos.Gasto;
import com.example.easysplit.Modelos.ParticipaGasto;
import com.example.easysplit.Modelos.Usuario;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.ParticipaGasto.ParticipaGastoRespuesta;
import com.example.easysplit.Servicios.ParticipaGasto.ParticipaGastoService;
import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;
import com.example.easysplit.Servicios.Usuario.UsuarioService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipaGastoAdapter extends ArrayAdapter<ParticipaGasto> {

    private String url;
    public ParticipaGastoAdapter(Context context, List<ParticipaGasto> participaGastos) {
        super(context, 0, participaGastos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParticipaGasto participaGasto = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gasto, parent, false);
        }

        TextView textViewConcepto = convertView.findViewById(R.id.textViewConcepto);
        TextView textViewImporte = convertView.findViewById(R.id.textViewImporte);

        int id_usuario = participaGasto.getId_usuario();
        int id_gasto = participaGasto.getId_gasto();

        url = "http://erisbarrancop.eu.pythonanywhere.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioService =  retrofit.create(UsuarioService.class);
        ParticipaGastoService participaGastoService = retrofit.create(ParticipaGastoService.class);
        Call<UsuarioRespuesta> llamada = usuarioService.obtenerUsuarioPorID(id_usuario);


        llamada.enqueue(new Callback<UsuarioRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                String detailExpenseUsername = response.body().getData().getNombre();
                textViewConcepto.setText(detailExpenseUsername);
            }

            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {

            }
        });

        Call<ParticipaGastoRespuesta> llamadaDetalle = participaGastoService.obtenerDetallesGasto(id_gasto);
        llamadaDetalle.enqueue(new Callback<ParticipaGastoRespuesta>() {
            @Override
            public void onResponse(Call<ParticipaGastoRespuesta> call, Response<ParticipaGastoRespuesta> response) {

                float importeDetalle = response.body().getData().getImporte();
                textViewImporte.setText((int) importeDetalle + " €");

            }

            @Override
            public void onFailure(Call<ParticipaGastoRespuesta> call, Throwable t) {

            }
        });
        return convertView;
    }
}
