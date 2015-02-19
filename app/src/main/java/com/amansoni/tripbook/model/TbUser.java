package com.amansoni.tripbook.model;

import java.util.ArrayList;

/**
 * Created by Aman on 09/12/2014.
 */
public class TbUser {
    private int mId;
    private String mEmail;
    private String mName;
    private ArrayList<TbSocialAccounts> mSocialAccounts;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public ArrayList<TbSocialAccounts> getSocialAccounts() {
        return mSocialAccounts;
    }

    public void setSocialAccounts(ArrayList<TbSocialAccounts> socialAccounts) {
        this.mSocialAccounts = socialAccounts;
    }



}
