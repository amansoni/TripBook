package com.amansoni.tripbook.model;

import android.graphics.Bitmap;

import com.amansoni.tripbook.db.TripBookItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookItem extends TripBookCommon {
    // itemType constants
    public final static String TYPE_TRIP = "Trips";
    public final static String TYPE_PLACE = "Places";
    public static final String TYPE_GALLERY = "Images";
    public static final String TYPE_STARRED = "Favourite";
    public static final String TYPE_FRIENDS = "Friends";
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

    public Bitmap getThumbnail(){
        return mThumbNail;
    }

    public void setThumbnail(Bitmap thumbNail){
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

    public void update(){
        new TripBookItemData().update(this);
    }

}
