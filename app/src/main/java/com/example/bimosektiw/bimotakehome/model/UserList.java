package com.example.bimosektiw.bimotakehome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserList {
    @SerializedName("total_count")
    @Expose
    private int totalCount;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("items")
    @Expose
    private List<User> items = null;

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
