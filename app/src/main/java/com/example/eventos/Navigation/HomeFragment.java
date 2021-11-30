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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventos.AddEventActivity;
import com.example.eventos.BottomNavigationActivity;
import com.example.eventos.MyAdapter;
import com.example.eventos.R;
import com.example.eventos.modelo.Event;
import com.example.eventos.modelo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private ArrayList<Event> eventList = new ArrayList<>();
    MyAdapter adapterEvent;
    ListView lista;
    Event event;
    String idUser;

    FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v= inflater.inflate(R.layout.fragment_home, container, false);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        idUser = getArguments().getString("Userid");
        lista = (ListView) v.findViewById(R.id.datosEventos);
        eventList.clear();
        mostrarDatos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                event = (Event) parent.getItemAtPosition(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getActivity())
                        .setPositiveButton("Asistir al evento", (dialog1, which) -> {
                            createCustomDialog(event).show();
                        })
                        .setNegativeButton("Cancelar", (dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .setTitle("Confirmar")
                        .setMessage("¿Desea asisitir al evento?")
                        .create();
                dialog.show();
            }
        });
        return v;
    }

    private void mostrarDatos() {
        mDatabase.child("Events").addValueEventListener(new ValueEventListener() {
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

    public AlertDialog createCustomDialog(Event event1) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.activity_question, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnAceptar = (Button) v.findViewById(R.id.btnAceptar);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancelar);
        RadioGroup q1 = (RadioGroup) v.findViewById(R.id.q1);
        RadioGroup q2 = (RadioGroup) v.findViewById(R.id.q2);
        RadioGroup q3 = (RadioGroup) v.findViewById(R.id.q3);
        RadioGroup q4 = (RadioGroup) v.findViewById(R.id.q4);
        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnAceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int seletedQ1 = q1.getCheckedRadioButtonId();
                        int seletedQ2 = q2.getCheckedRadioButtonId();
                        int seletedQ3 = q3.getCheckedRadioButtonId();
                        int seletedQ4 = q4.getCheckedRadioButtonId();

                        if(!(seletedQ1 == -1) && !(seletedQ2 == -1) && !(seletedQ3 == -1) && !(seletedQ4 == -1) ){

                            AlertDialog dialog, confi;

                            RadioButton rq1 = (RadioButton)q1.findViewById(seletedQ1);
                            RadioButton rq2 = (RadioButton)q2.findViewById(seletedQ2);
                            RadioButton rq3 = (RadioButton)q3.findViewById(seletedQ3);
                            RadioButton rq4 = (RadioButton)q4.findViewById(seletedQ4);

                            if (!rq1.getText().equals("Si") && !rq2.getText().equals("Si") && !rq3.getText().equals("Si") && !rq4.getText().equals("Si")){
                               asistencia(event1);
                            }else{
                                dialog = new AlertDialog
                                        .Builder(getActivity())
                                        .setPositiveButton("Asistir al evento", (dialog1, which) -> {
                                            new AlertDialog
                                                    .Builder(getActivity())
                                                    .setPositiveButton("Aceptar", (dialog2, which1) -> {
                                                        asistencia(event1);
                                                    })
                                                    .setTitle("Asistencia confirmada")
                                                    .setMessage("Ya esta confirmada tu asistencia al evento")
                                                    .create().show();
                                        })
                                        .setNegativeButton("Cancelar", (dialog1, which) -> {
                                            dialog1.dismiss();
                                            alertDialog.dismiss();
                                        })
                                        .setTitle("Precaución")
                                        .setMessage("Te recomendamos no asitir al evento")
                                        .create();
                                dialog.show();
                            }

                        }else{
                            Toast.makeText(getActivity(), "No respondiste alguna pregunta", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        );
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
    }

    public void asistencia( Event evento){
        mDatabase.child("Users").child(idUser).child("Events").child(evento.getId()).setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()){
                    new AlertDialog
                            .Builder(getActivity())
                            .setPositiveButton("Aceptar", (dialog1, which) -> {
                                dialog1.dismiss();
                            })
                            .setTitle("Asistencia confirmada")
                            .setMessage("Ya esta confirmada tu asistencia al evento")
                            .create().show();
                }
            }
        });
    }
}