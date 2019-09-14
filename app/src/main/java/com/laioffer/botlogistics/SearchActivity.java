package com.laioffer.botlogistics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity {
    protected TextView search_orderId;
    protected TextView search_shippingAddress;
    protected TextView search_deliveryTime;
    protected Button search_backButton;
    protected DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_orderId = (TextView) findViewById(
                R.id.search_order_id);
        search_shippingAddress = (TextView) findViewById(
                R.id.search_order_shipping_address);
        search_deliveryTime = (TextView) findViewById(
                R.id.search_order_delivery_time);
        search_backButton = (Button) findViewById(R.id.search_back);;

        database = FirebaseDatabase.getInstance().getReference();
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        search_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    private void doMySearch(String query){
        final String orderId = query;
        database.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(orderId)){
                    Order order = dataSnapshot.child(orderId).getValue(Order.class);
                    search_orderId.setText(order.getOrderId());
                    search_shippingAddress.setText(order.getShippingAddress());
                    search_deliveryTime.setText(DataService.convertTime(order.getDeliveryTime()));
                }else{
                    search_orderId.setText("Not Found");
                    search_shippingAddress.setText("Not Found");
                    search_deliveryTime.setText("Not Found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void backToMain(){
        Intent intent = new Intent(this, ControlPanel.class);
        startActivity(intent);
        finish();
    }
}
