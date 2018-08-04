package com.grupo2.devmob.projetodevmobgrupo2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;

    private String usuario, senha;
    private FirebaseAuth mAuth;
    private ImageView imgIr;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioAtual = mAuth.getCurrentUser();

        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);

        imgIr = findViewById(R.id.imgIr);

        //checa se usuário já está logado
        if (usuarioAtual != null) {
            Intent intent = new Intent(MainActivity.this, MesasActivity.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("senha",senha);
            startActivity(intent);
        }

        imgIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = edtUsuario.getText().toString().trim();
                senha = edtSenha.getText().toString();
                realizaLogin(usuario, senha);
            }
        });


    }

    @Override
    //impede que o app volte para Activity anterior quando o botão voltar é pressionado
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public void realizaLogin(final String usuario, final String senha) {
        if (!usuario.isEmpty() && !senha.isEmpty()) {
            mAuth.signInWithEmailAndPassword(usuario, senha)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(MainActivity.this, MesasActivity.class);
                                intent.putExtra("usuario",usuario);
                                intent.putExtra("senha",senha);
                                startActivity(intent);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Falha no login...",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Informe usuário e senha...",
                    Toast.LENGTH_SHORT).show();
        }

    }
}