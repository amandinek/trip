package com.example.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.trip.ui.bottomsheetrider.BottomSheetRiderFragment;

public class BottomSheetRider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_rider_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BottomSheetRiderFragment.newInstance())
                    .commitNow();
        }
    }
}
