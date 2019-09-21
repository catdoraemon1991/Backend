package com.laioffer.botlogistics;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends AppCompatActivity implements OrderFragment.OnItemSelectListener, TransactionManager {
    private DrawerLayout drawerLayout;
    private OrderFragment orderFragment;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        database = FirebaseDatabase.getInstance().getReference();


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

                        if (menuItem.getItemId() == R.id.drawer_home) {
                            orderFragment.updateOrderList();
                        }
                        return true;
                    }
                });

        final Context context = this;

//        final LottieAnimationView animationView = findViewById(R.id.animation_view);
//        animationView.setAnimation(R.raw.lottie_anim_entry);
//        animationView.playAnimation();

        final FloatingSearchView mSearchView = findViewById(R.id.floating_search_view);

        // add Fragment to the activity
        orderFragment = OrderFragment.newInstance();
        doTransactionFragment(orderFragment, true, false);

        mSearchView.setOnHomeActionClickListener(
                () -> drawerLayout.openDrawer(GravityCompat.START));

        // set listener to search content
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            orderFragment.searchOrder(newQuery);
        });

    }

    @Override
    public void onItemSelected(int position, Order order) {
        doTransactionFragment(MapFragment.newInstance(order), true, true);
    }

    @Override
    public void onBackPressed() {
        Log.d("fragment left", Integer.toString(getSupportFragmentManager().getBackStackEntryCount()));
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
        startSearch(null, false, appData, false);
        return true;
    }

    private void logout() {
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }

    private void search() {
    }

    @Override
    public void doTransactionFragment(Fragment fragment, boolean isAnimate, boolean isAddBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isAddBackStack) {
            ft.addToBackStack(null);
        }
        if (isAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        ft.replace(R.id.content_frame, fragment).commit();
    }



    @Override
    public void doActivityTransaction(Class clazz, boolean isFinish) {

    }

    @Override
    public void doCleanBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void doBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
        }
    }

    public void updateOrderList(){
        orderFragment.updateOrderList();
    }
}
