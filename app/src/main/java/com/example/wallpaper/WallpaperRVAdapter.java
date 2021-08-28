package com.example.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WallpaperRVAdapter extends RecyclerView.Adapter<WallpaperRVAdapter.ViewHolder> {

    private ArrayList<String> wallpaperUrl;
    private ArrayList<String>  originalSizeWallpaperUrl;
    private Context context;

    public WallpaperRVAdapter(ArrayList<String> wallpaperUrl,ArrayList<String> originalSizeWallpaperUrl, Context context) {
        this.wallpaperUrl = wallpaperUrl;
        this.context = context;
        this.originalSizeWallpaperUrl=originalSizeWallpaperUrl;
    }

    @NonNull
    @Override
    public WallpaperRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallpaper_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(wallpaperUrl.get(position)).into(holder.wallpaperIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WallpaperActivity.class);
                intent.putExtra("imgUrl",originalSizeWallpaperUrl.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperUrl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView wallpaperIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperIV=itemView.findViewById(R.id.imgWallpaper);
        }
    }
}
