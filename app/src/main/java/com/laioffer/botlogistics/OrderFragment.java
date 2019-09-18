package com.laioffer.botlogistics;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.laioffer.entity.Order;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment{

    OnItemSelectListener callBack;
    private FloatingActionButton fabReport;
    private ListView listView;
    protected DatabaseReference database;

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


        final String username = Config.username;

        database = FirebaseDatabase.getInstance().getReference();

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
                // sort orders based on delivery time
                Collections.sort(orders, new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        if(o1.getDeliveryTime() == o2.getDeliveryTime()){
                            return 0;
                        }
                        return o1.getDeliveryTime() > o2.getDeliveryTime() ? -1 : 1;
                    }
                });
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
        transactionManager.doTransactionFragment(DeliveryFragment.newInstance(), true, true);
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
