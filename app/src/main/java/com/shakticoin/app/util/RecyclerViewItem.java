package com.shakticoin.app.util;

public interface RecyclerViewItem {
    enum TYPE {
        ITEM,
        HEADER
    }

    public RecyclerViewItem.TYPE getItemType();
}
