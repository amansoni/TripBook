package com.amansoni.tripbook.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amansoni.tripbook.activity.AddItemActivity;
import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.provider.Images;
import com.amansoni.tripbook.util.FloatingActionButton;
import com.amansoni.tripbook.util.ImageWrapper;
import com.amansoni.tripbook.util.Photo;

import java.util.ArrayList;
import java.util.Date;

public class ItemViewFragment extends BaseFragment {
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;

    TripBookItem mTripBookItem;
    TextView mTitleField;
    TextView mStartDateField;
    TextView mEndDateField;
    TextView mNotesField;
    ImageView mPhotoView;
    RecyclerView mListPlaces;
    RecyclerView mListFriends;
    RecyclerView mListImages;


    public static ItemViewFragment newInstance(long tripBookItemId) {
        Bundle args = new Bundle();
        args.putLong(TripBookItem.ITEM_ID, tripBookItemId);

        ItemViewFragment fragment = new ItemViewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        long itemId = getArguments().getLong(TripBookItem.ITEM_ID);
        mTripBookItem = new TripBookItemData(getActivity()).getItem(itemId);

        setHasOptionsMenu(true);
    }

    public void updateDate() {
        mStartDateField.setText(mTripBookItem.getCreatedAt().toString());
    }

    public void closeFragment() {
        getActivity().finish();
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_view, parent, false);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton exitButton = (ImageButton) v.findViewById(R.id.item_view_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        FloatingActionButton editButton = (FloatingActionButton) v.findViewById(R.id.item_view_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(getActivity(), AddItemActivity.class);
                Bundle args = new Bundle();
                args.putString("itemType", mTripBookItem.getItemType());
                args.putLong("itemKey", mTripBookItem.getId());
                args.putBoolean("editable", true);
                editIntent.putExtras(args);
                startActivity(editIntent);

                closeFragment();
            }
        });

        ImageView mPhotoView;
        mTitleField = (TextView) v.findViewById(R.id.item_view_title);
        mTitleField.setText(mTripBookItem.getTitle());
        mTitleField.setGravity(Gravity.CENTER);

        mStartDateField = (TextView) v.findViewById(R.id.item_view_start_date);
        mStartDateField.setText("Starts on: " + mTripBookItem.getCreatedAt());

        mEndDateField = (TextView) v.findViewById(R.id.item_view_end_date);
        if (mTripBookItem.getEndDate().length() > 0) {
            mEndDateField.setText("Ends on: " + mTripBookItem.getEndDate());
        } else {
            mEndDateField.setVisibility(View.GONE);
        }

        mNotesField = (TextView) v.findViewById(R.id.item_view_notes);
        mNotesField.setText(mTripBookItem.getDescription());

        mListImages = (RecyclerView) v.findViewById(R.id.item_view_list_images);
//TODO        setUpRecyclerView(mListImages, TripBookItem.TYPE_GALLERY);
        setUpRecyclerView(mListImages, TripBookItem.TYPE_PLACE);

        mListFriends = (RecyclerView) v.findViewById(R.id.item_view_list_friends);
        setUpRecyclerView(mListFriends, TripBookItem.TYPE_FRIENDS);

        mListPlaces = (RecyclerView) v.findViewById(R.id.item_view_list_places);
        setUpRecyclerView(mListPlaces, TripBookItem.TYPE_PLACE);

//        mTitleField.addTextChangedListener(new TextWatcher() {
//            public void onTextChanged(CharSequence c, int start, int before, int count) {
//                mTripBookItem.setTitle(c.toString());
//            }
//
//            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
//                // this space intentionally left blank
//            }
//
//            public void afterTextChanged(Editable c) {
//                // this one too
//            }
//        });

//        mDateButton = (Button) v.findViewById(R.id.crime_date);
//        updateDate();
//        mDateButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FragmentManager fm = getActivity()
//                        .getFragmentManager();
//                DatePickerFragment dialog = DatePickerFragment
//                        .newInstance(mTripBookItem.getDate());
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                dialog.show(fm, DIALOG_DATE);
//            }
//        });

//        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
//        mPhotoButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // launch the camera activity
//                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
//                startActivityForResult(i, REQUEST_PHOTO);
//            }
//        });
//
//        // if camera is not available, disable camera functionality
//        PackageManager pm = getActivity().getPackageManager();
//        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
//                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
//            mPhotoButton.setEnabled(false);
//        }
//
//        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
//        mPhotoView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Photo p = mTripBookItem.getPhoto();
//                if (p == null)
//                    return;
//
//                FragmentManager fm = getActivity()
//                        .getFragmentManager();
//                String path = getActivity()
//                        .getFileStreamPath(p.getFilename()).getAbsolutePath();
//                ImageFragment.createInstance(path)
//                        .show(fm, DIALOG_IMAGE);
//            }
//        });
//
//        mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
//        mSuspectButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(i, REQUEST_CONTACT);
//            }
//        });
//        if (mTripBookItem.getSuspect() != null) {
//            mSuspectButton.setText(mTripBookItem.getSuspect());
//        }
//
//        Button reportButton = (Button) v.findViewById(R.id.crime_reportButton);
//        reportButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));
//                startActivity(i);
//            }
//        });

        return v;
    }

    private void setUpRecyclerView(RecyclerView recyclerView, String itemType) {
        TripBookItemData tripBookItemData = new TripBookItemData(getActivity(), itemType);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(0);

        LinkedItemListAdapter mAdapter = new LinkedItemListAdapter(getActivity(), tripBookItemData,
                mTripBookItem.getId(), false, R.color.list_text_selected, R.color.list_text_unselected);
        recyclerView.setAdapter(mAdapter);
    }

    //TODO
    private void showPhoto() {
        // (re)set the image button's image based on our photo
        Photo p = mTripBookItem.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
//            String path = getActivity()
//                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
//            b = PictureUtils.getScaledDrawable(getActivity(), p.getFilename());
        }
//        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStop() {
        super.onStop();
//TODO
//        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTripBookItem.setCreatedAt(date.toString());
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            // create a new Photo object and attach it to the crime
            String filename = data
                    .getStringExtra(TripBookCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo p = new Photo(filename);
//                TODO
                mTripBookItem.setPhoto(p);
                showPhoto();
            }
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            if (c.getCount() == 0) {
                c.close();
                return;
            }

            c.moveToFirst();
            String suspect = c.getString(0);
//            TODO
//            mTripBookItem.setSuspect(suspect);
//            mSuspectButton.setText(suspect);
            c.close();
        }
    }

//    private String getCrimeReport() {
//        String solvedString = null;
//        if (mTripBookItem.isSolved()) {
//            solvedString = getString(R.string.crime_report_solved);
//        } else {
//            solvedString = getString(R.string.crime_report_unsolved);
//        }
//
//        String dateFormat = "EEE, MMM dd";
//        String dateString = DateFormat.format(dateFormat, mTripBookItem.getDate()).toString();
//
//        String suspect = mTripBookItem.getSuspect();
//        if (suspect == null) {
//            suspect = getString(R.string.crime_report_no_suspect);
//        } else {
//            suspect = getString(R.string.crime_report_suspect, suspect);
//        }
//
//        String report = getString(R.string.crime_report, mTripBookItem.getTitle(), dateString, solvedString, suspect);
//
//        return report;
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LinkedItemListAdapter extends RecyclerView.Adapter<LinkedItemListAdapter.ListViewHolder> {
        private static final String TAG = "LinkedItemListAdapter";
        protected final FragmentActivity mActivity;
        protected boolean mEditable = false;
        protected int selectedColour = 0;
        protected int unSelectedColour = 0;
        ArrayList<TripBookItem> selectedItems;
        private int mPosition = -1;
        private long mItemId = 0;
        private ArrayList<TripBookItem> tripBookItems;

        /**
         * Initialize the dataset of the Adapter.
         */
        public LinkedItemListAdapter(FragmentActivity activity, TripBookItemData dataSet, long itemId, boolean editable, int list_text_selected, int list_text_unselected) {
            mActivity = activity;
            mItemId = itemId;
            mEditable = editable;
            selectedColour = list_text_selected;
            unSelectedColour = list_text_unselected;
            if (!editable) {
                if (itemId == 0)
                    tripBookItems = dataSet.getAllRows();
                else {
                    dataSet.setLinkedItemId(itemId);
                    tripBookItems = dataSet.getAllRows();
                }
                selectedItems = new ArrayList<>();
            } else {
                // get all rows for new items with no selected
                if (itemId == 0) {
                    selectedItems = new ArrayList<>();
                    tripBookItems = dataSet.getAllRows();
                } else {
                    selectedItems = dataSet.getAllRows();
                    tripBookItems = new TripBookItemData(activity, dataSet.getItemType()).getAllRows();
                }
            }
        }

        public int getPosition() {
            return mPosition;
        }

        public ArrayList<TripBookItem> getSelectedItems() {
            return selectedItems;
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.linked_item_list, viewGroup, false);
            return new ListViewHolder(v, selectedItems);
        }

        @Override
        public void onBindViewHolder(ListViewHolder listViewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");
            listViewHolder.tripBookItem = ((TripBookItem) tripBookItems.get(position));
            listViewHolder.itemName.setText(listViewHolder.tripBookItem.getTitle());
            ImageWrapper.loadImage(mActivity, listViewHolder.itemImage, Images.imageThumbUrls[position]);

            for (TripBookCommon item : selectedItems) {
                if (item.getId() == listViewHolder.tripBookItem.getId()) {
                    Log.d(TAG, "In selectedItems" + listViewHolder.tripBookItem.getTitle());
                    listViewHolder.isSelected = true;
                }
            }
            if (selectedItems.contains(listViewHolder.tripBookItem)) {
                Log.d(TAG, "In selectedItems" + listViewHolder.tripBookItem.getTitle());
                listViewHolder.isSelected = true;
            } else {
                Log.d(TAG, "Not selectedItems" + listViewHolder.tripBookItem.getTitle());
            }


            if (listViewHolder.isSelected) {
                listViewHolder.itemName.setBackgroundColor(unSelectedColour);
                listViewHolder.itemName.setTextColor(Color.RED);
            } else {
                listViewHolder.itemName.setBackgroundColor(unSelectedColour);
                listViewHolder.itemName.setTextColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return tripBookItems.size();
        }

        /**
         * Custom ListViewHolder)
         */
        class ListViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
            public final TextView itemName;
            public final ImageView itemImage;
            public TripBookItem tripBookItem;
            public boolean isSelected;
            public ArrayList<TripBookItem> selectedItems;


            public ListViewHolder(View view, ArrayList<TripBookItem> selectedItems) {
                super(view);
                itemName = (TextView) view.findViewById(R.id.item_title);
                itemImage = (ImageView) view.findViewById(R.id.item_image);
                isSelected = false;
                this.selectedItems = selectedItems;
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                view.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                //menuInfo is null
//            menu.add(Menu.NONE, 1, Menu.NONE, R.string.title_starred);
//            menu.add(Menu.NONE, 2, Menu.NONE, R.string.title_gallery);
            }

            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "position = " + getPosition() + isSelected, Toast.LENGTH_SHORT).show();

                if (mEditable) {
                    isSelected = !isSelected;
                }
                if (isSelected) {
                    itemName.setTextColor(Color.RED);
                    selectedItems.add(tripBookItem);
                } else {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        if (selectedItems.get(i).getId() == tripBookItem.getId()) {
                            selectedItems.remove(i);
                        }
                    }
                    itemName.setTextColor(Color.WHITE);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                LinkedItemListAdapter.this.mPosition = getPosition();
                return false;
            }
        }
    }


}
