package com.example.wallpaper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private EditText searchEdit;
    private ImageView searchImg;
    private RecyclerView categoryRV,wallpaperRV;
    private ProgressBar loadingProgress;
    private ArrayList<String> wallpaperArrayList;
    private ArrayList<String> originalSizeWallpaper;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
    protected int a;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        Random random=new Random();
        a=random.nextInt(100);
        a++;

        searchEdit=findViewById(R.id.idEditSearch);
        searchImg=findViewById(R.id.idSearch);
        categoryRV=findViewById(R.id.category);
        wallpaperRV=findViewById(R.id.wallpapers);
        loadingProgress=findViewById(R.id.progBar);

        wallpaperArrayList=new ArrayList<>();
        categoryRVModelArrayList=new ArrayList<>();
        originalSizeWallpaper=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModelArrayList,this,this::onClickCategory);
        categoryRV.setLayoutManager(linearLayoutManager);
        categoryRV.setAdapter(categoryRVAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        wallpaperRVAdapter =new WallpaperRVAdapter(wallpaperArrayList,originalSizeWallpaper,this);
        wallpaperRV.setLayoutManager(gridLayoutManager);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getWallpapers();
        getCategory();

        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr=searchEdit.getText().toString();
                if (searchStr.isEmpty()){
                    Toast.makeText(MainActivity.this,"Empty Search",Toast.LENGTH_LONG).show();
                }else {
                    getWallpaperBySearch(searchStr);
                }
            }
        });
    }

    private void getWallpapers(){

        wallpaperArrayList.clear();
        originalSizeWallpaper.clear();
        loadingProgress.setVisibility(View.VISIBLE);



        String url="https://api.pexels.com/v1/curated?per_page=100&page="+a+"";
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingProgress.setVisibility(View.GONE);
                try {
                    JSONArray photoArray=response.getJSONArray("photos");
                    for (int i=0;i<photoArray.length();i++){
                        JSONObject photoObject=photoArray.getJSONObject(i);
                        String originalImgUrl=photoObject.getJSONObject("src").getString("original");
                        String imgUrl=photoObject.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                        originalSizeWallpaper.add(originalImgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"Fail to load Wallpaper",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Authorization","YOUR API KEY FROM PEXEL");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void getCategory(){

        categoryRVModelArrayList.add(new CategoryRVModel("Random","https://images.unsplash.com/photo-1485550409059-9afb054cada4?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=802&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://images.unsplash.com/photo-1483000805330-4eaf0a0d82da?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80t"));
        categoryRVModelArrayList.add(new CategoryRVModel("Nature","https://images.unsplash.com/photo-1518495973542-4542c06a5843?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Minimal","https://images.unsplash.com/photo-1492321936769-b49830bc1d1e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Space","https://images.unsplash.com/photo-1501862700950-18382cd41497?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=894&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Abstract Art","https://images.unsplash.com/photo-1564951434112-64d74cc2a2d7?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Dark","https://images.unsplash.com/photo-1517999144091-3d9dca6d1e43?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2267&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Travel","https://images.unsplash.com/photo-1476900543704-4312b78632f8?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Animal","https://images.unsplash.com/photo-1555169062-013468b47731?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Alphabet","https://images.unsplash.com/photo-1468528885091-58bab38a6632?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Anime","https://images.unsplash.com/photo-1578632767115-351597cf2477?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=668&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Coral Reef","https://images.unsplash.com/photo-1611833767698-7a8a336761db?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=750&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Rain","https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80"));
        categoryRVAdapter.notifyDataSetChanged();


    }

    private void getWallpaperBySearch(String search){
        wallpaperArrayList.clear();
        originalSizeWallpaper.clear();
        loadingProgress.setVisibility(View.VISIBLE);

        String url;
        if(search.equals("Random")){
            getWallpapers();
            return;
        }
        else{
            if(search.length()<20){
                url="https://api.pexels.com/v1/search/?page="+a+"&per_page=80&query="+search+"";
            }
            else{
                url=search;
            }
        }

        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingProgress.setVisibility(View.GONE);
                try {
                    JSONArray photoArray=response.getJSONArray("photos");
                    if(photoArray.length()==0){
                        if(photoArray.length()==0){
                            String url="https://api.pexels.com/v1/search/?page=1&per_page=80&query="+search+"";
                            getWallpaperBySearch(url);
                        }
                    }
                    for (int i=0;i<photoArray.length();i++){
                        JSONObject photoObject=photoArray.getJSONObject(i);
                        String imgUrl=photoObject.getJSONObject("src").getString("portrait");
                        String originalImgUrl=photoObject.getJSONObject("src").getString("original");
                        wallpaperArrayList.add(imgUrl);
                        originalSizeWallpaper.add(originalImgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Authorization","YOUR API KEY FROM PEXEL");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClickCategory(int postion) {

        String category=categoryRVModelArrayList.get(postion).getCategory();
        getWallpaperBySearch(category);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.wallpaper)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
