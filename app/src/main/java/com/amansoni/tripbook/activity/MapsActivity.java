package com.amansoni.tripbook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.fragment.SearchNearbyDialogFragment;
import com.amansoni.tripbook.fragment.ShowPlaceFilterDialogFragment;
import com.amansoni.tripbook.map.GooglePlace;
import com.amansoni.tripbook.map.GooglePlaceList;
import com.amansoni.tripbook.map.GooglePlacesUtility;
import com.amansoni.tripbook.map.PlaceDetailActivity;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookItemData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {
    protected static final String TAG = "MapsActivity";
    protected static int defaultFilter = 4;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<Marker, GooglePlace> nearby;
    private HashMap<Marker, TripBookItem> tripPlace;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMapIfNeeded();
        showMarkers(defaultFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_search:
                new SearchNearbyDialogFragment().show(getSupportFragmentManager(), "search_nearby");
                return true;
            case R.id.action_map_places:
                new ShowPlaceFilterDialogFragment().show(getSupportFragmentManager(), "show_place_filter");
                return true;
            default:
                return false;
        }
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
//                mMap.setOnMarkerClickListener(this);
                mMap.setOnInfoWindowClickListener(this);
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
        showMarkers(defaultFilter);
    }

    private void showMarkers(int filter) {
        mMap.clear(); // clear all existing markers
        TripBookItemData data = new TripBookItemData(this, TripBookItem.TYPE_PLACE);
        tripPlace = new HashMap<>();

        for (TripBookCommon common : data.getAllRows()) {
            TripBookItem place = (TripBookItem) common;
            if (place.getLocation() != null) {
                if (place.getLocation().getLatitude() != 0 && place.getLocation().getLongitude() != 0) {
                    if (filter == defaultFilter) {
                        LatLng position = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(position));

                        marker.setTitle(place.getTitle());
                        marker.setSnippet(place.toString());
                        tripPlace.put(marker, place);

                        Log.d(TAG, "Added location for " + place.getTitle() + " " + place.getLocation().toString());
                    }
                }
            } else {
                Log.d(TAG, "No location for " + place.getTitle());
            }
        }
    }

    private Marker createMarker(LatLng ll, String title, String description, float hue) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(hue)));
        marker.setTitle(title);
        marker.setSnippet(description);

        return marker;
    }


    public void onFilterPlaceSelected(int filter) {
        Log.d(TAG, "onFilterPlaceSelected" + filter);
        showMarkers(filter);
    }

    public void onNearBySelect(int filter) {
        onMarkerClick(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (nearby == null || nearby.size() == 0) {
            String placesKey = getResources().getString(R.string.google_places_key);
            double lat = marker.getPosition().latitude;
            double lng = marker.getPosition().longitude;
            String type = URLEncoder.encode("train_station|bus_station");
            String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    lat + "," + lng + "&radius=500&key=" + placesKey;
            PlacesReadFeed process = new PlacesReadFeed();
            process.execute(placesRequest);
        }
        //if (marker.equals(this.itemMarker) && nearby != null) {
        if (nearby != null) {
            for (Marker placeMarker : nearby.keySet()) {
                placeMarker.setVisible(!placeMarker.isVisible());
            }
        } else if (nearby != null && nearby.containsKey(marker)) {
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            intent.putExtra("PLACE", nearby.get(marker));
            startActivity(intent);
            return true;
        } else if (tripPlace != null && tripPlace.containsKey(marker)) {
            String placesKey = getResources().getString(R.string.google_places_key);
            double lng = marker.getPosition().latitude;
            double lat = marker.getPosition().longitude;

            String type = URLEncoder.encode("train_station|bus_station");
            String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    lat + "," + lng + "&radius=500&key=" + placesKey;
            PlacesReadFeed process = new PlacesReadFeed();
            process.execute(placesRequest);
        } else {
            Log.i(TAG, "Not the item marker so not fetching places");
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (tripPlace != null && tripPlace.containsKey(marker)) {
            // is the a place that has already been added to places
            TripBookItem tripBookItem = tripPlace.get(marker);
            Intent i = new Intent(this, ItemPagerActivity.class);
            i.putExtra(TripBookItem.ITEM_ID, tripBookItem.getId());
            i.putExtra(TripBookItem.ITEM_TYPE, tripBookItem.getItemType());
            startActivity(i);
        } else if (nearby != null && nearby.containsKey(marker)) {
            // is this a Google Place Marker only
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            intent.putExtra("PLACE", nearby.get(marker));
            startActivity(intent);
        }
    }

    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
        private final ProgressDialog dialog = new ProgressDialog(MapsActivity.this);

        @Override
        protected GooglePlaceList doInBackground(String... urls) {
            try {
                //dialog.setMessage("Fetching Places Data");
                String input = GooglePlacesUtility.readGooglePlaces(urls[0]);
                Gson gson = new Gson();
                GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
                Log.d(TAG, "Number of places found is " + places.getResults().size());
                return places;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting nearby places...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(GooglePlaceList places) {
            nearby = new HashMap<>();
            for (GooglePlace place : places.getResults()) {
                String name = place.getName();
                List<String> types = place.getTypes();
                Log.i(TAG, "Found a place called: " + name);
                GooglePlace.Geometry geometry = place.getGeometry();
                if (geometry != null) {
                    GooglePlace.Geometry.Location location = geometry.getLocation();
                    if (location != null) {
                        nearby.put(createMarker(new LatLng(location.getLat(), location.getLng()),
                                        types.toString(), name, BitmapDescriptorFactory.HUE_BLUE),
                                place);
                    }
                }
            }
            this.dialog.dismiss();
        }


    }
}
