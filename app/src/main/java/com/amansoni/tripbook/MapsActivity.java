package com.amansoni.tripbook;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends ActionBarActivity {
    protected static final String TAG = "MapsActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button searchButton = (Button) findViewById(R.id.map_search_nearby);
        Drawable icon = this.getResources().getDrawable(R.drawable.ic_action_search);
        searchButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        Button selectButton = (Button) findViewById(R.id.map_select_places);
        Drawable settings = this.getResources().getDrawable(R.drawable.ic_action_settings);
        selectButton.setCompoundDrawablesWithIntrinsicBounds(settings, null, null, null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMapIfNeeded();
        showMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SUCCESS:
                MapsInitializer.initialize(this);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setMyLocationEnabled(true);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(52.487269, -1.890457), 10);
                mMap.animateCamera(cameraUpdate);
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(this, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(this, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, GooglePlayServicesUtil.isGooglePlayServicesAvailable(this), Toast.LENGTH_SHORT).show();
        }
        showMarkers();
    }

    private void showMarkers() {
        TripBookItemData data = new TripBookItemData(TripBookItem.TYPE_PLACE);

        for (TripBookCommon common : data.getAllRows()) {
            TripBookItem place = (TripBookItem) common;
            if (place.getLocation() != null) {
                if (place.getLocation().getLatitude() != 0 && place.getLocation().getLongitude() != 0) {
                    LatLng position = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(place.getTitle()))
                            .setSnippet(place.toString());
                    Log.d(TAG, "Added location for " + place.getTitle() + " " + place.getLocation().toString());
                }
            } else {
                Log.d(TAG, "No location for " + place.getTitle());
            }
        }
    }
}
