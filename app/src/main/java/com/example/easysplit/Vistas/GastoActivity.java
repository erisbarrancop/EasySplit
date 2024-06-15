package com.example.easysplit.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easysplit.Adapters.GastoAdapter;
import com.example.easysplit.Adapters.UserCheckboxSpinnerAdapter;
import com.example.easysplit.Modelos.Gasto;
import com.example.easysplit.Modelos.PlanUsuario;
import com.example.easysplit.Modelos.Usuario;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.Gasto.GastoRespuesta;
import com.example.easysplit.Servicios.Gasto.GastoService;
import com.example.easysplit.Servicios.Plan.PlanUsuarioRespuesta;
import com.example.easysplit.Servicios.Plan.PlanUsuarioService;
import com.example.easysplit.Servicios.Usuario.UsuarioPorPlanRespuesta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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
    private SharedPreferences sharedPreferences;
    private String url;
    private int idPagador;
    private PlanUsuarioService planUsuarioService;
    private Spinner spinnerUsuarios;
    private UserCheckboxSpinnerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        // Retrieve the planId and planName from the intent
        planId = getIntent().getIntExtra("selectedPlan", 0);
        planName = getIntent().getStringExtra("planName");
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

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
        planUsuarioService = retrofit.create(PlanUsuarioService.class);

        idPagador = sharedPreferences.getInt("UserID", 0);

        obtenerGastos(planId);

        FloatingActionButton fabAddGasto = findViewById(R.id.fab_gasto);

        fabAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCrearGasto();
            }
        });
    }

    private void mostrarDialogoCrearGasto() {
        Dialog dialog = new Dialog(GastoActivity.this);
        View dialogView = LayoutInflater.from(GastoActivity.this).inflate(R.layout.dialog_create_gasto, null);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        EditText editTextConcepto = dialogView.findViewById(R.id.editTextConcepto);
        EditText editTextImporte = dialogView.findViewById(R.id.editTextImporte);
        spinnerUsuarios = dialogView.findViewById(R.id.spinnerUsuarios);
        Button btnCrearGasto = dialogView.findViewById(R.id.btnCrearGasto);

        // Obtener usuarios del plan y configurar el adaptador del Spinner
        obtenerUsuariosDelPlan(planId);

        // Listener para el botón de crear gasto
        btnCrearGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String concepto = editTextConcepto.getText().toString().trim();
                float importe = Float.parseFloat(editTextImporte.getText().toString().trim());

                // Obtener los usuarios seleccionados del spinner
                List<Usuario> selectedUsuarios = adapter.getSelectedUsuarios();
                List<Integer> selectedUserIds = new ArrayList<>();
                for (Usuario usuario : selectedUsuarios) {
                    selectedUserIds.add(usuario.getId());
                }

                // Crear el objeto Gasto
                Gasto gasto = new Gasto(concepto, importe, idPagador);

                // Enviar la solicitud para crear el gasto y asociar usuarios
                crearGasto(planId, gasto, selectedUserIds);

                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void obtenerUsuariosDelPlan(int planId) {
        Call<UsuarioPorPlanRespuesta> llamada = planUsuarioService.obtenerUsuariosPorPlan(planId);

        llamada.enqueue(new Callback<UsuarioPorPlanRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioPorPlanRespuesta> call, Response<UsuarioPorPlanRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> usuarios = response.body().getUsuarios();

                    // Configurar el adaptador del Spinner con los usuarios obtenidos
                    adapter = new UserCheckboxSpinnerAdapter(GastoActivity.this, usuarios, new ArrayList<>());
                    spinnerUsuarios.setAdapter(adapter);
                } else {
                    Toast.makeText(GastoActivity.this, "No se pudieron obtener los usuarios del plan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioPorPlanRespuesta> call, Throwable t) {
                Toast.makeText(GastoActivity.this, "Error en la conexión al obtener usuarios del plan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearGasto(int planId, Gasto gasto, List<Integer> usuarios) {
        Call<Void> llamada = gastoService.crearGasto(planId, new GastoService.CrearGastoRequest(gasto, usuarios));

        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GastoActivity.this, "Gasto creado exitosamente", Toast.LENGTH_SHORT).show();
                    obtenerGastos(planId); // Actualizar la lista de gastos después de crear uno nuevo
                } else {
                    Toast.makeText(GastoActivity.this, "No se pudo crear el gasto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GastoActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
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