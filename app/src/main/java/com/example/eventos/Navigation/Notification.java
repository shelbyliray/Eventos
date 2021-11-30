package com.example.eventos.Navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eventos.Adapter;
import com.example.eventos.AddEventActivity;
import com.example.eventos.MyAdapter;
import com.example.eventos.R;
import com.example.eventos.modelo.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {

    private ArrayList<Event> eventList = new ArrayList<>();
    Adapter adapterEvent;
    ListView lista;
    Event event;

    FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;
    String n,e,i, p;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification newInstance(String param1, String param2) {
        Notification fragment = new Notification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        lista = (ListView) v.findViewById(R.id.notiEvent);
        eventList.clear();



        i = getArguments().getString("Userid");
        n = getArguments().getString("Username");
        e = getArguments().getString("Useremail");
        p = getArguments().getString("Userpass");

        mostrarDatos();

        return v;
    }

    private void mostrarDatos() {
        mDatabase.child("Users").child(i).child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objectSnapshot : snapshot.getChildren()){
                    Event e = objectSnapshot.getValue(Event.class);
                    boolean status = objectSnapshot.getValue(Event.class).getStatus();
                    if(status){
                        eventList.add(e);
                    }
                }

                adapterEvent = new Adapter(getActivity(), eventList);
                lista.setAdapter(adapterEvent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}