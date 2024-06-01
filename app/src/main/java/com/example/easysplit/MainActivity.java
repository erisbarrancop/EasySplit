package com.example.easysplit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.easysplit.Modelos.Usuario;


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
    }

    private void enviarUsuario(){

        String nombre = userEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String telefono = phoneEditText.getText().toString();

        Usuario usuario = new Usuario(nombre, email, telefono);

    }

}