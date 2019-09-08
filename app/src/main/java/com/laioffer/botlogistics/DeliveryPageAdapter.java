package com.laioffer.botlogistics;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DeliveryPageAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 3;

        public DeliveryPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return LoginFragment.newInstance();
//                case 1:
//                    return RegisterFragment.newInstance();
//
//                default:
//                    return null;
//            }
            return DeliveryFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Sign In";
//                case 1:
//                    return "Sign Up";
//
//            }
            return "Delivery Method";
//            return null;
        }
}
