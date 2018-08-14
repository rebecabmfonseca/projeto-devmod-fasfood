package com.grupo2.devmob.projetodevmobgrupo2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MesasActivity extends AppCompatActivity {


    private ImageView imgLogout;
    private FirebaseFirestore mDatabase;
    final int[] MesasImages= new int[]{R.id.mesa1,R.id.mesa2,R.id.mesa3,R.id.mesa4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        Gson gson= new Gson();

        for (int i=0;i<MesasImages.length;i++)
        {
            final int finalI = i+1;
            findViewById(MesasImages[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MesasActivity.this,SelectCardapio.class).putExtra("mesa", finalI));

                }
            });
        }


        imgLogout = findViewById(R.id.imgLogout);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MesasActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        conectaMesas();

    }

    @Override
    protected void onResume() {
        super.onResume();
        conectaMesas();
    }

    @Override
    //impede que o app volte para MainActivity quando o botão voltar é pressionado
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public void conectaMesas(){
        FirebaseFirestore mDatabase;
        mDatabase= FirebaseFirestore.getInstance();
        mDatabase.collection("dados").document("mesas").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot= task.getResult();
                    String[] mesas = new String[]{"mesa1","mesa2","mesa3","mesa4"};
                    for(int i=1;i<=mesas.length;i++){
                        String MesaValue=(String)snapshot.getData().get("mesa"+i);
                        //Log.d("kkkk", "onComplete: "+MesaValue);
                        if(MesaValue.equals("[]")||MesaValue.equals(null)||MesaValue.equals(""))
                        {
                            ((ImageView)findViewById(MesasImages[i-1])).setImageResource(R.drawable.mesa);
                        }
                        else {
                            ((ImageView)findViewById(MesasImages[i-1])).setImageResource(R.drawable.mesaocupada);
                        }

                    }

                }else
                {
                    Toast.makeText(getApplicationContext(),"Erro ao obter dadods",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

