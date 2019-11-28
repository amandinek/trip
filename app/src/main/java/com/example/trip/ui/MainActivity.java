package com.example.trip.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.trip.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.driving) TextView driver;
    @BindView(R.id.riding)TextView rider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Animation slideRight= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sliderigth);
        driver.startAnimation(slideRight);


        Animation slideLeft= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideleft);
        rider.startAnimation(slideLeft);

        driver.setOnClickListener(this);
        rider.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==rider){
            Intent look =new Intent(MainActivity.this, BookRiderActivity.class);
            startActivity(look);
        }

        if(v==driver){
            Intent look =new Intent(MainActivity.this,DriverHomeActivity.class);
            startActivity(look);
        }

    }
}
