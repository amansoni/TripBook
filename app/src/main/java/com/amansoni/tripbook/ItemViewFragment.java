package com.amansoni.tripbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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

import com.amansoni.tripbook.db.TripBookImageData;
import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookImage;
import com.amansoni.tripbook.model.TripBookItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Aman on 13/02/2015.
 */
public class ItemViewFragment extends Fragment {
    private static final String TAG = "ItemViewFragment";
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private TripBookItem tripBookItem;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tripBookItem = new TripBookItemData(getActivity()).getItem(args.getLong("itemKey"));
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
        // TextView:title
        ((TextView) view.findViewById(R.id.item_title)).setText(tripBookItem.getTitle());
        // TextView:description
        ((TextView) view.findViewById(R.id.item_createdAt)).setText(tripBookItem.getCreatedAt());
        // ImageView:description
        mImageView = ((ImageView) view.findViewById(R.id.item_image));
//        TripBookImage tripBookImage = new TripBookImageData(getActivity()).getItem(3);
//        mImageView.setImageBitmap(tripBookImage.getThumbnail());
//        if (tripBookItem.getImage() != null && tripBookItem.getImage().length() > 0) {
//            mImageView = ((ImageView) view.findViewById(R.id.item_image));
//            mImageView.setImageURI(Uri.parse(tripBookItem.getImage()));
//            mImageView.setImageBitmap(Utils.decodeSampledBitmap(Uri.parse(tripBookItem.getImage()).getPath(), 80, 80));
//            Bitmap bitmap = BitmapFactory.decodeFile(tripBookItem.getImage());
//            if (bitmap == null){
//                Log.d(TAG, "Bitmap is null from " + tripBookItem.getImage());
//            }
//            mImageView.setImageBitmap(Utils.getResizedBitmap(bitmap, 80, 80));
//        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_item_edit) {
            Toast.makeText(getActivity(), "Edit Item", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Setting bundle:" + item.toString());
            Bundle args = new Bundle();
            args.putLong("itemKey", tripBookItem.getId());
            ItemEditFragment itemEditFragment = new ItemEditFragment();
            itemEditFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, itemEditFragment)
                    .addToBackStack("view")
                    .commit();

            return true;
        }

        if (item.getItemId() == R.id.action_item_share) {
            createShareIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tripBookItem.getTitle());
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, tripBookItem.getCreatedAt());
        Uri uri = getLocalBitmapUri(mImageView);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
        return shareIntent;
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
