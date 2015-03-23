package com.amansoni.tripbook.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.amansoni.tripbook.R;

/**
 * Created by Aman on 17/03/2015.
 */
public class ImageWrapper {
    private static final String TAG = "ImageWrapper";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    public static void loadImageFromFile(Context context, ImageView imageView, String imageSource, int size) {
        ImageResizer imageResizer = new ImageResizer(context, size);
        imageView.setImageBitmap(imageResizer.processBitmap(imageSource));
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void loadImageFromFile(Fragment fragment, ImageView imageView, String imageSource, int size) {
        ImageResizer imageResizer = new ImageResizer(fragment.getActivity(), size);
        imageView.setImageBitmap(imageResizer.processBitmap(imageSource));
    }

    //        String imagePath = new TripBookImageData().getItem(0).getFilePath();
//        File image = new File(imagePath);
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap,72,72,true);
//        if (tripBookItem.getThumbnail() != null) {
//            ImageView mainImage = (ImageView) view.findViewById(R.id.item_main_image);
//            mainImage.setImageBitmap(tripBookItem.getThumbnail());
//        }

}
