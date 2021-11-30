package com.example.eventos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eventos.modelo.Event;

import java.util.ArrayList;


public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Event> eventsList;

    public Adapter(Context context, ArrayList<Event> eventsList) {
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

        convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, null);

        TextView titulo =(TextView) convertView.findViewById(R.id.txtNevento);


        titulo.setText("Tomas las medidas necesarias una persona que asistio al evento de "+ item.getName()+" una persona se contagio");



        return convertView;
    }


}
