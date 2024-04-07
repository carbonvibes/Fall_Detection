package com.example.Child;

/* Copyright 2016-2021 The MathWorks, Inc. */

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Child.databinding.FragmentAppBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class AppFragment extends Fragment {

    FragmentAppBinding fragmentAppBinding;
    

    
    private OnFragmentInteractionListener mListener;
    private boolean isWidgetsLayoutHidden = false;

    public AppFragment() {
    }

    public static AppFragment newInstance() {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener == null) {
            try {
                mListener = (OnFragmentInteractionListener) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }
        mListener.onFragmentCreate("App");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAppBinding = FragmentAppBinding.inflate(inflater, container, false);
        return fragmentAppBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.onFragmentStart("App");
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentResume("App");
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener.onFragmentPause("App");
    }

    public void setFragmentInteractionListener(Activity activity) {
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    
}
