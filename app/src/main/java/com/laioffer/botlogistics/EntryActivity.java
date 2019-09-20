package com.laioffer.botlogistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntryActivity extends AppCompatActivity implements TransactionManager{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        EntryFragment entryFragment = new EntryFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.entry_frame, entryFragment).commit();
    }

    @Override
    public void doTransactionFragment(Fragment fragment, boolean isAnimate, boolean isAddBackStack) {

    }

    @Override
    public void doActivityTransaction(Class clazz, boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    @Override
    public void doCleanBackStack() {

    }

    @Override
    public void doBackStack() {

    }
}
