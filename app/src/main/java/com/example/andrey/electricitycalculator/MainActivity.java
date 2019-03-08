package com.example.andrey.electricitycalculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.orm.SugarContext;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(getApplicationContext());
        setContentView(R.layout.activity_main);

        //== Adds NavigationDrawer to the activity ==//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_nav_drawer);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(abdt);
        abdt.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        Fragment fragment = null;

        switch (id){
            case R.id.to_one_rate:
                fragment = new OneRateCounter();
                break;
            case R.id.to_two_rate:
                fragment = new TwoRateCounter();
                break;
            case R.id.to_three_rate:
                fragment = new ThreeRateCounter();
                break;
            case R.id.to_history:
                fragment = new History();
                break;
            case R.id.to_rate_us:
                Toast.makeText(this, "Данный раздел на стадии раработки",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }

        if (fragment != null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_main, fragment, "").commit();
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
