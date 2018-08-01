package com.grupo2.devmob.projetodevmobgrupo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MesasActivity extends AppCompatActivity {

    private Button  btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        btnSair = findViewById(R.id.btnSair);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MesasActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.mesa1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MesasActivity.this,SelectCardapio.class));
            }
        });
    }

    @Override
    //impede que o app volte para MainActivity quando o botão voltar é pressionado
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
