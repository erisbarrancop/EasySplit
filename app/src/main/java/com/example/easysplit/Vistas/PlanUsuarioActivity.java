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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easysplit.Adapters.PlanAdapter;
import com.example.easysplit.Adapters.UserCheckboxSpinnerAdapter;
import com.example.easysplit.Modelos.Plan;
import com.example.easysplit.Modelos.Usuario;
import com.example.easysplit.R;
import com.example.easysplit.Servicios.Plan.PlanRespuesta;
import com.example.easysplit.Servicios.Plan.PlanService;
import com.example.easysplit.Servicios.Plan.PlanUsuarioRespuesta;
import com.example.easysplit.Servicios.Plan.PlanUsuarioService;
import com.example.easysplit.Servicios.Usuario.UsuarioRespuestaLista;
import com.example.easysplit.Servicios.Usuario.UsuarioService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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
    private PlanService planService;
    private UsuarioService usuarioService;
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
        planService = retrofit.create(PlanService.class);
        usuarioService = retrofit.create(UsuarioService.class);
        // Obtener el UserID del SharedPreferences
        int userId = sharedPreferences.getInt("UserID", 0);

        // Realizar la petición para obtener los planes del usuario
        obtenerPlanes(userId);

        FloatingActionButton fabAddPlan = findViewById(R.id.fab);

        fabAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePlanDialog();
            }
        });

        listViewPlanes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Plan selectedPlan = (Plan) parent.getItemAtPosition(position);
                int planId = selectedPlan.getId();
                String planName = selectedPlan.getNombre();
                Intent intent = new Intent(PlanUsuarioActivity.this, GastoActivity.class);
                intent.putExtra("selectedPlan", planId);
                intent.putExtra("planName", planName);

                startActivity(intent);
            }
        });
    }

    private void obtenerPlanes(int userId) {
        Call<PlanUsuarioRespuesta> llamada = planUsuarioService.obtenerPlanesPorUsuario(userId);

        llamada.enqueue(new Callback<PlanUsuarioRespuesta>() {
            @Override
            public void onResponse(Call<PlanUsuarioRespuesta> call, Response<PlanUsuarioRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> planes = response.body().getData();
                    planAdapter = new PlanAdapter(PlanUsuarioActivity.this, planes);
                    listViewPlanes.setAdapter(planAdapter);
                } else {
                    Toast.makeText(PlanUsuarioActivity.this, "No se encontraron planes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanUsuarioRespuesta> call, Throwable t) {
                Toast.makeText(PlanUsuarioActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreatePlanDialog() {
        Dialog dialog = new Dialog(PlanUsuarioActivity.this);
        View dialogView = LayoutInflater.from(PlanUsuarioActivity.this).inflate(R.layout.crearplan_form, null);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(dialogView);
        EditText planNameEditText = dialogView.findViewById(R.id.plan_name);
        EditText planCurrencyEditText = dialogView.findViewById(R.id.plan_currency);
        Spinner userSpinner = dialogView.findViewById(R.id.user_spinner);
        Button createPlanButton = dialogView.findViewById(R.id.create_plan_button);

        // Lista para usuarios seleccionados
        List<Usuario> selectedUsuarios = new ArrayList<>();

        // Obtener la lista de usuarios de la base de datos
        usuarioService.obtenerListaUsuarios().enqueue(new Callback<UsuarioRespuestaLista>() {
            @Override
            public void onResponse(Call<UsuarioRespuestaLista> call, Response<UsuarioRespuestaLista> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> usuarios = response.body().getUsuarios();

                    // Filtrar usuarios para excluir al administrador actual
                    List<Usuario> filteredUsuarios = new ArrayList<>();
                    for (Usuario usuario : usuarios) {
                        int id_admin = sharedPreferences.getInt("UserID", 0);
                        if (usuario.getId() != id_admin) {
                            filteredUsuarios.add(usuario);
                        }
                    }

                    UserCheckboxSpinnerAdapter adapter = new UserCheckboxSpinnerAdapter(PlanUsuarioActivity.this, filteredUsuarios, selectedUsuarios);
                    userSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(PlanUsuarioActivity.this, "No se pudieron obtener los usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioRespuestaLista> call, Throwable t) {
                Toast.makeText(PlanUsuarioActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        createPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = planNameEditText.getText().toString();
                String moneda = planCurrencyEditText.getText().toString();
                int id_admin = sharedPreferences.getInt("UserID", 0);

                Plan plan = new Plan(nombre, moneda, id_admin);

                Call<PlanRespuesta> llamada = planService.crearPlan(plan);

                llamada.enqueue(new Callback<PlanRespuesta>() {
                    @Override
                    public void onResponse(Call<PlanRespuesta> call, Response<PlanRespuesta> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            int planId = response.body().getData().getId();
                            unirUsuariosAlPlan(planId, selectedUsuarios);
                            Toast.makeText(PlanUsuarioActivity.this, "Plan creado exitosamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            obtenerPlanes(id_admin);
                        } else {
                            Toast.makeText(PlanUsuarioActivity.this, "No se pudo crear el plan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlanRespuesta> call, Throwable t) {
                        Toast.makeText(PlanUsuarioActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }


    private void unirUsuariosAlPlan(int planId, List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            Call<Void> llamada = planUsuarioService.crearRelacionUsuarioPlan(planId, usuario.getId());
            llamada.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(PlanUsuarioActivity.this, "No se pudo unir el usuario " + usuario.getNombre() + " al plan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PlanUsuarioActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}