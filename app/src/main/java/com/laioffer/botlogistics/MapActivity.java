package com.laioffer.botlogistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MapActivity extends AppCompatActivity implements TransactionManager{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        doTransactionFragment(new MapFragment());
    }

    @Override
    public void doTransactionFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, fragment, "").commit();
    }

    @Override
    public void doActivityTransaction(Class clazz, boolean isFinish) {

    }
}
