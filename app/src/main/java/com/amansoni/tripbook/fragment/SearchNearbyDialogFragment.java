package com.amansoni.tripbook.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.amansoni.tripbook.activity.MapsActivity;
import com.amansoni.tripbook.R;

/**
 * Created by Aman on 01/03/2015.
 */
public class SearchNearbyDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.search_nearby_title)
                .setItems(R.array.search_nearby_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }
                        MapsActivity callingActivity = (MapsActivity) getActivity();
                        callingActivity.onNearBySelect(which);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getResources().getText(android.R.string.cancel).toString(), null);
        return builder.create();
    }

}
