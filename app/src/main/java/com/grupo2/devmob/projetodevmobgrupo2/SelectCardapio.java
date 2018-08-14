package com.grupo2.devmob.projetodevmobgrupo2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;
import java.util.List;

public class SelectCardapio extends AppCompatActivity {

    ListView list ;
    int ItemTIPO=0;
    Bundle mybundle;
    int Mymesa;
    FirebaseFirestore mDatabase;

    ArrayList<Pedido>MesaPedidos;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            findViewById(R.id.btfecha).setVisibility(View.INVISIBLE);
            findViewById(R.id.txpreco).setVisibility(View.INVISIBLE);
            switch (item.getItemId()) {
                case R.id.comanda:
                    showComanda();
                    return true;
                case R.id.Salgados:
                    ItemTIPO=0;
                    showAlimentos();
                    return true;
                case R.id.doces:
                    ItemTIPO=1;
                    showAlimentos();
                    return true;
                case R.id.bebidas:
                    ItemTIPO=2;
                    showAlimentos();
                    return true;
            }
            return false;
        }
    };

    public ArrayList<Integer> attributesvalue= new ArrayList<>();
    float InitialValue, finalvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mymesa= getIntent().getIntExtra("mesa",0);
        mybundle= savedInstanceState;
        MesaPedidos= new ArrayList<>();
        mDatabase= FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_select_cardapio);
        findViewById(R.id.btfecha).setVisibility(View.INVISIBLE);
        findViewById(R.id.txpreco).setVisibility(View.INVISIBLE);
        list= findViewById(R.id.mylist);
        mDatabase.collection("dados").document("mesas").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot= task.getResult();

                        String MesaValue=(String)snapshot.getData().get("mesa"+Mymesa);
                        if(!(MesaValue.equals("[]")||MesaValue.equals(null)||MesaValue.equals("")))
                        {
                            Gson gson= new Gson();
                            MesaPedidos = gson.fromJson(MesaValue,new TypeToken<List<Pedido>>(){}.getType());
                            Log.d("OK", "onComplete: DEU CERTO");
                        }

                }else
                {
                    Toast.makeText(getApplicationContext(),"Erro ao obter dadods",Toast.LENGTH_LONG).show();
                }
            }
        });


        showAlimentos();

    }
    @Override
    public void onBackPressed() {

        Gson gson= new Gson();
        String json= gson.toJson(MesaPedidos);
        mDatabase.collection("dados").document("mesas").update("mesa"+String.valueOf(Mymesa),json).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Tag", "Sucesso: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", "Falha ao salvar  ");

            }
        });
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
    void showComanda()
    {
        if(MesaPedidos.size()>0)
        {
            float Fprice=0;
            for(Pedido p :MesaPedidos)Fprice+= p.price;
            list.setAdapter(new CreateComanda(this,MesaPedidos));
            findViewById(R.id.btfecha).setVisibility(View.VISIBLE);
            findViewById(R.id.txpreco).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txpreco)).setText(String.format("O Preço Final da compra é R$%.2f",Fprice));
            findViewById(R.id.btfecha).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    MesaPedidos.clear();
                                    onBackPressed();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Você tem certeza que deseja fechar a conta ?").setPositiveButton("Sim", dialogClickListener)
                            .setNegativeButton("Não", dialogClickListener).show();
                }
            });
        }
        else {
            ((TextView)findViewById(R.id.txpreco)).setText("Não Foi realizado nenhum pedido");

        }
    }
    void showAlimentos()
    {
        list.setVisibility(View.VISIBLE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list= findViewById(R.id.mylist);
        ArrayList<Lanche>lanches= new ArrayList<>();
        if(ItemTIPO==0) {
            lanches.add(new Lanche("X-Tudo", "Hamburguer completo", 5.50f, R.drawable.hamburguer));
            lanches.add(new Lanche("Cachorro-quente", "Cachorro quente completo", 6.00f, R.drawable.hotdog));
            lanches.add(new Lanche("Misto Quente", "Misto quente de queijo com presunto", 4.00f, R.drawable.mistoquente));
            lanches.add(new Lanche("Pizza", "Pedaço de pizza de calabresa", 4.50f, R.drawable.pizza));
        }
        else if (ItemTIPO==1)
        {
            lanches.add(new Lanche("Pudim", "Pudim de leite condensado", 3.50f, R.drawable.pudim));
            lanches.add(new Lanche("Sorvete", "Casquinha de baunilha", 2.50f, R.drawable.sorvete));
            lanches.add(new Lanche("Bolinho", "Bolinho de bunilha com gotas de chocolate", 3.00f, R.drawable.bolinho));
            lanches.add(new Lanche("Torta", "Torta de morango com chocolate", 5.50f, R.drawable.torta));
        }
        else {
            lanches.add(new Lanche("Água", "Garrafa com 500ml", 2.00f, R.drawable.agua));
            lanches.add(new Lanche("Suco", "Suco de laranja", 4.50f, R.drawable.suco));
            lanches.add(new Lanche("Refrigerante", "Pepsi", 5.50f, R.drawable.refri));
            lanches.add(new Lanche("Café", "Xícara de café", 3.00f, R.drawable.cafe));
        }
        list.setAdapter(new CreateList(this,lanches));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater layoutInflater = (LayoutInflater) SelectCardapio.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = layoutInflater.inflate(R.layout.pedidoselect, null);
                RelativeLayout propiets =layout.findViewById(R.id.Atributes);
                ((TextView)layout.findViewById(R.id.bname)).setText(((TextView)view.findViewById(R.id.bname)).getText().toString());
                ((TextView)layout.findViewById(R.id.bdesc)).setText(((TextView)view.findViewById(R.id.bdesc)).getText().toString());
                ((TextView)layout.findViewById(R.id.bprice)).setText(((TextView)view.findViewById(R.id.bprice)).getText().toString());
                ((ImageView)layout.findViewById(R.id.bimage)).setImageResource(Integer.valueOf( view.findViewById(R.id.bimage).getContentDescription().toString()));

                final ArrayList<Atributes> complements = new ArrayList<>();
                final String FoodName=(String)((TextView)layout.findViewById(R.id.bname)).getText();
                final int imgID= Integer.valueOf(view.findViewById(R.id.bimage).getContentDescription()+"");
                if(ItemTIPO==0) {
                    complements.add(new Atributes(" Carnes(s)", 3.00f, 1));
                    complements.add(new Atributes(" Queijos(s)", 1.00f, 1));
                    complements.add(new Atributes(" Bacon", 1.50f, 1));
                    complements.add(new Atributes(" Alface(s)", 0.50f, 1));
                    complements.add(new Atributes(" Tomate(s)", 0.40f, 1));
                    complements.add(new Atributes(" Maionese(s)", 0.50f, 1));
                }
                else if (ItemTIPO==1)
                {
                    complements.add(new Atributes(" Granulado", 0.10f, 1));
                    complements.add(new Atributes(" Amendoim", 0.50f, 1));
                    complements.add(new Atributes(" Calda Chocolate", 0.10f, 1));
                    complements.add(new Atributes(" Calda Morango", 0.10f, 1));

                }
                else {
                    complements.add(new Atributes(" Gelo", 0.00f, 1));
                    complements.add(new Atributes(" Limão", 0.00f, 0));
                }
                InitialValue= Float.valueOf( view.findViewById(R.id.bprice).getContentDescription().toString());
                finalvalue= InitialValue;
                ((TextView)layout.findViewById(R.id.finalvalue)).setText(String.format("O Valor total é de R$ %.2f",finalvalue));

                final LinearLayout atrilay= (LinearLayout)layout.findViewById(R.id.atrilayout);
                Button btn = layout.findViewById(R.id.confirm);

                for (int z=0;z<complements.size();z++)attributesvalue.add(complements.get(z).inicialqnt);
                for (int z=0;z<complements.size();z++) {
                    final Atributes at = complements.get(z);
                    View t = layoutInflater.inflate(R.layout.salgados,atrilay,false);
                    t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)( 50*SelectCardapio.this.getResources().getDisplayMetrics().density + 0.5f)));
                    ((TextView)t.findViewById(R.id.tipo)).setText(at.Desc);
                    ((TextView)t.findViewById(R.id.preco)).setText(String.valueOf(String.format("R$ %.2f ", at.price)));
                    t.findViewById(R.id.preco).setContentDescription(String.valueOf(String.valueOf( at.price)));
                    ((ScrollableNumberPicker)t.findViewById(R.id.quant)).setValue(at.inicialqnt);
                    final int finalZ = z;
                    ((ScrollableNumberPicker)t.findViewById(R.id.quant)).setListener(new ScrollableNumberPickerListener() {
                        @Override
                        public void onNumberPicked(int value) {
                            attributesvalue.set(finalZ,value);
                            finalvalue= InitialValue;
                            for (int y=0;y<attributesvalue.size();y++)
                            {
                                if (attributesvalue.get(y)>1)
                                    finalvalue+=(attributesvalue.get(y)-1)*at.price;
                            }
                            ((TextView)layout.findViewById(R.id.finalvalue)).setText(String.format("O Valor total é de R$ %.2f",finalvalue));
                        }
                    });

                    atrilay.addView(t);
                }

                final PopupWindow changeSortPopUp = new PopupWindow(SelectCardapio.this);
                changeSortPopUp.setContentView(layout);
                changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setFocusable(true);


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String FinalComplements="";
                        for(int i=0;i<complements.size();i++)
                        {
                            if(attributesvalue.get(i)>0)
                            {
                                FinalComplements+= complements.get(i).Desc+" x"+attributesvalue.get(i)+";";
                            }
                        }
                        Pedido p = new Pedido(FoodName,FinalComplements,finalvalue,imgID);
                        Log.d("FINALPRICE", "O preço é "+p.price+" o nome é "+p.Name+" A descri "+p.complements);
                        changeSortPopUp.dismiss();
                        MesaPedidos.add(p);




                    }
                });


                // Clear the default translucent background
                changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

                // Displaying the popup at the specified location, + offsets.
                changeSortPopUp.showAtLocation(layout, Gravity.TOP, 0, 0);
            }
        });
    }


}

class CreateList extends ArrayAdapter<Lanche> {

    private Context mContext;
    private List<Lanche> lancheList ;
    public CreateList(@NonNull Context context,ArrayList<Lanche> list ) {
        super(context,0, list);
        mContext= context;
        lancheList= list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Lanche l = lancheList.get(position);
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.lanche_list,parent,false);
        ((TextView)listItem.findViewById(R.id.bname)).setText(l.name);
        ((TextView)listItem.findViewById(R.id.bdesc)).setText(l.desc);
        ((TextView)listItem.findViewById(R.id.bprice)).setText(String.format("R$ %.2f ", l.price));
        ((TextView)listItem.findViewById(R.id.bprice)).setContentDescription(String.valueOf( l.price));

        ((ImageView)listItem.findViewById(R.id.bimage)).setImageResource(l.imageID);
        ((ImageView)listItem.findViewById(R.id.bimage)).setContentDescription(String.valueOf(l.imageID));

        return listItem;
    }
}
class CreateComanda extends ArrayAdapter<Pedido>{

    private Context mContext;
    private List<Pedido> ComidaList ;
    public CreateComanda(@NonNull Context context, ArrayList<Pedido> resource) {
        super(context,0, resource);
        mContext= context;
        ComidaList= resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if(v == null)
            v = LayoutInflater.from(mContext).inflate(R.layout.comanda_pedido,parent,false);
        Pedido p= ComidaList.get(position);
        ((TextView)v.findViewById(R.id.bname)).setText(p.Name);
        ((TextView)v.findViewById(R.id.bprice)).setText(String.format("R$ %.2f ", p.price));
        ((TextView)v.findViewById(R.id.bdesc)).setText(p.complements.replaceAll(";","\n"));
        ((ImageView)v.findViewById(R.id.bimage)).setImageResource(p.ProductID);
        Log.d("HYE", "showComanda: mostra ai ");

        return v;
    }
}
class Lanche{
    String name,desc;
    float price;
    int imageID;
    public Lanche(String n, String d,float p,int imgId)
    {
        name=n;
        desc=d;
        imageID= imgId;
        price=p;
    }
}
class Atributes{
    String Desc;
    float price;
    int inicialqnt;
    public Atributes(String d,float p,int ini)
    {
        Desc=d;
        price=p;
        inicialqnt=ini;

    }
}
class Pedido{
    String Name,complements;
    float price;
    int ProductID;
    public Pedido(String n,String c,float p ,int proid)
    {
        Name= n;
        complements=c;
        price=p;
        ProductID= proid;
    }

}