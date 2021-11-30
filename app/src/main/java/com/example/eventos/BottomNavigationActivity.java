package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.eventos.Navigation.EventFragment;
import com.example.eventos.Navigation.HomeFragment;
import com.example.eventos.Navigation.Notification;
import com.example.eventos.Navigation.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    HomeFragment homeFragment = new HomeFragment();
    EventFragment eventFragment = new EventFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    Notification notification = new Notification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            String value1 = extras.getString("Username");
            String value2 = extras.getString("Useremail");
            String value3 = extras.getString("Userid");
            String value4 = extras.getString("Userpass");

            Bundle bundle = new Bundle();
            bundle.putString("Username", value1);
            bundle.putString("Useremail", value2);
            bundle.putString("Userid", value3);
            bundle.putString("Userpass", value4);


            // si estas usando la antigua forma de navegacion de fragments:
            profileFragment.setArguments(bundle);
            homeFragment.setArguments(bundle);
            eventFragment.setArguments(bundle);
            notification.setArguments(bundle);// aquí envías los datos al fragment

        }

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(homeFragment);
    }

        private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        loadFragment(homeFragment);
                        return true;
                    case R.id.eventFragment:
                        loadFragment(eventFragment);
                        return true;
                    case R.id.profileFragment:
                        loadFragment(profileFragment);
                        return true;
                    case  R.id.notification:
                        loadFragment(notification);
                        return true;
                }
                return false;
            }
        };

        public void loadFragment(Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        }
}