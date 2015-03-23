package com.amansoni.tripbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amansoni.tripbook.activity.AddItemActivity;
import com.amansoni.tripbook.fragment.HorizontalListFragment;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.util.ImageWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Aman on 13/02/2015.
 */
public class ItemViewFragment extends Fragment {
    private static final String TAG = "ItemViewFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private TextView tripName;
    TextView tripNotes;
    TextView tripStart;
    TextView tripEnd;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private TripBookItem tripBookItem;
    private boolean mEditable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tripBookItem = new TripBookItemData(getActivity()).getItem(args.getLong("itemKey"));
            if (args.containsKey("editable"))
                mEditable = args.getBoolean("editable");
            Log.d(TAG, tripBookItem.toString());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_view, container, false);
        view.setTag(TAG);
        Bundle args = getArguments();
        if (args == null) {
            Log.d(TAG, "No itemKey passed in bundle args");
        }
        tripBookItem = new TripBookItemData(getActivity()).getItem(args.getLong("itemKey"));

        tripName = ((TextView) view.findViewById(R.id.trip_add_name));
        tripName.setText(tripBookItem.getTitle());

        tripStart = ((TextView) view.findViewById(R.id.trip_add_start));
        tripStart.setText(tripBookItem.getCreatedAt());

        tripEnd = ((TextView) view.findViewById(R.id.trip_add_start));
        tripEnd.setText(tripBookItem.getCreatedAt());

        ((TextView) view.findViewById(R.id.trip_add_end)).setText(tripBookItem.getEndDate());

        tripNotes = ((TextView) view.findViewById(R.id.trip_add_notes));
        tripNotes.setText(tripBookItem.getDescription());

        if (true) {
            tripName.setFocusable(false);
            tripName.setClickable(false);
            tripNotes.setFocusable(false);
            tripNotes.setClickable(false);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.item_main_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        ImageWrapper.loadImageFromFile(this, imageView, tripBookItem.getPhoto().getFilename(), 400);
        ImageWrapper.loadImageFromFile(this, imageView, tripBookItem.getThumbnail(), 400);

//        ImageWrapper.loadImage(this, imageView, tripBookItem.getPhoto().getFilename());

        if (tripBookItem.getItemType().equals(TripBookItem.TYPE_TRIP)) {
            replaceListFragment(R.id.trip_view_friends, TripBookItem.TYPE_FRIENDS);
            replaceListFragment(R.id.trip_view_places, TripBookItem.TYPE_PLACE);
            replaceListFragment(R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        } else if (tripBookItem.getItemType().equals(TripBookItem.TYPE_PLACE)) {
            replaceListFragment(R.id.trip_view_places, TripBookItem.TYPE_TRIP);
            replaceListFragment(R.id.trip_view_friends, TripBookItem.TYPE_FRIENDS);
            replaceListFragment(R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        } else if (tripBookItem.getItemType().equals(TripBookItem.TYPE_FRIENDS)) {
            replaceListFragment(R.id.trip_view_places, TripBookItem.TYPE_TRIP);
            replaceListFragment(R.id.trip_view_friends, TripBookItem.TYPE_PLACE);
            replaceListFragment(R.id.trip_view_gallery, TripBookItem.TYPE_GALLERY);
        }

        return view;
    }

    private void replaceListFragment(int horizontalList, String itemType) {
        Fragment horizontalListFragment = new HorizontalListFragment();
        Bundle listArgs = new Bundle();
        listArgs.putLong("itemId", tripBookItem.getId());
        listArgs.putString("itemType", itemType);
        listArgs.putBoolean("editable", false);
        horizontalListFragment.setArguments(listArgs);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(horizontalList, horizontalListFragment)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mEditable) {
            inflater.inflate(R.menu.item_edit, menu);
        } else {
            inflater.inflate(R.menu.item_view, menu);
        }
//        if (menu.getItem(R.id.menu_search)!=null){
//            menu.getItem(R.id.menu_search).setVisible(false);
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_item_edit) {
//            Toast.makeText(getActivity(), "Edit Item", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Setting bundle for itemEditFragment:" + item.toString());
            Intent editIntent = new Intent(getActivity(), AddItemActivity.class);
            Bundle args = new Bundle();
            args.putString("itemType", tripBookItem.getItemType());
            args.putLong("itemKey", tripBookItem.getId());
            args.putBoolean("editable", true);
            editIntent.putExtras(args);
            startActivity(editIntent);

            return true;
        }

        if (item.getItemId() == R.id.action_item_share) {
            createShareIntent();
            return true;
        }
//        if (item.getItemId() == R.id.action_cancel) {
//            getActivity().getSupportFragmentManager().popBackStack();
//            return true;
//        }

        if (item.getItemId() == R.id.action_item_save) {
            saveItem();
            Toast.makeText(getActivity(), tripBookItem.getTitle() + " saved", Toast.LENGTH_SHORT).show();
            Bundle args = new Bundle();
            args.putLong("itemKey", tripBookItem.getId());
            ItemViewFragment itemViewFragment = new ItemViewFragment();
            itemViewFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, itemViewFragment)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        tripBookItem.setTitle(tripName.getText().toString());
        tripBookItem.setCreatedAt(tripStart.getText().toString());
        tripBookItem.setEndDate(tripEnd.getText().toString());
        tripBookItem.setDescription(tripNotes.getText().toString());
        //tripBookItem.setThumbnail(((BitmapDrawable)imageView.getDrawable()).getBitmap());
//        TODO
//        if (tripBookImage != null)
//            tripBookItem.addImage(tripBookImage);
        tripBookItem.update(getActivity());
    }

    /**
     * Create a sharable intent using the item being viewed.
     *
     * @return the shareIntent
     */
    protected Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("*/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tripBookItem.getItemType() + " " + tripBookItem.getTitle());
        String extraText = "From:" + tripBookItem.getCreatedAt() + "\nTo:" + tripBookItem.getEndDate() + "\n" + tripBookItem.getDescription();
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, extraText);

//TODO share appropriate image data for the item
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (String filepath : tripBookItem.getImages()){
            imageUris.add(Uri.parse(filepath )); // Add your image URIs here
        }
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        startActivity(Intent.createChooser(shareIntent, "Share using..."));
        return shareIntent;
    }
}
