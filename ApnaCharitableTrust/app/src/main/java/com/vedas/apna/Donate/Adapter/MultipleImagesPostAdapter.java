package com.vedas.apna.Donate.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.vedas.apna.R;

import java.io.File;
import java.util.ArrayList;

public class MultipleImagesPostAdapter extends RecyclerView.Adapter<MultipleImagesPostAdapter.ImageAdapter> {
    public final Context context;
    final ArrayList<Uri> uriList;
    public final ImageView galleryPics;
    final ArrayList<File> files;
    ArrayList<String> strings=new ArrayList<>();

    public MultipleImagesPostAdapter(Context context, ArrayList<Uri> uriList, ArrayList<File> files, ImageView galleryPics) {
        this.context = context;
        this.uriList = uriList;
        this.galleryPics=galleryPics;
        this.files=files;
    }

    @NonNull
    @Override
    public ImageAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.images_from_gallery,null);
        return new ImageAdapter(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter holder, int position) {
        Log.e("nf",""+uriList.size());
        Log.e("nfhj",""+files.size());
        galleryPics.setVisibility(View.GONE);
        if(uriList.size()==position ) {
            galleryPics.setVisibility(View.GONE);
            holder.delPic.setVisibility(View.GONE);
            holder.crossMark.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.drawable.group_1799);
            if(uriList.size()==3){
                holder.imageView.setVisibility(View.GONE);
            }else {
                holder.imageView.setVisibility(View.VISIBLE);
            }
        }else{
            Log.e("nfddf","hbhj"+position);
            holder.delPic.setVisibility(View.VISIBLE);
            holder.crossMark.setVisibility(View.VISIBLE);
            Picasso.get().load(uriList.get(position)).into(holder.imageView);
        }

        holder.delPic.setOnClickListener(v -> {
            Log.e("ndfddf",""+uriList.get(holder.getAdapterPosition()));
            uriList.remove(holder.getAdapterPosition());
            files.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            galleryPics.setVisibility(View.VISIBLE);
        });

        holder.imageView.setOnClickListener(v -> {
            strings=new ArrayList<>();
            if (holder.getAdapterPosition() >= uriList.size()) {
                galleryPics.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ImageAdapter extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final ImageView delPic;
        final ImageView crossMark;
        public ImageAdapter(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv1);
            delPic=itemView.findViewById(R.id.delPic);
            crossMark=itemView.findViewById(R.id.crossMark);
        }
    }
}
