package com.example.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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
    @BindView(R.id.in_date)EditText date;
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

        date.setOnClickListener(this);
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
            String fullNames=names.getText().toString();
            String phone=contacts.getText().toString();
            String datedepart=date.getText().toString();
            String timedepart=time.getText().toString();


            Intent book =new Intent(BookRiderActivity.this,RiderMapsActivity.class);
            startActivity(book);

        }
        if(v==date){
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
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
}
