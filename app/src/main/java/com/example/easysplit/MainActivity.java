package com.example.easysplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easysplit.Interfaces.UsuarioRespuesta;
import com.example.easysplit.Interfaces.UsuarioService;
import com.example.easysplit.Modelos.Usuario;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEditText = findViewById(R.id.name_login);
        emailEditText = findViewById(R.id.email_login);
        phoneEditText = findViewById(R.id.phone_login);
        loginButton = findViewById(R.id.login_button);

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

        String url = getResources().getString(R.string.baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioService usuarioCrear = retrofit.create(UsuarioService.class);
        Call<UsuarioRespuesta> llamada = usuarioCrear.crearUsuario(usuario);

        llamada.enqueue(new Callback<UsuarioRespuesta>() {
            @Override
            public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                Toast.makeText(MainActivity.this, "Se ha creado el usuario correctamente", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putInt("UserID", response.body().getData().getId());
                spEditor.commit();

            }


            @Override
            public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha creado el usuario correctamente", Toast.LENGTH_SHORT).show();

            }
        });

    }

}