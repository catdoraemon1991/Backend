package com.laioffer.botlogistics;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.airbnb.lottie.LottieAnimationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFragment extends Fragment {

    protected TransactionManager transactionManager;

    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        transactionManager = (TransactionManager) context;
    }

    public EntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.animation_view);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:","end");
                //Your code for remove the fragment
                try {
                    transactionManager.doActivityTransaction(OnBoardingActivity.class, true);
                } catch(Exception ex) {
                    ex.toString();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });
        return view;
    }

}
