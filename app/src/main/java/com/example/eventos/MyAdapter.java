package com.example.eventos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eventos.modelo.Event;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Event> eventsList;

    public MyAdapter(Context context, ArrayList<Event> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event item = (Event) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

        TextView titulo =(TextView) convertView.findViewById(R.id.txtTitulo);
        TextView direccion = (TextView) convertView.findViewById(R.id.txtDir);
        TextView fecha = (TextView) convertView.findViewById(R.id.txtF) ;
        TextView hora = (TextView) convertView.findViewById(R.id.txtHo);

        titulo.setText(item.getName());
        direccion.setText(item.getDireccion());
        fecha.setText(item.getFecha());
        hora.setText(item.getHora());


        return convertView;
    }


}
