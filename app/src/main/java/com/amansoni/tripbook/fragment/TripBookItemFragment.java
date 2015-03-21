package com.amansoni.tripbook.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.util.Photo;

import java.util.Date;

public class TripBookItemFragment extends BaseFragment {
    public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;

    TripBookItem mTripBookItem;
    TextView mTitleField;
    TextView mNotesField;
    TextView mStartDateField;
    ImageView mPhotoView;
    ImageButton mPhotoButton;


    public static TripBookItemFragment newInstance(long tripBookItemId) {
        Bundle args = new Bundle();
        args.putLong(TripBookItem.ITEM_ID, tripBookItemId);

        TripBookItemFragment fragment = new TripBookItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long itemId = getArguments().getLong(TripBookItem.ITEM_ID);
        mTripBookItem = new TripBookItemData(getActivity()).getItem(itemId);

        setHasOptionsMenu(true);
    }

    public void updateDate() {
        mStartDateField.setText(mTripBookItem.getCreatedAt().toString());
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add, parent, false);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        mTitleField = (EditText) v.findViewById(R.id.trip_add_name);
        mTitleField.setText(mTripBookItem.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mTripBookItem.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

//        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
//        mSolvedCheckBox.setChecked(mTripBookItem.isSolved());
//        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // set the crime's solved property
//                mTripBookItem.setSolved(isChecked);
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

    //TODO
    private void showPhoto() {
        // (re)set the image button's image based on our photo
//        Photo p = mTripBookItem.getPhoto();
//        BitmapDrawable b = null;
//        if (p != null) {
//            String path = getActivity()
//                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
//            b = PictureUtils.getScaledDrawable(getActivity(), path);
//        }
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
//                mTripBookItem.setPhoto(p);
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
        mTripBookItem.update(getActivity());
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
}
