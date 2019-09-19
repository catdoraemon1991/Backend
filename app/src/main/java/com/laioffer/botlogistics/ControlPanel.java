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

                        //TODO implement updates
//                        if (menuItem.getItemId() == R.id.drawer_home) {
//                            doTransactionFragment(new OrderFragment(), true, false);
//                        }
                        return true;
                    }
                });

        // TODO: How to get activity context more elegent
        final Context context = this;

        final FloatingSearchView mSearchView = findViewById(R.id.floating_search_view);

        mSearchView.setOnHomeActionClickListener(
                () -> drawerLayout.openDrawer(GravityCompat.START));

//        // set listener to show suggestions
//        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//                List<Order> orders = new ArrayList<>();
//                database.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            Order order = child.getValue(Order.class);
//                            if(order.getOrderId().contains(newQuery)){
//                                orders.add(order);
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//                //pass them on to the search view
//                mSearchView.swapSuggestions(orders);
//            }
//        });

        // set listener to search content
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }
            @Override
            public void onSearchAction(final String currentQuery) {
                database.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(currentQuery)) {
                            Order order = dataSnapshot.child(currentQuery).getValue(Order.class);
                            OrderDetailDialog dialog = OrderDetailDialog.newInstance(context, order);
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        // add Fragment to the activity
        orderFragment = OrderFragment.newInstance();
        doTransactionFragment(orderFragment, true, false);

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
