package com.example.deepgandhi.movieguidedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreenActvity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView_name,textView_email;
    Toolbar toolbar;
    SearchView searchView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_actvity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);

        View header_view=navigationView.getHeaderView(0);
        textView_name=header_view.findViewById(R.id.header_name);
        textView_email=header_view.findViewById(R.id.header_email);
        textView_name.setText(preferences.getString("username",""));
        textView_email.setText(preferences.getString("email",""));

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.main_frame_layout,new main_frame());
        //transaction.addToBackStack("");
        //HomeScreenActvity.this.getActionBar().setTitle("Popular Shows");
        getSupportActionBar().setTitle("Popualr Shows");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen_actvity, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.equals("") || !s.isEmpty()) {
                    Bundle bundle=new Bundle();
                    bundle.putString("searched",s);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    search_frame sf=new search_frame();
                    sf.setArguments(bundle);
                    transaction.replace(R.id.main_frame_layout, sf);
                    transaction.commit();
                }
                else{
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.main_frame_layout, new main_frame());
                    transaction.commit();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_user){
            Intent intent=new Intent(HomeScreenActvity.this,ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trending){
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.main_frame_layout,new trending_frame());
            HomeScreenActvity.this.getSupportActionBar().setTitle("Trending Shows");
            transaction.addToBackStack("");
            transaction.commit();

        }
        else if(id==R.id.nav_today){
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.main_frame_layout,new today_frame());
            HomeScreenActvity.this.getSupportActionBar().setTitle("Today Airing");
            transaction.addToBackStack("");
            transaction.commit();

        }
        else if(id==R.id.nav_logout){
            SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.remove("email");
            editor.remove("username");
            editor.remove("id");
            editor.commit();
            Intent i=new Intent(HomeScreenActvity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
