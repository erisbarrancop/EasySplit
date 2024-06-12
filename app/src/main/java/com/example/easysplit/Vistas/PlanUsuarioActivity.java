package com.example.easysplit.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.easysplit.Adapters.PlanAdapter;
import com.example.easysplit.Modelos.Plan;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.Plan.PlanRespuesta;
import com.example.easysplit.Servicios.Plan.PlanUsuarioService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlanUsuarioActivity extends AppCompatActivity {

    private ListView listViewPlanes;
    private PlanAdapter planAdapter;
    private PlanUsuarioService planUsuarioService;
    private SharedPreferences sharedPreferences;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        listViewPlanes = findViewById(R.id.listViewPlanes);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        url = getResources().getString(R.string.baseUrl);

        // Inicializar Retrofit y PlanUsuarioService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        planUsuarioService = retrofit.create(PlanUsuarioService.class);

        // Obtener el UserID del SharedPreferences
        int userId = sharedPreferences.getInt("UserID", 0);

        // Realizar la petición para obtener los planes del usuario
        obtenerPlanes(userId);

        listViewPlanes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void obtenerPlanes(int userId) {
        Call<PlanRespuesta> llamada = planUsuarioService.obtenerPlanesPorUsuario(userId);

        llamada.enqueue(new Callback<PlanRespuesta>() {
            @Override
            public void onResponse(Call<PlanRespuesta> call, Response<PlanRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> planes = response.body().getData();
                    planAdapter = new PlanAdapter(PlanUsuarioActivity.this, planes);
                    listViewPlanes.setAdapter(planAdapter);
                } else {
                    Toast.makeText(PlanUsuarioActivity.this, "No se encontraron planes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanRespuesta> call, Throwable t) {
                Toast.makeText(PlanUsuarioActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}