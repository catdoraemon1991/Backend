package com.laioffer.botlogistics;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment{
    private FloatingActionButton fabReport;
    private DeliveryDialog dialog;
    private ListView listView;
    protected DatabaseReference database;
    OnItemSelectListener callBack;
    private OrderAdapter orderAdapter;
    protected TransactionManager transactionManager;

    // Container Activity must implement this interface
    public interface OnItemSelectListener {
        public void onItemSelected(int position, Order order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (OnItemSelectListener) context;
        } catch (ClassCastException e) {
            //do something
        }
        transactionManager = (TransactionManager) context;
    }

    public static OrderFragment newInstance() {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        fabReport = (FloatingActionButton) view.findViewById(R.id.fab);
        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(null, null);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();
        final String username = Config.username;
        listView = (ListView) view.findViewById(R.id.order_list);

        // Assign adapter to ListView.
        orderAdapter = new OrderAdapter(getActivity());
        listView.setAdapter(orderAdapter);
        database.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if (order.getUserId().equals(username)) {
                        orders.add(order);
                    }
                }
                orderAdapter.updateOrder(orders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callBack.onItemSelected(i, getOrderById(i));
            }
        });


        return view;
    }


    private void showDialog(String label, String prefillText) {
//        int cx = (int) (fabReport.getX() + (fabReport.getWidth() / 2));
//        int cy = (int) (fabReport.getY()) + fabReport.getHeight() + 56;
//        dialog = DeliveryDialog.newInstance(getContext(), cx, cy);
//        dialog.show();
        transactionManager.doTransactionFragment(DeliveryFragment.newInstance());
    }

    // Change background color if the item is selected
    public void onItemSelected(int position) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            if (position == i) {
                listView.getChildAt(i).setBackgroundColor(Color.BLUE);
                Order r = orderAdapter.getOrders().get(i);
            } else {
                listView.getChildAt(i).setBackgroundColor(Color.parseColor("#FAFAFA"));
            }
        }
    }


    // get selected order
    public Order getOrderById(int position) {
        if (position < 0 || position >= orderAdapter.getOrders().size()) {
            return null;
        }
        return orderAdapter.getOrders().get(position);
    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_order;
    }

}
