package com.example.trip.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trip.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookRiderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.names)EditText names;
    @BindView(R.id.fone)EditText contacts;

    @BindView(R.id.in_time)EditText time;
    @BindView(R.id.booking)Button booking;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatabaseReference clientReference;
    private ValueEventListener  clientReferenceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clientReference = FirebaseDatabase
                .getInstance()
                .getReference().child(LOCATION_SERVICE);


        clientReferenceListener =  clientReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Locations updated", "location: " + location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_rider);

        ButterKnife.bind(this);

      ;
        time.setOnClickListener(this);
        booking.setOnClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();


    }

    public void saveLocationToFirebase(String location) {
        clientReference.push().setValue(location);
    }

    @Override
    public void onClick(View v) {

        if (v==booking){

            if((names==null)||(contacts==null)||(time==null)){
                Toast.makeText(this, "please fill the blank", Toast.LENGTH_SHORT).show();
            }

           else{
                saveUser();
                Intent book =new Intent(BookRiderActivity.this, RiderMapsActivity.class);
                startActivity(book);
            }

        }

        if(v==time){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private void saveUser() {
        String fullNames=names.getText().toString();
        String phone=contacts.getText().toString();
        String timedepart=time.getText().toString();
        String name=names.getText().toString();

        boolean validName=isValidName(name);
        boolean validPhone=isValidPhone(phone);
        boolean validTime=isValidTime(timedepart);

    }

    private boolean isValidName(String name) {

        if (name.equals("")){
            names.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPhone(String phone) {

        if (phone.length() < 10){
            contacts.setError("Please enter a valid phone number");
            return false;
        }
        return true;
    }
    private boolean isValidTime(String timedepart) {

        if (timedepart.equals("")){
            time.setError("Please enter your pickup time ");
            return false;
        }
        return true;
    }
}
