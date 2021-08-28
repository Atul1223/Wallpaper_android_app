package com.example.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {

    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private Context context;
    private CategoryClickInterface categoryClickInterface;


    public CategoryRVAdapter(ArrayList<CategoryRVModel> categoryRVModelArrayList, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVModelArrayList = categoryRVModelArrayList;
        this.context = context;
        this.categoryClickInterface=categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.category_rv_item,parent,false);
        return new CategoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        CategoryRVModel categoryRVModel=categoryRVModelArrayList.get(position);
        holder.categoryTV.setText(categoryRVModel.getCategory());
        Glide.with(context).load(categoryRVModel.getCategoryImgUrl()).into(holder.categoryIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryClickInterface.onClickCategory(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryTV;
        private ImageView categoryIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV=itemView.findViewById(R.id.textCategory);
            categoryIV=itemView.findViewById(R.id.imgCategory);
        }
    }

    public interface CategoryClickInterface{
        public void onClickCategory(int postion);
    }
}
