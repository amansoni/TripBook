package com.amansoni.tripbook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookItem extends TripBookCommon {
    // itemType constants
    public final static String TYPE_TRIP = "trip";
    public final static String TYPE_PLACE = "place";
    public static final String TYPE_GALLERY = "gallery";
    public static final String TYPE_STARRED = "starred";
    public static final String TYPE_FRIENDS = "friend";
    List<TripBookImage> mTripBookImages;
    // private fields
    private String mTitle;
    private String mItemType;

    public TripBookItem() {
        super();
        mTripBookImages = new ArrayList<>();
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

    public List<TripBookImage> getTripBookImages() {
        return mTripBookImages;
    }

    public void setTripBookImages(List<TripBookImage> tripBookImages) {
        mTripBookImages = tripBookImages;
    }

    public void addImage(TripBookImage tripBookImage) {
        mTripBookImages.add(tripBookImage);
    }

}
