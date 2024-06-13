package com.example.easysplit.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easysplit.Adapters.GastoAdapter;
import com.example.easysplit.Modelos.Gasto;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.Gasto.GastoRespuesta;
import com.example.easysplit.Servicios.Gasto.GastoService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GastoActivity extends AppCompatActivity {

    private int planId;
    private String planName;
    private ListView listViewGastos;
    private GastoService gastoService;
    private GastoAdapter gastoAdapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        // Retrieve the planId and planName from the intent
        planId = getIntent().getIntExtra("selectedPlan", 0);
        planName = getIntent().getStringExtra("planName");

        // Set the plan name in the TextView
        TextView currentPlanName = findViewById(R.id.textViewCurrentPlan);
        currentPlanName.setText(planName);

        listViewGastos = findViewById(R.id.listViewCurrentPlan);
        url = getResources().getString(R.string.baseUrl);

        // Initialize Retrofit and GastoService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gastoService = retrofit.create(GastoService.class);

        obtenerGastos(planId);
    }

    private void obtenerGastos(int planId) {
        Call<GastoRespuesta> llamada = gastoService.obtenerGastosPorPlan(planId);

        llamada.enqueue(new Callback<GastoRespuesta>() {
            @Override
            public void onResponse(Call<GastoRespuesta> call, Response<GastoRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Gasto> gastos = response.body().getData();
                    gastoAdapter = new GastoAdapter(GastoActivity.this, gastos);
                    listViewGastos.setAdapter(gastoAdapter);
                } else {
                    Toast.makeText(GastoActivity.this, "No se encontraron gastos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GastoRespuesta> call, Throwable t) {
                Toast.makeText(GastoActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}