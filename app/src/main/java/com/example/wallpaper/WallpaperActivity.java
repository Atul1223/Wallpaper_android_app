package com.example.wallpaper;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WallpaperActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button,save;
    private String url;
    WallpaperManager wallpaperManager;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        imageView=findViewById(R.id.wallpaperActivityImgView);
        button=findViewById(R.id.btnSetWallpaper);
        progressBar=findViewById(R.id.progBarWallpaper);
        save=findViewById(R.id.btnSaveWallpaper);

        Intent intent;
        intent=getIntent();
        url=intent.getStringExtra("imgUrl");


        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(WallpaperActivity.this,"Failed to load image",Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);

        wallpaperManager=(WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(WallpaperActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(WallpaperActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                if(ActivityCompat.checkSelfPermission(WallpaperActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    downloadImage("wallpaper",url);
                }
                else{
                    Toast.makeText(WallpaperActivity.this,"Storage permission Required",Toast.LENGTH_LONG).show();
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WallpaperActivity.this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(WallpaperActivity.this,"Fail to load Image",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        try {

                            //wallpaperManager.clear();
                            wallpaperManager.setBitmap(resource);

                        }catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(WallpaperActivity.this,"Fail to set Wallpaper",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                }).submit();
                Toast.makeText(WallpaperActivity.this,"Setting up you Wallpaper...",Toast.LENGTH_LONG).show();
                Toast.makeText(WallpaperActivity.this,"This may take some time..",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void downloadImage(String filename,String imgUrl){

        try {

            DownloadManager downloadManager;
            downloadManager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri=Uri.parse(imgUrl);

            DownloadManager.Request request=new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                    .setAllowedOverRoaming(true)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator+filename+".jpeg")
                    .setVisibleInDownloadsUi(true);

            downloadManager.enqueue(request);

            Toast.makeText(this,"Wallpaper Downloading...",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(this,"Failed to download",Toast.LENGTH_SHORT).show();
        }

    }

}