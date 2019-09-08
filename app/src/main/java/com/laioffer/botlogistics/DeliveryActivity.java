package com.laioffer.botlogistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class DeliveryActivity extends AppCompatActivity {


    private ViewPager viewpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        // setup viewpager
        viewpage = findViewById(R.id.viewpager);
        DeliveryPageAdapter deliveryPageAdapter = new DeliveryPageAdapter(getSupportFragmentManager());
        viewpage.setAdapter(deliveryPageAdapter);

    }

    // switch viewpage to #page
    public void setCurrentPage(int page) {
        viewpage.setCurrentItem(page);
    }
}
