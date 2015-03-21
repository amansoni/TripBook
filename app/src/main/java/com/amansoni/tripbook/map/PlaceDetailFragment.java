package com.amansoni.tripbook.map;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amansoni.tripbook.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by beaumoaj on 11/02/15.
 */
public class PlaceDetailFragment extends Fragment {
    private static String TAG = "PlaceDetailFragment";
    private PlaceDetail placeDetail;
    private GooglePlace place;

    public PlaceDetailFragment() {
        super();
        //contact = null;
    }

    /**
     * Factory method to generate a fragment with arguments.  Cannot do this as a
     * normal constructor because it interferes with the way Fragments are constructed
     * and restarted in life cycle events.
     *
     * @param place the google place we want the full details of
     * @return a new PlaceDetailFragment
     */
    public static PlaceDetailFragment newInstance(GooglePlace place) {
        Bundle args = new Bundle();
        args.putSerializable("PLACE", place);
        PlaceDetailFragment fragment = new PlaceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //rssItem = new RssItem();
        //int position = getActivity().getIntent().getIntExtra("POSITION", 0);
        place = (GooglePlace) getArguments().getSerializable("PLACE");
        this.setHasOptionsMenu(true);
        getActivity().setTitle(place.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // we haven't got the data to fill in yet
        return inflater.inflate(R.layout.place_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        String placesKey = getActivity().getResources().getString(R.string.google_maps_key);
        ;
        String placesRequest = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "key=" + placesKey + "&reference=" + place.getReference();
        PlacesDetailReadFeed detailTask = new PlacesDetailReadFeed();
        detailTask.execute(placesRequest);
    }

    private void fillInLayout(GooglePlace place) {
        // title element has name and types
        TextView title = (TextView) getView().findViewById(R.id.name);
        title.setText(place.getName());
        Log.i(TAG, "Setting title to: " + title.getText());
        //address
        TextView address = (TextView) getView().findViewById(R.id.address);
        address.setText(place.getFormatted_address() + " " + place.getFormatted_phone_number());
        Log.i(TAG, "Setting address to: " + address.getText());
        //vicinity
        TextView vicinity = (TextView) getView().findViewById(R.id.vicinity);
        vicinity.setText(place.getVicinity());
        Log.i(TAG, "Setting vicinity to: " + vicinity.getText());
        //rating
        TextView reviews = (TextView) getView().findViewById(R.id.reviews);

        List<GooglePlace.Review> reviewsData = place.getReviews();
        if (reviewsData != null) {
            StringBuffer sb = new StringBuffer();
            for (GooglePlace.Review r : reviewsData) {
                sb.append(r.getAuthor_name());
                sb.append(" says \"");
                sb.append(r.getText());
                sb.append("\" and rated it ");
                sb.append(r.getRating());
                sb.append("\n");
            }
            reviews.setText("Reviews:\n" + sb.toString());
        } else {
            reviews.setText("There have not been any reviews!");
        }
        Log.i(TAG, "Setting rating to: " + reviews.getText());
    }

    private class PlacesDetailReadFeed extends AsyncTask<String, Void, PlaceDetail> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected PlaceDetail doInBackground(String... urls) {
            try {
                //dialog.setMessage("Fetching Places Data");
                String input = GooglePlacesUtility.readGooglePlaces(urls[0]);
                Gson gson = new Gson();
                PlaceDetail place = gson.fromJson(input, PlaceDetail.class);
                Log.i(TAG, "Place found is " + place.toString());
                return place;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting place details...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(PlaceDetail placeDetail) {
            place = placeDetail.getResult();
            fillInLayout(place);
            this.dialog.dismiss();
        }
    }


}
