package com.amansoni.tripbook.recycler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.amansoni.tripbook.AddActivity;
import com.amansoni.tripbook.ItemEditFragment;
import com.amansoni.tripbook.LocationLookup;
import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aman on 01/03/2015.
 */
public class AddItemDialogFragment extends DialogFragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_item_title)
                .setItems(R.array.additems_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle args = new Bundle();
                        switch (which){
                            case 0: // Friend
                                Intent friendIntent = new Intent(getActivity(), AddActivity.class);
                                args.putString("itemType", TripBookItem.TYPE_FRIENDS);
                                friendIntent.putExtras(args);
                                startActivity(friendIntent);
                                break;
                            case 1: // Trip
                                Intent tripIntent = new Intent(getActivity(), AddActivity.class);
                                args.putString("itemType", TripBookItem.TYPE_TRIP);
                                tripIntent.putExtras(args);
                                startActivity(tripIntent);
                                break;
                            case 2: // Place
                                Intent placeIntent = new Intent(getActivity(), AddActivity.class);
                                args.putString("itemType", TripBookItem.TYPE_PLACE);
                                placeIntent.putExtras(args);
                                startActivity(placeIntent);
                                break;
                            case 3: // Photo
                                // create Intent to take a picture and return control to the calling application
                                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                                // start the image capture Intent
                                startActivityForResult(photoIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                                break;
                        }
                    }
                });
        return builder.create();
    }
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TripBook");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("TripBook", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                Intent intent = new Intent(getActivity(), LocationLookup.class);
                intent.putExtra(LocationLookup.IMAGE_URI, fileUri.getPath());
                startActivity(intent);

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(getActivity(), "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }
}
