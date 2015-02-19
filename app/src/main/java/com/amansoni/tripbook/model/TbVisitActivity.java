package com.amansoni.tripbook.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Superclass for Trips and Places.
 *
 */
public class TbVisitActivity {

    private int mId;
    private String mName;
    private TbImage mDisplayImage;
    private ArrayList<TbImage> mImages;
    private ArrayList<TbFriend> mFriends;
    private Date mVisitDate;

    private String mNotes;

    public int getTripId() {
        return mId;
    }

    public void setTripId(int mTripId) {
        this.mId = mTripId;
    }

    public String getTripName() {
        return mName;
    }

    public void setTripName(String mTripName) {
        this.mName = mTripName;
    }

    public ArrayList<TbImage> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<TbImage> mImages) {
        this.mImages = mImages;
    }


    public Date getStartDate() {
        return mVisitDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mVisitDate = mStartDate;
    }


    public TbImage getDisplayImage() {
        return mDisplayImage;
    }

    public void setDisplayImage(TbImage mDisplayImage) {
        this.mDisplayImage = mDisplayImage;
    }

    public ArrayList<TbFriend> getFriends() {
        return mFriends;
    }

    public void setFriends(ArrayList<TbFriend> mFriends) {
        this.mFriends = mFriends;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }


}