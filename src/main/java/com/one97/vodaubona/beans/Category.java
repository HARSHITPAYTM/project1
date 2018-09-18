package com.one97.vodaubona.beans;

import java.util.List;

public class Category {
    private String categoryId;
    private List<String> categorySongs;
    private List<Category> subCategories;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCategorySongs() {
        return categorySongs;
    }

    public void setCategorySongs(List<String> categorySongs) {
        this.categorySongs = categorySongs;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nCategory [categoryId=");
        builder.append(categoryId);
        builder.append(", categorySongs=");
        builder.append(categorySongs);
        builder.append(", \tsubCategories=");
        builder.append(subCategories);
        builder.append("]");
        return builder.toString();
    }

}
