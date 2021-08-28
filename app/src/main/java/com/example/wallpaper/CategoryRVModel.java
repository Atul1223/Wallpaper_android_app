package com.example.wallpaper;

public class CategoryRVModel {

    private String category;
    private String categoryImgUrl;

    public CategoryRVModel(String category,String categoryImgUrl){
        this.category=category;
        this.categoryImgUrl=categoryImgUrl;
    }

    public String getCategory(){
        return category;
    }
    public String getCategoryImgUrl(){
        return categoryImgUrl;
    }
    public void setCategoryImgUrl(String categoryImgUrl){
        this.categoryImgUrl=categoryImgUrl;
    }
}
