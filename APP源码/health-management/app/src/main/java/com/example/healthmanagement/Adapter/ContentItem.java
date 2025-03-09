package com.example.healthmanagement.Adapter;

public class ContentItem {

    final String name;
    final String desc;
    boolean isSection = false;

    ContentItem(String n) {
        name = n;
        desc = "";
        isSection = true;
    }

    public ContentItem(String n, String d) {
        name = n;
        desc = d;
    }
}
