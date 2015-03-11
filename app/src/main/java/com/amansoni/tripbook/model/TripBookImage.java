package com.amansoni.tripbook.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.amansoni.tripbook.util.AsyncTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Aman on 15/02/2015.
 */
public class TripBookImage extends TripBookCommon {
    public static final int THUMBNAIL_HEIGHT = 100;
    public static final int THUMBNAIL_WIDTH = 100;
    private String mFilePath;
    private byte[] mImage;
    private Bitmap mThumbNail;

    public TripBookImage() {
        super();
    }

    public TripBookImage(String filePath) {
        super();
        mFilePath = filePath;
        Bitmap bmp = BitmapFactory.decodeFile(mFilePath); //Uri.parse(mFilePath).toString());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mImage = stream.toByteArray();
    }

    public TripBookImage(Long id, String filePath, byte[] thumbnail) {
        super(id);
        mFilePath = filePath;
        mImage = thumbnail;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public Bitmap getThumbnail() {
        return mThumbNail;
    }

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] byteStream) {
        mImage = byteStream;
        // Calculate inSampleSize
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        mThumbNail = BitmapFactory.decodeByteArray(byteStream, 0, byteStream.length, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
