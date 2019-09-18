package com.laioffer.botlogistics;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laioffer.entity.ConfirmationRequest;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Calendar;

public class DeliveryFragment extends Fragment {
    protected EditText pickupEditText;
    protected EditText dropoffEditText;
    protected RadioGroup sizeOptions;
    protected TimePicker timePicker;
    protected Button submitButton;
    protected DatabaseReference database;

    private String pickUp;
    private String dropOff;
    private Long time;
    private String size;

    protected TransactionManager transactionManager;

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
        submitButton = (Button) view.findViewById(R.id.delivery_submit);
        submitButton.setText(getString(R.string.submit));

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

        // submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pickUp = pickupEditText.getText().toString();
                 dropOff = dropoffEditText.getText().toString();

               // read by a calendar
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, timePicker.getCurrentHour());
                cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                time = cal.getTimeInMillis();

                 Log.d("pickUp", pickUp);
                 Log.d("dropOff", dropOff);
                 Log.d("time", Long.toString(time));
                 Log.d("size", size);

                 submitButton.setText("Clicked !");

                // Instantiate the RequestQueue.
                RequestQueue queue = HttpHelper.getInstance(getContext()).getRequestQueue();
                String url = Config.url_prefix + "shippingMethod";

                JSONObject parameters = new JSONObject();
                try {
                    parameters.put("destination", dropOff);
                    parameters.put("shippingAddress", pickUp);
                    parameters.put("shippingTime", time);
                    parameters.put("size", size);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                        parameters,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                ConfirmationRequest confirm = new ConfirmationRequest();
                                confirm.setDestination(dropOff);
                                confirm.setShippingAddress(pickUp);
                                confirm.setShippingTime(time);
                                transactionManager.doTransactionFragment(ShippingMethodFragment.newInstance(response, confirm), true, true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //   Handle Error
                                Log.d("Error", error.toString());
                            }
                        }) {
                };
                queue.add(postRequest);

            }
        });



        return view;

    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_delivery;
    }


}
