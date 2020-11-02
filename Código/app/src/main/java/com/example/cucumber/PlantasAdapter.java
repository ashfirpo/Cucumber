package com.example.cucumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cucumber.Firebase.Planta;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantasAdapter extends BaseAdapter {

    private Context context;
    private List<Planta> plantas;

    public PlantasAdapter(Context context, List<Planta> plantas)
    {
        this.context = context;
        this.plantas = plantas;
    }

    @Override
    public int getCount() {
        return plantas.size();
    }

    @Override
    public Object getItem(int position) {
        return plantas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.data_grilla, parent, false);
        }

        ImageView imagen = convertView.findViewById(R.id.imagen);
        TextView nombrePlanta = convertView.findViewById(R.id.nombreplantas);

        //Agregamos la imagen y el nombre de la planta
        final Planta item = (Planta)getItem(position);
        Picasso.get().load(item.getImagen()).into(imagen);
        nombrePlanta.setText(item.getNombre());

        return convertView;
    }
}
