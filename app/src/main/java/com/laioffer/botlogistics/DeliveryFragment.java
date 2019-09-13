package com.laioffer.botlogistics;


import android.content.Context;
import android.content.Intent;
import android.net.http.RequestQueue;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {
    protected EditText pickupEditText;
    protected EditText dropoffEditText;
    protected RadioGroup sizeOptions;
    protected RadioButton sizeButton;
    protected TimePicker timePicker;
    protected Button submitButton;
    protected DatabaseReference database;

    protected TransactionManager transactionManager;

    private String pickUp;
    private String dropOff;
    private String time;
    private String size;


    public static DeliveryFragment newInstance() {
        Bundle args = new Bundle();

        DeliveryFragment fragment = new DeliveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DeliveryFragment() {
        // Required empty public constructor
    }

    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        transactionManager = (TransactionManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayout(), container, false);
        pickupEditText = (EditText) view.findViewById(R.id.editTextPickUp);
        dropoffEditText = (EditText) view.findViewById(R.id.editTextDropOff);
        timePicker=(TimePicker)view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        sizeOptions = (RadioGroup) view.findViewById(R.id.size);
        submitButton = (Button) view.findViewById(R.id.submit);
        database = FirebaseDatabase.getInstance().getReference();


        sizeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.sizeS:
                        size = getString(R.string.size_s);
                        break;
                    case R.id.sizeM:
                        size = getString(R.string.size_m);
                        break;

                    case R.id.sizeL:
                        size = getString(R.string.size_l);
                        break;
                }
            }
        });

        submitButton.setText(getString(R.string.submit));

        // submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pickUp = pickupEditText.getText().toString();
                 dropOff = dropoffEditText.getText().toString();
                 time = timePicker.toString();

//                // Instantiate the RequestQueue.
//                RequestQueue queue = Volley.newRequestQueue(this);
//                String url ="http://www.google.com";
//
//                // Request a string response from the provided URL.
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                // Display the first 500 characters of the response string.
//                                textView.setText("Response is: "+ response.substring(0,500));
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        textView.setText("That didn't work!");
//                    }
//                });
//
//                // Add the request to the RequestQueue.
//                queue.add(stringRequest);

            }
        });



        return view;

    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_delivery;
    }


}
