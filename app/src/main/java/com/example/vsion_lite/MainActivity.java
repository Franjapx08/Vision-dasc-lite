package com.example.vsion_lite;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vsion_lite.fragment.Location;
import com.example.vsion_lite.fragment.Perfil;
import com.example.vsion_lite.fragment.Teacher;
import com.example.vsion_lite.fragment.Tower;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Mapa");
                    selectFragment(Location.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("Direcctorio Maestros");
                    selectFragment(Teacher.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    setTitle("Direcctorio Edificios");
                    selectFragment(Tower.newInstance());
                    return true;
                case R.id.perfil:
                    setTitle("Perfil");
                    selectFragment(Perfil.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void selectFragment(Fragment blankFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.cont_fragment, blankFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        selectFragment(Location.newInstance());

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
