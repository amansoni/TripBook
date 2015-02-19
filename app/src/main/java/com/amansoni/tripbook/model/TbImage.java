package com.amansoni.tripbook.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.Date;

/**
 * Created by Aman on 05/12/2014.
 */
public class TbImage {
    private Long mId;
    private String mName;
    private String mDescription;
    private String mImage;
    private TbGeolocation mLocation;
    private Date dateAdded;

    public String toString() {
        return "TbImage id:" + getId()
                + "\nName" + getName()
                + "\nDescription" + getDescription()
                + "\nImage(Path)" + getImage();
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getImage() {
        return (mImage == null ? "" : mImage);
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
