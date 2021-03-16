package com.vedas.apna.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vedas.apna.R;

/**
 * Created by User on 6/4/2017.
 */

@SuppressWarnings("JavaDoc")
public class UniversalImageLoader {

    private static final int defaultImage = R.drawable.image_3;
    private final Context mContext;

    public UniversalImageLoader(Context context) {
        mContext = context;
    }

    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .considerExifParams(true)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        return new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
    }

    /**
     * this method can be sued to set images that are static. It can't be used if the images
     * are being changed in the Fragment/Activity - OR if they are being set in a list or
     * a grid
     * @param imgURL
     * @param image
     * @param mProgressBar
     * @param append
     */
    public static void setImage(String imgURL, ImageView image, final ProgressBar mProgressBar, String append){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        /* Bitmap bmp= ImageLoader.getInstance().getMemoryCache().get(imgURL);
        if(bmp!=null){
            image.setImageBitmap(bmp);
        }else{
            //Ensure that the disk cache is configured in the current loading configuration true
            File cache=ImageLoader.getInstance().getDiskCache().get(imgURL);
            if(cache!=null){
                //According to specific needs, you can define the Bitmap compression item yourself, but generally you don’t need to customize it again
                //The initial configuration of UIL has already determined the compression result, the effect is still relatively good, just decode directly
                image.setImageBitmap(BitmapFactory.decodeFile(cache.getAbsolutePath()));
            }else{
                DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                        .showImageOnLoading(defaultImage)
                        .showImageForEmptyUri(defaultImage)
                        .showImageOnFail(defaultImage)
                        .considerExifParams(true)
                        .cacheOnDisk(true).cacheInMemory(true)
                        .cacheOnDisk(true).resetViewBeforeLoading(true)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .displayer(new FadeInBitmapDisplayer(300)).build();
                //If there are none of the first two, it proves that the image has not been cached, so please load it with the traditional method.
                ImageLoader.getInstance().displayImage(append + imgURL,image, defaultOptions);
            }
        }
*/
    }
}
