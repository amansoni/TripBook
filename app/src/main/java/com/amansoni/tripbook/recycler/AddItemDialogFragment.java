package com.amansoni.tripbook.recycler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.amansoni.tripbook.AddActivity;
import com.amansoni.tripbook.ItemEditFragment;
import com.amansoni.tripbook.LocationLookup;
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
                        Bundle args = new Bundle();
                        ItemEditFragment itemEditFragment = new ItemEditFragment();
                        switch (which){
                            case 0: // Friend
                                args.putInt("itemType", R.string.title_social);
                                itemEditFragment.setArguments(args);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, itemEditFragment)
                                        .commit();

                                break;
                            case 1: // Trip
                                Intent intent = new Intent(getActivity(), AddActivity.class);
                                startActivity(intent);
                                break;
                            case 2: // Place
                                args.putInt("itemType", R.string.title_place);
                                itemEditFragment.setArguments(args);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, itemEditFragment)
                                        .commit();

                                break;
                            case 3: // Photo
                                args.putInt("itemType", R.string.title_gallery);
                                itemEditFragment.setArguments(args);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, itemEditFragment)
                                        .commit();

                                break;
                        }
                    }
                });
        return builder.create();
    }
}
