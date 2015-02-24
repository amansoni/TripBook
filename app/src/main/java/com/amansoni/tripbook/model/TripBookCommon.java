package com.amansoni.tripbook.model;

import com.amansoni.tripbook.Utils;

import java.util.Date;

/**
 * Created by Aman on 15/02/2015.
 */
public abstract class TripBookCommon {
    protected Long mId;
    protected Date mCreatedAt;
    protected boolean isStarred;

    public TripBookCommon() {
    }

    public TripBookCommon(Long mId) {
        this.mId = mId;
        this.mCreatedAt = new Date();
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
        this.mCreatedAt = Utils.formatDateTime(createdAt);
    }

    public void star() {
        isStarred = !isStarred;
    }


    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }
}
