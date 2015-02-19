package com.amansoni.tripbook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookLink extends TripBookCommon {
    // private fields
    private TripBookCommon mParent;
    private TripBookCommon mChild;

    public TripBookLink() {
        super();
    }

    public TripBookLink(TripBookCommon parent, TripBookCommon child) {
        this();
        mParent = parent;
        mChild = child;
    }

    public TripBookCommon getParent() {
        return mParent;
    }

    public TripBookCommon getChild() {
        return mChild;
    }

    public void setChild(TripBookCommon child) {
        this.mChild = child;
    }

    public void setParent(TripBookCommon parent) {
        this.mParent = parent;
    }

}