/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amansoni.tripbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;

import java.util.Calendar;
import java.util.Date;

public class AddPlaceActivity extends ActionBarActivity {

    protected static final String TAG = "AddActivity";

    protected static EditText mTripName;
    protected static TextView mStartDatePicker;
    protected static TextView mEndDatePicker;
    protected static EditText mNotes;
    protected static TextView mCurrentDate;
    static java.text.DateFormat dateFormat;

    private View.OnTouchListener dateOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                showDatePickerDialog(view);
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        mTripName = (EditText) findViewById(R.id.trip_add_name);

        mStartDatePicker = (TextView) findViewById(R.id.trip_add_start);
        mStartDatePicker.setOnTouchListener(dateOnTouchListener);

        mEndDatePicker = (TextView) findViewById(R.id.trip_add_end);
        mEndDatePicker.setOnTouchListener(dateOnTouchListener);

        mNotes = (EditText) findViewById(R.id.trip_add_notes);
        mCurrentDate = mStartDatePicker;
        dateFormat = DateFormat.getMediumDateFormat(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_edit, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.add_item_title) + " " + getResources().getString(R.string.title_place));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_cancel) {
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.dialog_cancel_save_title)
//                    .setMessage(R.string.dialog_cancel_save_message)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            finish();
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, null).show();
//            return true;
//        }

        if (item.getItemId() == R.id.action_item_save) {
            saveItem();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        TripBookItemData tripBookItemData = new TripBookItemData(this);
        TripBookItem tripBookItem = new TripBookItem(mTripName.getText().toString(), TripBookItem.TYPE_TRIP);
        tripBookItem.setCreatedAt(mStartDatePicker.getText().toString());
        tripBookItem.setEndDate(mEndDatePicker.getText().toString());
        tripBookItem = tripBookItemData.add(tripBookItem);
    }

    /**
     * Shows a toast with the given text.
     */
    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        mCurrentDate = (TextView)v;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            mTimePicker.setText(hourOfDay + ":" + minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            year = -1900 + year;
            mCurrentDate.setText(dateFormat.format(new Date(year, month, day)));
            //mCurrentDate.setText(day + "/" + month + "/" + year);
        }
    }

}
