package com.laioffer.botlogistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ContentActivity extends AppCompatActivity {
    private ViewPager viewpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // setup viewpager and tablayout
        viewpage = findViewById(R.id.viewpager_content);
        ContentPageAdapter contentPageAdapter = new ContentPageAdapter(getSupportFragmentManager());
        viewpage.setAdapter(contentPageAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_content);
        tabLayout.setupWithViewPager(viewpage);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
    }
}
