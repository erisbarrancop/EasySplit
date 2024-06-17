package com.example.easysplit.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.easysplit.Servicios.Plan.PlanUsuarioService;
import com.example.easysplit.Servicios.Usuario.UsuarioPorPlanRespuesta;
import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;
import com.example.easysplit.Servicios.Usuario.UsuarioService;
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
    private UsuarioService usuarioService;
    private Spinner spinnerUsuarios;
    private UserCheckboxSpinnerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        planId = getIntent().getIntExtra("selectedPlan", 0);
        planName = getIntent().getStringExtra("planName");
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        TextView currentPlanName = findViewById(R.id.textViewCurrentPlan);
        currentPlanName.setText(planName);

        listViewGastos = findViewById(R.id.listViewCurrentPlan);
        url = getResources().getString(R.string.baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gastoService = retrofit.create(GastoService.class);
        planUsuarioService = retrofit.create(PlanUsuarioService.class);
        usuarioService = retrofit.create(UsuarioService.class);

        idPagador = sharedPreferences.getInt("UserID", 0);

        obtenerGastos(planId);

        FloatingActionButton fabAddGasto = findViewById(R.id.fab_gasto);
        fabAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerUsuariosDelPlanYMostrarDialogo(planId);
            }
        });

        listViewGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gasto gastoSeleccionado = (Gasto) parent.getItemAtPosition(position);

                Intent intent = new Intent(GastoActivity.this, DetalleGastoActivity.class);

                intent.putExtra("concepto", gastoSeleccionado.getConcepto());
                intent.putExtra("importe", gastoSeleccionado.getImporte());
                intent.putExtra("idPagador", gastoSeleccionado.getId_pagador());

                startActivity(intent);
            }
        });

    }

    private void obtenerUsuariosDelPlanYMostrarDialogo(int planId) {
        Call<UsuarioPorPlanRespuesta> llamada = planUsuarioService.obtenerUsuariosPorPlan(planId);

        llamada.enqueue(new Callback<UsuarioPorPlanRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioPorPlanRespuesta> call, Response<UsuarioPorPlanRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlanUsuario> usuariosPlan = response.body().getData();
                    obtenerDetallesUsuarios(usuariosPlan);
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

    private void obtenerDetallesUsuarios(List<PlanUsuario> usuariosPlan) {
        List<Usuario> usuarios = new ArrayList<>();

        for (PlanUsuario usuario : usuariosPlan) {
            Call<UsuarioRespuesta> llamadaUsuario = usuarioService.obtenerUsuarioPorID(usuario.getId_usuario());
            llamadaUsuario.enqueue(new Callback<UsuarioRespuesta>() {
                @Override
                public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        usuarios.add(response.body().getData());

                        if (usuarios.size() == usuariosPlan.size()) {
                            adapter = new UserCheckboxSpinnerAdapter(GastoActivity.this, usuarios, new ArrayList<>());
                            mostrarDialogoCrearGasto();
                        }
                    } else {
                        Toast.makeText(GastoActivity.this, "No se pudieron obtener los detalles del usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                    Toast.makeText(GastoActivity.this, "Error en la conexión al obtener detalles del usuario", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mostrarDialogoCrearGasto() {
        Dialog dialog = new Dialog(GastoActivity.this);
        View dialogView = LayoutInflater.from(GastoActivity.this).inflate(R.layout.dialog_create_gasto, null);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        EditText editTextConcepto = dialogView.findViewById(R.id.editTextConcepto);
        EditText editTextImporte = dialogView.findViewById(R.id.editTextImporte);
        spinnerUsuarios = dialogView.findViewById(R.id.spinnerUsuarios);
        spinnerUsuarios.setAdapter(adapter);
        Button btnCrearGasto = dialogView.findViewById(R.id.btnCrearGasto);

        btnCrearGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String concepto = editTextConcepto.getText().toString().trim();
                float importe = Float.parseFloat(editTextImporte.getText().toString().trim());

                List<Usuario> selectedUsuarios = adapter.getSelectedUsuarios();
                List<Integer> selectedUserIds = new ArrayList<>();
                for (Usuario usuario : selectedUsuarios) {
                    selectedUserIds.add(usuario.getId());
                }

                Gasto gasto = new Gasto(concepto, importe, idPagador, selectedUserIds);

                crearGasto(planId, gasto);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void crearGasto(int planId, Gasto gasto) {
        Call<Void> llamada = gastoService.crearGasto(planId, gasto);

        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GastoActivity.this, "Gasto creado exitosamente", Toast.LENGTH_SHORT).show();
                    obtenerGastos(planId);
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