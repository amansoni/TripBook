package com.amansoni.tripbook.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.amansoni.tripbook.util.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookItem extends TripBookCommon {
    // itemType constants
    public final static String TYPE_TRIP = "Trip";
    public final static String TYPE_PLACE = "Place";
    public static final String TYPE_Image = "Image";
    public static final String TYPE_GALLERY = "Gallery";
    public static final String TYPE_STARRED = "Favourite";
    public static final String TYPE_FRIENDS = "Friend";
    public static final String ITEM_ID = "itemId";
    public static final String ITEM_TYPE = "itemType";
    List<TripBookCommon> mLinks;

    // private fields
    private String mTitle;
    private String mDescription;
    private String mItemType;
    private String mThumbNail;
    private Photo mPhoto;

    public TripBookItem() {
        super();
        mLinks = new ArrayList<>();
    }

    public TripBookItem(String title, String itemType) {
        this();
        mTitle = title;
        mItemType = itemType;
    }

    public TripBookItem(String title, String itemType, boolean starred) {
        this();
        mTitle = title;
        mItemType = itemType;
        isStarred = starred;
    }

    @Override
    public String toString() {
//        return mItemType + ": " + mTitle;
        String s = mItemType + ": " + mTitle;
        if (isStarred)
            s = s + " (favourite)";

        if (mDescription != null && mDescription.length() > 0)
            s = s + "\n" + mDescription;

        if (mEndDate != null && mEndDate.length() > 0)
            s = s + "\nFrom: " + mCreatedAt + " to " + mEndDate;
        else {
            if (mCreatedAt != null && mCreatedAt.length()>0)
                s = s + " Date: " + mCreatedAt;
        }
        return s;
//        String sPlace = "";
//        String sFriend = "";
//        switch (mItemType){
//            case TYPE_TRIP:
//
//                } else {
//                    s = TYPE_TRIP + ": " + mTitle + "\n" + mDescription + "\nOn:" + mCreatedAt;
//                }
//                break;
//            case TYPE_PLACE:
//                break;
//            case TYPE_GALLERY:
//                break;
//        }
//        s = "Last visit " + this.getCreatedAt();

    }

    public String getItemType() {
        return mItemType;
    }

    public void setItemType(String mItemType) {
        this.mItemType = mItemType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getThumbnail() {
        return mThumbNail;
    }

    public void setThumbnail(String thumbNail) {
        mThumbNail = thumbNail;
    }

    public List<TripBookCommon> getLinks() {
        return mLinks;
    }

    public void setLinks(List<TripBookCommon> links) {
        mLinks = links;
    }

    public void addLink(TripBookCommon linkItem) {
        mLinks.add(linkItem);
    }

    public void update(Activity activity) {
        new TripBookItemData(activity).update(this);
    }

    public String[] getImages() {
        return new String[]{"file:///storage/emulated/0/Pictures/TripBook/IMG_20150310_080409.jpg",
                "file:///storage/emulated/0/Pictures/TripBook/IMG_20150310_134840.jpg",
                "file:///storage/emulated/0/Pictures/TripBook/IMG_20150310_152455.jpg",
                "file:///storage/emulated/0/Pictures/TripBook/IMG_20150301_170051.jpg",
                "file:///storage/emulated/0/Pictures/TripBook/IMG_20150309_093228.jpg",
        };
    }


}