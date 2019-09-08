package com.laioffer.botlogistics;


import android.content.Context;
import android.content.Intent;
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
        sizeButton = (RadioButton) sizeOptions.getChildAt(sizeOptions.getCheckedRadioButtonId());
        submitButton = (Button) view.findViewById(R.id.submit);
        database = FirebaseDatabase.getInstance().getReference();


        submitButton.setText(getString(R.string.login));

//        // test database connection
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");
//        myRef.setValue("Hello, World!");
        // login the submitButton and register
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pickUp = pickupEditText.getText().toString();
                final String dropOff = dropoffEditText.getText().toString();
                final String time=timePicker.toString();
                final String size=sizeButton.getText().toString();
                database.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild() && (.equals(dataSnapshot.child(username).child("user_password").getValue()))) {
//                            Config.username = username;
//                            startActivity(new Intent(getActivity(), ControlPanel.class));
//
//                        } else {
//                            Toast.makeText(getActivity(),"Please try to login again", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        return view;

    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.fragment_delivery;
    }


}
