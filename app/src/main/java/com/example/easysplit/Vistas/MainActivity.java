package com.example.easysplit.Vistas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        url = getResources().getString(R.string.baseUrl);

        if (sharedPreferences.contains("UserID")) {
            promptUsernameDialog();
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarUsuario();
            }
        });
    }

     void enviarUsuario(){

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
                spEditor.commit();
                Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                startActivity(intent);
            }


            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha creado el usuario correctamente", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void promptUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Iniciar sesión");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = input.getText().toString();
                checkUsername(username);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

                    int userIdFromSharedPreferences = sharedPreferences.getInt("UserID",0);

                    if (userIdFromDatabase == userIdFromSharedPreferences) {
                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Nombre de usuario no coincide", Toast.LENGTH_SHORT).show();
                    }
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