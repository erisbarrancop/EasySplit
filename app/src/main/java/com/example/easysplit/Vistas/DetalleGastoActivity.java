package com.example.easysplit.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easysplit.Adapters.GastoAdapter;
import com.example.easysplit.Adapters.ParticipaGastoAdapter;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.ParticipaGasto.ParticipaGastoRespuesta;
import com.example.easysplit.Servicios.ParticipaGasto.ParticipaGastoService;
import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;
import com.example.easysplit.Servicios.Usuario.UsuarioService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleGastoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String url;
    private ParticipaGastoAdapter participaGastoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_gasto);

        String concepto = getIntent().getStringExtra("concepto");
        float importe = getIntent().getFloatExtra("importe", 0);
        int idPagador = getIntent().getIntExtra("idPagador", 0);

        TextView textViewExpenseName = findViewById(R.id.textViewCurrentExpense);
        TextView textViewPagador = findViewById(R.id.nombrePagadorGasto);
        TextView textViewImporte = findViewById(R.id.importeGasto);
        ListView listViewDetalleGasto = findViewById(R.id.listViewCurrentExpense);



        url = getResources().getString(R.string.baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioService = retrofit.create(UsuarioService.class);

        textViewExpenseName.setText(concepto);

        Call<UsuarioRespuesta> llamada = usuarioService.obtenerUsuarioPorID(idPagador);
        llamada.enqueue(new Callback<UsuarioRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                String nombrePagador = response.body().getData().getNombre();
                textViewPagador.setText(nombrePagador);
                textViewImporte.setText("Total del gasto: " + (int) importe + " €");
            }

            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
            }
        });
    }
}