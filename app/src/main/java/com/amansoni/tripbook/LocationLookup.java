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
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TbGeolocation;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.util.ImageWrapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class LocationLookup extends ActionBarActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    public static final String IMAGE_URI = "image-file-path";
    protected static final String TAG = "LocationLookup";
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    protected static EditText mDatePicker;
    protected static EditText mTimePicker;
    //     * Provides the entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;
    //     * Represents a geographical location.
    protected Location mLastLocation;
    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     * The user requests an address by pressing the Fetch Address button. This may happen
     * before GoogleApiClient connects. This activity uses this boolean to keep track of the
     * user's intent. If the value is true, the activity tries to fetch the address as soon as
     * GoogleApiClient connects.
     */
    protected boolean mAddressRequested;
    //     * The formatted location address.
    protected String mAddressOutput;
    //     * Displays the location address.
    protected EditText mLocationAddressTextView;
    //     * Visible while the address is being fetched.
    ProgressBar mProgressBar;
    //     * The image view to display the image
    ImageView mImageView;
    //     * Receiver registered with this activity to get the response from FetchAddressIntentService.
    private AddressResultReceiver mResultReceiver;
    //     * Image file path string
    private String mImageFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_lookup);

        mResultReceiver = new AddressResultReceiver(new Handler());

        mLocationAddressTextView = (EditText) findViewById(R.id.location_address_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mImageView = (ImageView) findViewById(R.id.list_row_imageView);
        mDatePicker = (EditText) findViewById(R.id.dateText);
        mTimePicker = (EditText) findViewById(R.id.timeText);
//        mFetchAddressButton = (Button) findViewById(R.id.fetch_address_button);

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = true;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);
        mImageFilePath = getIntent().getExtras().getString(IMAGE_URI);
        showImage();
        updateUIWidgets();
        buildGoogleApiClient();
//        setHasOptionsMenu(true);

        // We only start the service to fetch the address if GoogleApiClient is connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_edit, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Add new photo");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
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
            return true;
        }

        if (item.getItemId() == R.id.action_item_save) {
            saveItem();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {

        TripBookItemData tripBookItemData = new TripBookItemData(this);
        // save the image
        TripBookItem tripBookItem = new TripBookItem(mLocationAddressTextView.getText().toString(), TripBookItem.TYPE_PLACE);
        //TODO add location to data object
        tripBookItem.setLocation(new TbGeolocation(mLastLocation.getLongitude(), mLastLocation.getLatitude()));
        tripBookItem = tripBookItemData.add(tripBookItem);

        //TODO add image(s)
//        TripBookImage tripBookImage = new TripBookImage(mImageFilePath);
//        if (tripBookImage != null)
//            tripBookItem.addImage(tripBookImage);

        new TripBookItemData(this).add(tripBookItem);

    }


    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }

            if (savedInstanceState.keySet().contains(IMAGE_URI)) {
//                mImageFilePath = savedInstanceState.getString(IMAGE_URI);
                showImage();
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
            // It is possible that the user presses the button to get the address before the
            // GoogleApiClient object successfully connects. In such a case, mAddressRequested
            // is set to true, but no attempt is made to fetch the address (see
            // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
            // user has requested an address, since we now have a connection to GoogleApiClient.
            if (mAddressRequested) {
                startIntentService();
            }
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }

    /**
     * Updates the address in the UI.
     */
    protected void showImage() {
        ImageWrapper.loadImageFromFile(this, mImageView, mImageFilePath, mImageView.getHeight());
//        File imgFile = new File(mImageFilePath);
//        Log.d(TAG, mImageFilePath);
//
//        if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            mImageView.setImageBitmap(myBitmap);
//        }
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    /**
     * Shows a toast with the given text.
     */
    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
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
            mTimePicker.setText(hourOfDay + ":" + minute);
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
            mDatePicker.setText(day + "/" + month + "/" + year);
        }
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
}
