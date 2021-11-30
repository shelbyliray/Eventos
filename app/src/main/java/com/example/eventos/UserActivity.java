package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventos.modelo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSingin;

    private String name ="";
    private String email ="";
    private String password ="";

    User user;

    FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = new User();

        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mSingin = (Button) findViewById(R.id.btn_sign_in);
        mSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name = mName.getText().toString();
                 email = mEmail.getText().toString();
                 password = mPassword.getText().toString();

                 if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() ){
                     if(password.length()>=6){
                         registrarUser();
                     }else{
                         Toast.makeText(UserActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                     }
                 }else{
                     validacion();
                     Toast.makeText(UserActivity.this, "Dejo un campo en blanco", Toast.LENGTH_SHORT).show();
                 }
             }
        });
    }


    private void validacion(){
        if (name.isEmpty()){
            mName.setError("Campo requerido");
        }
        if (email.isEmpty()){
            mEmail.setError("Campo requerido");
        }
        if (password.isEmpty()){
            mPassword.setError("Campo requerido");
        }
    }

    private void registrarUser(){
        mfirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = mfirebaseAuth.getCurrentUser().getUid();
                       user.setName(name);
                       user.setEmail(email);
                       user.setPassword(password);
                    mDatabase.child("Users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Intent intent = new Intent(UserActivity.this, BottomNavigationActivity.class);
                                intent.putExtra("UserName", name);
                                intent.putExtra("UserEmail", email);
                                intent.putExtra("UserId", id);
                                intent.putExtra("UserPass", password);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(UserActivity.this, "No se registro el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(UserActivity.this, "Verifique si el correo esta correcto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}