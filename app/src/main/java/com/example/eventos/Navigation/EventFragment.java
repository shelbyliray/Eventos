package com.example.eventos.Navigation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventos.AddEventActivity;
import com.example.eventos.BottomNavigationActivity;
import com.example.eventos.MainActivity;
import com.example.eventos.MyAdapter;
import com.example.eventos.R;
import com.example.eventos.modelo.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private FloatingActionButton fabAgregar;
    private ArrayList<Event> eventList = new ArrayList<>();
    MyAdapter adapterEvent;
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

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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
        // Inflate the layout for this
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        fabAgregar = v.findViewById(R.id.fab);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        lista = (ListView) v.findViewById(R.id.datos);
        eventList.clear();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                event = (Event) parent.getItemAtPosition(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getActivity())
                        .setPositiveButton("Si", (dialog1, which) -> {
                            Toast.makeText(getActivity(), "Se aviso a las demas personas que asitieron", Toast.LENGTH_SHORT).show();
                            createCustomDialog(event);
                        })
                        .setNegativeButton("No", (dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .setTitle("Confirmar")
                        .setMessage("¿Estas contagiado de COVID-19?")
                        .create();
                dialog.show();
            }
        });

        i = getArguments().getString("Userid");
        n = getArguments().getString("Username");
        e = getArguments().getString("Useremail");
        p = getArguments().getString("Userpass");
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                intent.putExtra("Userid", i);
                intent.putExtra("Username", n);
                intent.putExtra("Useremail", e);
                intent.putExtra("Userpass", p);
                startActivity(intent);
            }
        });

        mostrarDatos();

        return v;
    }

    private void mostrarDatos() {
        mDatabase.child("Users").child(i).child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objectSnapshot : snapshot.getChildren()){
                    String i = objectSnapshot.getKey();
                    String n = objectSnapshot.getValue(Event.class).getName();
                    String d = objectSnapshot.getValue(Event.class).getDireccion();
                    String f = objectSnapshot.getValue(Event.class).getFecha();
                    String h = objectSnapshot.getValue(Event.class).getHora();
                    boolean s = objectSnapshot.getValue(Event.class).getStatus();
                    Event e = new Event(i,n,d,f,h,s);
                    eventList.add(e);
                }

                adapterEvent = new MyAdapter(getActivity(), eventList);
                lista.setAdapter(adapterEvent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createCustomDialog(Event event1) {
        event1.setStatus(true);
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objectSnapshot : snapshot.getChildren()) {

                 mDatabase.child("Users").child(objectSnapshot.getKey()).child("Events").child(event1.getId()).setValue(event1);
                 mDatabase.child("Events").child(event1.getId()).setValue(event1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}