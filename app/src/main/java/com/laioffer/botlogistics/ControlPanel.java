package com.laioffer.botlogistics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class ControlPanel extends AppCompatActivity implements OrderFragment.OnItemSelectListener {
    private DrawerLayout drawerLayout;
    private OrderFragment orderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.baseline_home_black_18dp);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        final TextView user_textview = (TextView) drawerView.findViewById(R.id.user_name);
                        user_textview.setText("Hello, " + Config.username);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getItemId() == R.id.drawer_logout) {
                            Config.username = null;
                            logout();
                        }
                        if(menuItem.getItemId() == R.id.drawer_search){
                            search();
                        }
                        return true;
                    }
                });
        // add Fragment to the activity
        orderFragment = OrderFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, orderFragment).commit();
    }

    @Override
    public void onItemSelected(int position, Order order) {
        //TODO handle in orderfragment itself
//        orderFragment.onItemSelected(position);
//        Order order = orderFragment.getOrderById(position);
//        Intent intent = new Intent(this, MapActivity.class);
//        intent.putExtra("EXTRA_ORDER", new Gson().toJson(order));
//        startActivity(intent);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, MapFragment.newInstance(order)).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        // sappData.putBoolean(SearchActivity.JARGON, true);
        startSearch(null, false, appData, false);
        return true;
    }

    private void logout() {
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }

    private void search(){
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
    }
}
