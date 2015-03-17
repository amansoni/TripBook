package com.amansoni.tripbook.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.provider.Images;

/**
 * Created by Aman on 17/03/2015.
 */
public class ImageWrapper {
    private static final String TAG = "ItemViewFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    public static void loadImage(Fragment fragment, ImageView imageView, String imageSource){

        int mImageThumbSize = fragment.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(fragment.getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        ImageFetcher mImageFetcher = new ImageFetcher(fragment.getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(fragment.getActivity().getSupportFragmentManager(), cacheParams);


        // Finally load the image asynchronously into the ImageView, this also takes care of
        // setting a placeholder image while the background thread runs
        mImageFetcher.loadImage(imageSource, imageView);

    }

    public static void loadImageFromFile(Context context, ImageView imageView, String imageSource, int size){
        ImageResizer imageResizer = new ImageResizer(context, size);
        imageView.setImageBitmap(imageResizer.processBitmap(imageSource));
    }

    public static void loadImageFromFile(Fragment fragment, ImageView imageView, String imageSource, int size){
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
