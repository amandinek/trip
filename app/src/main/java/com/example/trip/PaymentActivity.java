package com.example.trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {

    @BindView(R.id.paymen)
    Button payment;
    @BindView(R.id.ratting)
    RatingBar rate;
    @BindView(R.id.money)
    Button cash;
    @BindView(R.id.mobil)
    Button mobile;
    @BindView(R.id.amount)
    EditText price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        payment.setOnClickListener(this);
        rate.setOnRatingBarChangeListener(this);
        cash.setOnClickListener(this);
        mobile.setOnClickListener(this);


    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


    }

    @Override
    public void onClick(View v) {
        if (v == payment) {
            Toast.makeText(PaymentActivity.this, "Thank you to ride with us!!!", Toast.LENGTH_LONG).show();
            Intent rate = new Intent(PaymentActivity.this, RatingActivity.class);
            startActivity(rate);

        }
        if (v == mobile) {
            Uri number = Uri.parse("tel:*182*6#");
            Intent callIntent = new Intent(Intent.ACTION_CALL, number);

            startActivity(callIntent);

//
//           if (ActivityCompat.checkSelfPermission(PaymentActivity.this,
//                   Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//
//           }

        }

    }
}

