package com.example.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookRiderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.names)EditText names;
    @BindView(R.id.fone)EditText contacts;
    @BindView(R.id.depPoint)EditText pickup;
    @BindView(R.id.arivPoint) EditText destination;
    @BindView(R.id.in_date)EditText date;
    @BindView(R.id.in_time)EditText time;
    @BindView(R.id.settingDate) Button setDate;
    @BindView(R.id.settingTime) Button setTime;
    @BindView(R.id.booking)Button booking;

    private int mYear, mMonth, mDay, mHour, mMinute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_rider);

        ButterKnife.bind(this);

        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
        booking.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v==booking){
            Intent book =new Intent(BookRiderActivity.this,ConfrimRiderActivity.class);
            startActivity(book);

        }
        if(v==setDate){
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
        if(v==setTime){
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
