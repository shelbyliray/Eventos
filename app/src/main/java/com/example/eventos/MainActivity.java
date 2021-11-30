package com.example.eventos;


import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventos.Navigation.ProfileFragment;
import com.example.eventos.modelo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private TextView sign_in;
    private Button log_in;
    private EditText mEmail;
    private EditText mPassword;
    private String email ="";
    private String password ="";
    private CheckBox bCerrar;

    FirebaseDatabase mDB;
    FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;
    User user;

    private static final String STRING_PREFERENCES = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        bCerrar = (CheckBox) findViewById(R.id.btnNCS);




        //boton SIGNIN
        sign_in = (TextView) findViewById(R.id.btn_signin);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();
            }
        });

        //boton LOGIN
        log_in = (Button) findViewById(R.id.btn_login);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                if(validacion()){
                    login();
                }else {
                    Toast.makeText(MainActivity.this, "Dejo un campo en blanco", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void login(){
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int n =0;
                for (DataSnapshot objectSnapshot : snapshot.getChildren()){
                    String correo = objectSnapshot.getValue(User.class).getEmail();
                    String contra = objectSnapshot.getValue(User.class).getPassword();
                    String nombre = objectSnapshot.getValue(User.class).getName();
                    String id = objectSnapshot.getKey();
                    if(email.equals(correo) && password.equals(contra)){
                        n=1;
                        user = objectSnapshot.getValue(User.class);
                        Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
                        intent.putExtra("Username", nombre);
                        intent.putExtra("Useremail", correo);
                        intent.putExtra("Userid", id);
                        intent.putExtra("Userpass", contra);
                        startActivity(intent);  // se inicia otra Activity y se le envían datos
                        finish();
                    }
                }
                if(n == 0) {
                    Toast.makeText(MainActivity.this, "El correo y contraseña no concuerdan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validacion(){
        boolean l = true;
        if (email.isEmpty()){
            mEmail.setError("Campo requerido");
            l=false;
        }
        if (password.isEmpty()){
            mPassword.setError("Campo requerido");
            l=false;
        }
        return l;
    }

    public void guardarEstado(){

    }
}