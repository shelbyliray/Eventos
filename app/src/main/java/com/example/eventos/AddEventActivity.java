package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventos.Navigation.EventFragment;
import com.example.eventos.modelo.Event;
import com.example.eventos.modelo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private EditText efecha,ehora, enombre, edireccion;
    private Button mAddEvent;
    private  int dia,mes,ano,hora,minutos;

    private String name ="";
    private String direction ="";
    private String fecha ="";
    private String hours ="";
    private  String Uid="", n,p,e;


    Event evento;

    FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Uid = getIntent().getStringExtra("Userid");
        n = getIntent().getStringExtra("Username");
        e = getIntent().getStringExtra("Useremail");
        p = getIntent().getStringExtra("Userpass");

        enombre=(EditText)findViewById(R.id.name);
        edireccion=(EditText)findViewById(R.id.direction);
        efecha=(EditText)findViewById(R.id.fecha);
        ehora=(EditText)findViewById(R.id.hora);
        mAddEvent = (Button) findViewById(R.id.addEvent);
        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = enombre.getText().toString();
                direction = edireccion.getText().toString();
                fecha = efecha.getText().toString();
                hours = ehora.getText().toString();

                if(!name.isEmpty() && !direction.isEmpty() && !fecha.isEmpty() && !hours.isEmpty()){
                   registrarEvent();
                }else{
                    validacion();
                    Toast.makeText(AddEventActivity.this, "Dejo un campo en blanco", Toast.LENGTH_SHORT).show();
                }

            }
        });

        efecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
        ehora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour();
            }
        });

    }

    private void validacion(){
        if (name.isEmpty()){
            enombre.setError("Campo requerido");
        }
        if (direction.isEmpty()){
            edireccion.setError("Campo requerido");
        }
        if (fecha.isEmpty()){
            efecha.setError("Campo requerido");
        }
        if (hours.isEmpty()){
            ehora.setError("Campo requerido");
        }
    }


    private void registrarEvent(){
        String id = UUID.randomUUID().toString().replace("-", "");
        evento = new Event(id, name, direction, fecha, hours, false);
        mDatabase.child("Users").child(Uid).child("Events").child(id).setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()){
                    Intent intent = new Intent(AddEventActivity.this, BottomNavigationActivity.class);
                    intent.putExtra("Userid", Uid);
                    intent.putExtra("Username", n);
                    intent.putExtra("Useremail", e);
                    intent.putExtra("Userpass", p);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AddEventActivity.this, "No se registro el evento", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDatabase.child("Events").child(id).setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(AddEventActivity.this, EventFragment.class));
                    finish();
                }else{
                    Toast.makeText(AddEventActivity.this, "No se registro el evento", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public  void date(){
        final Calendar c= Calendar.getInstance();
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH);
        ano=c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                efecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
            }
        }
                ,dia,mes,ano);
        datePickerDialog.show();

    }
    public void hour() {
            final Calendar c= Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute+"0");
                }
            },hora,minutos,false);
            timePickerDialog.show();
    }
}