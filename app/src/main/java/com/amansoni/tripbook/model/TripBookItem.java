package com.amansoni.tripbook.model;

import android.app.Activity;
import android.graphics.Bitmap;
import java.net.URI;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookItem extends TripBookCommon {
    // itemType constants
    public final static String TYPE_TRIP = "Trips";
    public final static String TYPE_PLACE = "Places";
    public static final String TYPE_Image = "Images";
    public static final String TYPE_GALLERY = "Gallery";
    public static final String TYPE_STARRED = "Favourite";
    public static final String TYPE_FRIENDS = "Friends";
    public static final String ITEM_ID = "itemId";
    List<TripBookCommon> mLinks;
    // private fields
    private String mTitle;
    private String mDescription;
    private String mItemType;
    private Bitmap mThumbNail;

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
        String s = "";
        s = "Last visit " + this.getCreatedAt();
        return mTitle;
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

    public Bitmap getThumbnail() {
        return mThumbNail;
    }

    public void setThumbnail(Bitmap thumbNail) {
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

    public URI getThumbnailUri(){
        try {
            return new URI("file:///storage/emulated/0/Pictures/TripBook/IMG_20150310_080409.jpg");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}