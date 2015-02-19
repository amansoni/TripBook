package com.amansoni.tripbook.model;

import com.google.android.gms.plus.model.people.Person.Image;

/**
 * Created by Aman on 05/12/2014.
 */
public class TbFriend {
    private String name;
    private boolean bNotify;
    private boolean bBlock;
    private TbSocialAccounts socialNetwork;
    private Image mImage;


    public TbSocialAccounts getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(TbSocialAccounts socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotify() {
        return bNotify;
    }

    public void setNotify(boolean bNotify) {
        this.bNotify = bNotify;
    }

    public boolean isBlock() {
        return bBlock;
    }

    public void setBlock(boolean bBlock) {
        this.bBlock = bBlock;
    }
    public Image getImage() {
        return mImage;
    }

    public void setImage(Image mImage) {
        this.mImage = mImage;
    }



}
