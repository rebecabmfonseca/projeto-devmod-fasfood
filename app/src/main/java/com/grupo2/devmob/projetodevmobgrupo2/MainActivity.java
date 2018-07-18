package com.grupo2.devmob.projetodevmobgrupo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;
    private Button btnLogar;
    private String usuario, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = edtUsuario.getText().toString();
                senha = edtSenha.getText().toString();

                //aqui verificamos o login e senha ..

                Intent intent = new Intent(MainActivity.this,MesasActivity.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("senha",senha);
                startActivity(intent);

            }
        });
    }
}
