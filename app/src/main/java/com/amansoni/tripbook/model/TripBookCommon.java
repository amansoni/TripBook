package com.amansoni.tripbook.model;

import android.location.Location;

import com.amansoni.tripbook.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Aman on 15/02/2015.
 */
public abstract class TripBookCommon {
    protected Long mId;
    protected String mCreatedAt;
    protected String mEndDate;
    protected boolean isStarred;
    private TbGeolocation mLocation;

    public TripBookCommon() {
    }

    public TripBookCommon(Long mId) {
        this.mId = mId;
        //this.mCreatedAt = new Date();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getCreatedAt() {
        return (mCreatedAt != null ? mCreatedAt.toString() : "");
    }

    public void setCreatedAt(String createdAt) {
        this.mCreatedAt = createdAt; //Utils.formatDateTime(createdAt);
    }

    public void star() {
        isStarred = !isStarred;
    }


    public boolean isStarred() {
        return isStarred;
    }

    public void setLocation(TbGeolocation location) {
        this.mLocation = location;
    }

    public TbGeolocation getLocation(){
        return mLocation;
    }

    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate; //Utils.formatDateTime(endDate);
    }

    public String getEndDate(){
        return (mEndDate != null ? mEndDate.toString() : "");
    }
}
