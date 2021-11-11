package com.example.shoppingapp;

public class CategoryModel {
    private String CategoryIconLink;
    private String CategoryIconName;

    public CategoryModel(String categoryIconLink, String categoryIconName) {
        CategoryIconLink = categoryIconLink;
        CategoryIconName = categoryIconName;
    }

    public String getCategoryIconLink() {
        return CategoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        CategoryIconLink = categoryIconLink;
    }

    public String getCategoryIconName() {
        return CategoryIconName;
    }

    public void setCategoryIconName(String categoryIconName) {
        CategoryIconName = categoryIconName;
    }
}
