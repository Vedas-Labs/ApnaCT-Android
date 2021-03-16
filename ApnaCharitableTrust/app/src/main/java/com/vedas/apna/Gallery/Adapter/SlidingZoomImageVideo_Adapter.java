package com.vedas.apna.Gallery.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vedas.apna.R;
import com.vedas.apna.ServerConnections.ServerApiCollection;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;

public class SlidingZoomImageVideo_Adapter extends PagerAdapter {

    private final ArrayList<String> IMAGES;
    private final LayoutInflater inflater;
    //final ViewPagerFixed pager;

    public SlidingZoomImageVideo_Adapter(Context context, ArrayList<String> IMAGES) {
        this.IMAGES=IMAGES;
        //this.pager = pager;
        inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingzoomimagevideos_layout, view, false);
        assert imageLayout != null;
        ImageView iv = imageLayout.findViewById(R.id.simg);
        ImageView ip = imageLayout.findViewById(R.id.playimg);
        RelativeLayout rl_play = imageLayout.findViewById(R.id.rl_play);
        final PhotoView imageView = imageLayout.findViewById(R.id.imageView);
        view.addView(imageLayout, 0);
        Glide.with(view.getContext()).load( ServerApiCollection.BASE_URL1+IMAGES.get(position).substring(1))
                .apply(new RequestOptions().placeholder(R.drawable.image_3))
                .into(imageView);
        Glide.with(view.getContext()).load(ServerApiCollection.BASE_URL1+IMAGES.get(position).substring(1))
                .apply(new RequestOptions().placeholder(R.drawable.image_3))
                .into(iv);
        ip.setClickable(true);
        if (IMAGES.get(position).contains("mp4")){
            rl_play.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }else {
            rl_play.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
        ip.setOnClickListener(v -> {
            // TODO Auto-generated method stub
           /* String n=IMAGES.get(position);
            Intent i=new Intent(context, VideoActivity.class);
            i.putExtra("imagePos",ServerApiCollection.BASE_URL1+IMAGES.get(position).substring(1));
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(i);*/
        });
        return imageLayout;
    }

    @Override
    public int getCount() {
        if (IMAGES != null){
            return Math.max(IMAGES.size(), 0);
        }else {
            return 0;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}