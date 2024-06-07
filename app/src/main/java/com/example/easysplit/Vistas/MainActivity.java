package com.example.easysplit.Vistas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easysplit.R;
import com.example.easysplit.Servicios.Usuario.UsuarioRespuesta;
import com.example.easysplit.Servicios.Usuario.UsuarioService;
import com.example.easysplit.Modelos.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private EditText userEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button loginButton;
    private TextView iniciarSesion;
    private SharedPreferences sharedPreferences;
    private UsuarioService usuarioService;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userEditText = findViewById(R.id.name_login);
        emailEditText = findViewById(R.id.email_login);
        phoneEditText = findViewById(R.id.phone_login);
        loginButton = findViewById(R.id.login_button);
        iniciarSesion = findViewById(R.id.iniciarSesion);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        url = getResources().getString(R.string.baseUrl);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarUsuario();
            }
        });
    }

    void enviarUsuario() {
        String nombre = userEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String telefono = phoneEditText.getText().toString();

        Usuario usuario = new Usuario(nombre, email, telefono);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioService = retrofit.create(UsuarioService.class);
        Call<UsuarioRespuesta> llamada = usuarioService.crearUsuario(usuario);

        llamada.enqueue(new Callback<UsuarioRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                Toast.makeText(MainActivity.this, "Se ha creado el usuario correctamente", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putInt("UserID", response.body().getData().getId());
                spEditor.apply();
                Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                startActivity(intent);
                spEditor.commit();
            }

            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha creado el usuario correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoginDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.iniciarsesion_form, null);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(dialogView);

        EditText username = dialog.findViewById(R.id.username_login);
        ImageView sendButton = dialog.findViewById(R.id.form_send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = username.getText().toString();
                checkUsername(nombre);
            }
        });
        dialog.show();
    }

    private void checkUsername(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        usuarioService = retrofit.create(UsuarioService.class);
        Call<UsuarioRespuesta> llamada = usuarioService.obtenerUsuarioPorUsername(username);
        llamada.enqueue(new Callback<UsuarioRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Usuario usuario = response.body().getData();
                    int userIdFromDatabase = usuario.getId();

                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                    spEditor.putInt("UserID", userIdFromDatabase);
                    spEditor.apply(); // Use apply() for asynchronous saving

                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Nombre de usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}