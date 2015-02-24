package com.amansoni.tripbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookImage;
import com.amansoni.tripbook.model.TripBookItem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 13/02/2015.
 */
public class ItemEditFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ItemViewFragment";
    private static final int SELECT_PICTURE = 1;
    private TripBookItem tripBookItem;
    TripBookImage tripBookImage;
    private Uri selectedImageUri;
    private String selectedImagePath;
    private EditText imageTitle;
    private EditText imageDescription;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tripBookItem = new TripBookItemData().getItem(args.getLong("itemKey"));
            Log.d(TAG, "Got tripBookItem:" + tripBookItem.toString());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_edit, container, false);
        view.setTag(TAG);
        imageView = (ImageView) view.findViewById(R.id.item_image);
        if (imageView != null) {
            imageView.setOnClickListener(this);
            Log.d(TAG, "setOnClickListener itemEdit.itemImage");
        }
        Bundle args = getArguments();
        if (args == null) {
            Log.d(TAG, "No itemkey passe in bundle args");
        }
        tripBookItem = new TripBookItemData().getItem(args.getLong("itemKey"));
        // TextView:title
        imageTitle = ((EditText) view.findViewById(R.id.item_title));
        imageTitle.setText(tripBookItem.getTitle());
        // TextView:description
        imageDescription = ((EditText) view.findViewById(R.id.item_createdAt));
        imageDescription.setText(tripBookItem.getCreatedAt());
        // ImageView:description
//        if (tripBookItem.getImage() != null && tripBookItem.getImage().length() > 0) {
//            ((ImageView) view.findViewById(R.id.item_image)).setImageURI(Uri.parse(tripBookItem.getImage()));
//            selectedImagePath = tripBookItem.getImage();
////        } else {
////            ((ImageView) view.findViewById(R.id.item_image)).setImageURI(Uri.parse(item.getImage()));
//        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            saveItem(false);
            return true;
        }

        if (item.getItemId() == R.id.action_item_save) {
            saveItem(true);
            Toast.makeText(getActivity(), "Save", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem(boolean updateValues) {
        if (updateValues) {
            tripBookItem.setTitle(imageTitle.getText().toString());
            tripBookItem.addImage(tripBookImage);
            new TripBookItemData().update(tripBookItem);
        }
        Bundle args = new Bundle();
        args.putLong("itemKey", tripBookItem.getId());
        ItemViewFragment itemViewFragment = new ItemViewFragment();
        itemViewFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, itemViewFragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                Log.d(TAG, "Set ImageView:" + selectedImageUri);

                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                tripBookImage = new TripBookImage();
                tripBookImage.setImage(stream.toByteArray());
                tripBookImage.setFilePath(selectedImageUri.getEncodedPath());
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
