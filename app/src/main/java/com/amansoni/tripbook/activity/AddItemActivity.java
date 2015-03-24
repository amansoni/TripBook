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

package com.amansoni.tripbook.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amansoni.tripbook.fragment.HorizontalListFragment;
import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.util.ImageWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends ActionBarActivity {

    protected static final String TAG = "AddActivity";
    protected static String mItemType;
    protected static ArrayList<TripBookItem> selectedItems;
    protected static HorizontalListFragment fragmentFriends;
    protected static HorizontalListFragment fragmentImages;
    protected static HorizontalListFragment fragmentPlace;
    protected static HorizontalListFragment fragmentTrip;
    static java.text.DateFormat dateFormat;
    private final int SELECT_PHOTO = 1;
    protected ImageView mMainImage;
    protected TripBookItem mTripbookItem;
    protected EditText mTripName;
    protected TextView mStartDatePicker;
    protected TextView mEndDatePicker;
    protected EditText mNotes;
    protected TextView mCurrentDate;
    protected boolean isDirty = false;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isDirty = true;
        }
    };
    private String mImageFilePath;
    private View.OnTouchListener dateOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                showDatePickerDialog(view);
            }
            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
//                    try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    mImageFilePath = ImageWrapper.getRealPathFromURI(this, imageUri);
                    ImageWrapper.loadImageFromFile(this, mMainImage, mImageFilePath, 400);
                    mTripbookItem.setThumbnail(mImageFilePath);
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        mMainImage.setImageBitmap(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mTripName = (EditText) findViewById(R.id.trip_add_name);
        mTripName.addTextChangedListener(textWatcher);
        mTripName.setHint(R.string.hint_trip_name);

        dateFormat = DateFormat.getMediumDateFormat(this);
        mStartDatePicker = (TextView) findViewById(R.id.trip_add_start);
        mStartDatePicker.setText(dateFormat.format(new Date()));
        mStartDatePicker.setOnTouchListener(dateOnTouchListener);

        mEndDatePicker = (TextView) findViewById(R.id.trip_add_end);
        mEndDatePicker.setText(dateFormat.format(new Date()));
        mEndDatePicker.setOnTouchListener(dateOnTouchListener);

        mNotes = (EditText) findViewById(R.id.trip_add_notes);
        mNotes.addTextChangedListener(textWatcher);

        mCurrentDate = mStartDatePicker;
        mMainImage = (ImageView) findViewById(R.id.trip_add_main_image);
        mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // check if its an edit to load the existing data
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("itemKey")) {
                long itemKey = getIntent().getExtras().getLong("itemKey");
                mTripbookItem = new TripBookItemData(this).getItem(itemKey);
                mTripName.setText(mTripbookItem.getTitle());
                mStartDatePicker.setText(mTripbookItem.getCreatedAt());
                mEndDatePicker.setText(mTripbookItem.getEndDate());
                mNotes.setText(mTripbookItem.getDescription());
                ImageWrapper.loadImageFromFile(this, mMainImage, mTripbookItem.getThumbnail(), 400);
            }
            if (getIntent().getExtras().containsKey("itemType")) {
                mItemType = getIntent().getExtras().getString("itemType");
            } else {
                mItemType = TripBookItem.TYPE_TRIP;
            }
        }


        selectedItems = new ArrayList<>();
        fragmentFriends = new HorizontalListFragment();
        fragmentPlace = new HorizontalListFragment();
        fragmentImages = new HorizontalListFragment();
        if (mItemType.equals(TripBookItem.TYPE_TRIP)) {
            replaceListFragment(fragmentFriends, R.id.trip_view_friends, TripBookItem.TYPE_FRIENDS);
            replaceListFragment(fragmentPlace, R.id.trip_view_places, TripBookItem.TYPE_PLACE);
            replaceListFragment(fragmentImages, R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        } else if (mItemType.equals(TripBookItem.TYPE_PLACE)) {
            replaceListFragment(fragmentPlace, R.id.trip_view_places, TripBookItem.TYPE_TRIP);
            replaceListFragment(fragmentFriends, R.id.trip_view_friends, TripBookItem.TYPE_FRIENDS);
            replaceListFragment(fragmentImages, R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        } else if (mItemType.equals(TripBookItem.TYPE_FRIENDS)) {
            replaceListFragment(fragmentPlace, R.id.trip_view_places, TripBookItem.TYPE_TRIP);
            replaceListFragment(fragmentFriends, R.id.trip_view_friends, TripBookItem.TYPE_PLACE);
            replaceListFragment(fragmentImages, R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        }

        isDirty = false;
    }

    private void replaceListFragment(HorizontalListFragment fragment, int horizontalList, String itemType) {
        Bundle listArgs = new Bundle();
        if (mTripbookItem != null)
            listArgs.putLong("itemId", mTripbookItem.getId());
        listArgs.putString("itemType", itemType);
        listArgs.putBoolean("editable", true);
        fragment.setArguments(listArgs);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(horizontalList, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_edit, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mActionBarView = getLayoutInflater().inflate(R.layout.edit_item_custom_actiobar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        String title = getResources().getString(R.string.add_item_title);
        if (mTripbookItem != null)
            title = "Edit " + mTripbookItem.getTitle();
        else
            title = title + " " + mItemType;

        ((TextView) mActionBarView.findViewById(R.id.text_title)).setText(title);
//        actionBar.setTitle(title);
        return true;
    }

    public void cancelAction(View view) {
        if (isDirty)
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_cancel_save_title)
                    .setMessage(R.string.dialog_cancel_save_message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        else
            finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_cancel) {
//            cancelAction(null);
//        }

        if (item.getItemId() == R.id.action_item_save) {
            if (validate()) {
                saveItem();
                showToast("Trip saved");
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        TripBookItem tripBookItem;
        if (mTripbookItem == null)
            tripBookItem = new TripBookItem(mTripName.getText().toString(), mItemType);
        else
            tripBookItem = mTripbookItem;

        tripBookItem.setCreatedAt(mStartDatePicker.getText().toString());
        tripBookItem.setEndDate(mEndDatePicker.getText().toString());
        tripBookItem.setDescription(mNotes.getText().toString());

        if (mTripbookItem == null) {
            tripBookItem = new TripBookItemData(this).add(tripBookItem);
        } else {
            tripBookItem.setLinks(new ArrayList<TripBookCommon>());
        }
        for (TripBookCommon item : fragmentFriends.mAdapter.getSelectedItems()) {
            tripBookItem.addLink(item);
        }
        for (TripBookCommon item : fragmentPlace.mAdapter.getSelectedItems()) {
            tripBookItem.addLink(item);
        }
        for (TripBookCommon item : fragmentImages.mAdapter.getSelectedItems()) {
            tripBookItem.addLink(item);
        }
        tripBookItem.update(this);
    }

    private boolean validate() {
        if (mTripName.getText().toString().length() == 0) {
            String message = getResources().getString(R.string.error_field_required);
            mTripName.setError(message, getResources().getDrawable(R.drawable.ic_action_error));
            mTripName.requestFocus();
            return false;
        }
        if (mStartDatePicker.getText().toString().length() == 0) {
            String message = getResources().getString(R.string.error_field_required);
            mStartDatePicker.setError(message, getResources().getDrawable(R.drawable.ic_action_error));
            mStartDatePicker.requestFocus();
            return false;
        }
        if (mItemType.equals(TripBookItem.TYPE_TRIP) && mEndDatePicker.getText().toString().length() == 0) {
            String message = getResources().getString(R.string.error_field_required);
            mEndDatePicker.setError(message, getResources().getDrawable(R.drawable.ic_action_error));
            mEndDatePicker.requestFocus();
            return false;
        }
        long startDate = Date.parse(mStartDatePicker.getText().toString());
        if (mEndDatePicker.getText().toString().length() > 0) {
            long endDate = Date.parse(mEndDatePicker.getText().toString());
            if (endDate < startDate) {
                String message = getResources().getString(R.string.enddate_before_startdate);
                mEndDatePicker.setError(message, getResources().getDrawable(R.drawable.ic_action_error));
                mEndDatePicker.requestFocus();
                return false;
            }
        }
        return true;
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
        mCurrentDate = (TextView) v;
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
            ((AddItemActivity)getActivity()).mCurrentDate.setText(dateFormat.format(new Date(year, month, day)));
            ((AddItemActivity)getActivity()).isDirty = true;
            //mCurrentDate.setText(day + "/" + month + "/" + year);
        }
    }

}
