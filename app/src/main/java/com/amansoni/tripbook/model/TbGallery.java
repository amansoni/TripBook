package com.amansoni.tripbook.model;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by Aman on 12/12/2014.
 */
public class TbGallery {
    private String mName;
    private String mDescription;
    private Image mImage;
    private ArrayList<TbImage> mImages;

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image mImage) {
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
