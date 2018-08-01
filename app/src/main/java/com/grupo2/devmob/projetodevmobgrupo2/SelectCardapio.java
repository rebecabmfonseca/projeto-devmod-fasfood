package com.grupo2.devmob.projetodevmobgrupo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectCardapio extends AppCompatActivity {

    ListView list ;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.comanda:
                    return true;
                case R.id.Salgados:
                    return true;
                case R.id.doces:
                    return true;
                case R.id.bebidas:
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cardapio);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list= findViewById(R.id.mylist);
        ArrayList<Lanche>lanches= new ArrayList<>();
        lanches.add(new Lanche("lanche","lanhd",0,R.drawable.ic_home_black_24dp));
        lanches.add(new Lanche("lanche","lanhd",0,R.drawable.ic_home_black_24dp));
        lanches.add(new Lanche("lanche","lanhd",0,R.drawable.ic_home_black_24dp));
        lanches.add(new Lanche("lanche","lanhd",0,R.drawable.ic_home_black_24dp));
        list.setAdapter(new CreateList(this,lanches));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater layoutInflater = (LayoutInflater) SelectCardapio.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = layoutInflater.inflate(R.layout.pedidoselect, null);
                RelativeLayout propiets =layout.findViewById(R.id.Atributes);
                ((TextView)layout.findViewById(R.id.bname)).setText(((TextView)view.findViewById(R.id.bname)).getText().toString());
                ((TextView)layout.findViewById(R.id.bdesc)).setText(((TextView)view.findViewById(R.id.bdesc)).getText().toString());
                ((TextView)layout.findViewById(R.id.bprice)).setText(((TextView)view.findViewById(R.id.bprice)).getText().toString());
                View lay2= layoutInflater.inflate(R.layout.salgados,null);
                ((LinearLayout)layout.findViewById(R.id.linearLayout)).addView(lay2);
               // Bitmap bitmap = ((BitmapDrawable)((ImageView)view.findViewById(R.id.bimage)).getDrawable()).getBitmap();

               // ((ImageView)findViewById(R.id.bimage)).setImageBitmap(bitmap);

                PopupWindow changeSortPopUp = new PopupWindow(SelectCardapio.this);
                changeSortPopUp.setContentView(layout);
                changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setFocusable(true);



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
        ((ImageView)listItem.findViewById(R.id.bimage)).setImageResource(l.imageID);
        return listItem;
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