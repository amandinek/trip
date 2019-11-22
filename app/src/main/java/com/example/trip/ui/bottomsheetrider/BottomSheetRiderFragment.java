package com.example.trip.ui.bottomsheetrider;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trip.R;

public class BottomSheetRiderFragment extends Fragment {

    private BottomSheetRiderViewModel mViewModel;

    public static BottomSheetRiderFragment newInstance() {
        return new BottomSheetRiderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_rider_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BottomSheetRiderViewModel.class);
        // TODO: Use the ViewModel
    }

}
