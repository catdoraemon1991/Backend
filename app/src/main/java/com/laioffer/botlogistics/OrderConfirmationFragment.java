package com.laioffer.botlogistics;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laioffer.entity.ConfirmationRequest;
import com.laioffer.entity.ShippingMethod;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderConfirmationFragment extends Fragment {
    private ConfirmationRequest confirm;
    private TransactionManager transactionManager;
    public OrderConfirmationFragment() {
        // Required empty public constructor
    }

    public static OrderConfirmationFragment newInstance(ConfirmationRequest confirm) {

        Bundle args = new Bundle();
        args.putSerializable("CONFIRM", confirm);
        OrderConfirmationFragment fragment = new OrderConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_order_confirmation, container, false);
        confirm = (ConfirmationRequest) getArguments().get("CONFIRM");
        Log.d("The method of current:", confirm.toString());

        TextView confirmDestination = (TextView)view.findViewById(R.id.confirmation_destination);
        TextView confirmShippingAddress = (TextView)view.findViewById(R.id.confirmation_shippingAddress);
        TextView confirmShippingMethod = (TextView)view.findViewById(R.id.confirmation_shippingMethod);
        TextView confirmStationId = (TextView)view.findViewById(R.id.confirmation_stationId);
        TextView confirmUserId = (TextView)view.findViewById(R.id.confirmation_userId);
        TextView confirmDuration = (TextView)view.findViewById(R.id.confirmation_duration);
        TextView confirmPrice = (TextView)view.findViewById(R.id.confirmation_price);
        Button confirmButton = (Button) view.findViewById(R.id.confirmation_submit);

        confirmDestination.setText(confirm.getDestination());
        confirmShippingAddress.setText(confirm.getShippingAddress());
        confirmShippingMethod.setText(confirm.getShippingMethod());
        confirmStationId.setText(confirm.getStationId());
        confirmUserId.setText(confirm.getUserId());
        confirmDuration.setText(Utils.convertDuration(confirm.getDuration()));
        confirmPrice.setText(Utils.convertPrice(confirm.getPrice()));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // confirm order
                // Instantiate the RequestQueue.
                RequestQueue queue = HttpHelper.getInstance(getContext()).getRequestQueue();
                String url = Config.url_prefix + "orderConfirmation";

                JSONObject parameters = new JSONObject();
                try {
                    parameters.put("destination", confirm.getDestination());
                    parameters.put("shippingAddress", confirm.getShippingAddress());
                    parameters.put("shippingTime", confirm.getShippingTime());
                    parameters.put("userId", confirm.getUserId());
                    parameters.put("shippingMethod", confirm.getShippingMethod());
                    parameters.put("stationId", confirm.getStationId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                        parameters,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                transactionManager.doTransactionFragment(new OrderFragment());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //   Handle Error
                                Log.d("Error", error.toString());
                                Toast.makeText(getActivity(),"Invalid choice!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                };
                queue.add(postRequest);
            }
        });

        return view;
    }

}
