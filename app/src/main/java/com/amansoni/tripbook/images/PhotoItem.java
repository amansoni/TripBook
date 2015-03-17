package com.amansoni.tripbook.images;

/**
 * Created by Aman on 16/03/2015.
 */
import android.net.Uri;

/**
 * Used to represent a photo item.
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoItem {

    // Ivars.
    private Uri thumbnailUri;
    private Uri fullImageUri;

    public PhotoItem(Uri thumbnailUri,Uri fullImageUri) {
        this.thumbnailUri = thumbnailUri;
        this.fullImageUri = fullImageUri;
    }

    /**
     * Getters and setters
     */
    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public Uri getFullImageUri() {
        return fullImageUri;
    }

    public void setFullImageUri(Uri fullImageUri) {
        this.fullImageUri = fullImageUri;
    }
}