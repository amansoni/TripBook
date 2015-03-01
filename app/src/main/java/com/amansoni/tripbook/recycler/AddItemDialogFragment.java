package com.amansoni.tripbook.recycler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.amansoni.tripbook.R;

/**
 * Created by Aman on 01/03/2015.
 */
public class AddItemDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_item_title)
                .setItems(R.array.additems_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "select = " + which, Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
