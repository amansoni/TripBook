package com.amansoni.tripbook.model;

/**
 * Created by Aman on 12/12/2014.
 */
public class TbSocialAccounts {
    private int mId;
    private String mName;
    private String mUsername;
    private String mAuthToken;
    private boolean bUpdate;
    private boolean bPost;
    private boolean bGetFriends;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = mName;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = mUsername;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        this.mAuthToken = mAuthToken;
    }

    public boolean isbUpdate() {
        return bUpdate;
    }

    public void setUpdate(boolean bUpdate) {
        this.bUpdate = bUpdate;
    }

    public boolean isPost() {
        return bPost;
    }

    public void setPost(boolean bPost) {
        this.bPost = bPost;
    }

    public boolean isGetFriends() {
        return bGetFriends;
    }

    public void setGetFriends(boolean bGetFriends) {
        this.bGetFriends = bGetFriends;
    }


}
