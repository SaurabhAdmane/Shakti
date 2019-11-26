package com.shakticoin.app.util;

import androidx.annotation.NonNull;

public class RecyclerViewHeader implements RecyclerViewItem {
    private String title;

    public RecyclerViewHeader(@NonNull String title) {
        this.title = title;
    }

    @Override
    public TYPE getItemType() {
        return TYPE.HEADER;
    }

    @Override
    public String toString() {
        return title;
    }
}
