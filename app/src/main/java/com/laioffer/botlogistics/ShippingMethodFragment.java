package com.laioffer.botlogistics;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laioffer.entity.ConfirmationRequest;
import com.laioffer.entity.ShippingMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingMethodFragment extends Fragment{
    private ListView listView;
    protected DatabaseReference database;
    private ShippingMethodAdapter shippingMethodAdapter;
    private OrderConfirmationFragment orderConfirmationFragment;
    protected TransactionManager transactionManager;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            transactionManager = (TransactionManager) context;
        } catch (ClassCastException e) {
            Log.d("ERROR", "Can't cast Context to TransactionManager");
        }
    }

    public ShippingMethodFragment() {
        // Required empty public constructor
    }

    public static ShippingMethodFragment newInstance(JSONObject json, ConfirmationRequest confirm) {
        Bundle args = new Bundle();
        args.putString("json", json.toString());
        args.putSerializable("CONFIRM", confirm);
        ShippingMethodFragment fragment = new ShippingMethodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shipping_method, container, false);

        List<ShippingMethod> methods = new ArrayList<>();

        // get json and convert it to List<ShippingMethod>
        String str = (String) getArguments().get("json");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> stationIterator = jsonObject.keys();
        try {
            while(stationIterator.hasNext()) {
                String stationName = stationIterator.next();
                Log.d("stationName", stationName);
                if (jsonObject.get(stationName) instanceof JSONObject) {
                    JSONObject station = (JSONObject) jsonObject.get(stationName);
                    Iterator<String> machineTypeIterator = station.keys();
                    while (machineTypeIterator.hasNext()){
                        String machineName = machineTypeIterator.next();
                        Log.d("machineName", machineName);
                        if(station.get(machineName) instanceof JSONObject){
                            JSONObject machine = (JSONObject) station.get(machineName);
                            ShippingMethod method = new ShippingMethod();
                            if(machine.get("price") instanceof Integer){
                                Log.d("Error", "Invalid message!");
                            }else{
                                method.setQuantity((Integer) machine.get("quantity"))
                                        .setPrice((Double) machine.get("price"))
                                        .setDutation((Double) machine.get("duration"))
                                        .setType(machineName)
                                        .setStation(stationName);
                                methods.add(method);
                            }
                        }
                    }
                }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) view.findViewById(R.id.station_list);

        // Assign adapter to ListView.
        shippingMethodAdapter = new ShippingMethodAdapter(getActivity());
        listView.setAdapter(shippingMethodAdapter);

        // pass ShippingMethods to Adapter
        shippingMethodAdapter.updateStations(methods);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShippingMethod method = getOrderById(i);
                ConfirmationRequest confirm = (ConfirmationRequest) getArguments().get("CONFIRM");
                confirm.setUserId(Config.username)
                        .setShippingMethod(method.getType())
                        .setStationId(method.getStation())
                        .setDuration(method.getDutation())
                        .setPrice(method.getPrice());
                orderConfirmationFragment = OrderConfirmationFragment.newInstance(confirm);
                transactionManager.doTransactionFragment(orderConfirmationFragment, true, true);
            }
        });

        return view;
    }

    // get selected order
    public ShippingMethod getOrderById(int position) {
        if (position < 0 || position >= shippingMethodAdapter.getMethods().size()) {
            return null;
        }
        return shippingMethodAdapter.getMethods().get(position);
    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_shipping_method;
    }
}
