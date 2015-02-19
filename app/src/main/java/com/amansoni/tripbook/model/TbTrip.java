package com.amansoni.tripbook.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aman on 12/12/2014.
 */
public class TbTrip extends TbPlace {
    private Date mEndDate;
    private ArrayList<TbPlace> mPlaces;

    public ArrayList<TbPlace> getPlaces() {
        return mPlaces;
    }

    public void setPlaces(ArrayList<TbPlace> places) {
        this.mPlaces = places;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        this.mEndDate = endDate;
    }


}
