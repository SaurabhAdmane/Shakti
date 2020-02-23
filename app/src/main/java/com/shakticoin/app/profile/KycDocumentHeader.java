package com.shakticoin.app.profile;

/**
 * Data class that hold a section title for list of KYC files. The title is a
 * corresponding KYC category title.
 */
public class KycDocumentHeader {
    private String title;
    private int categoryId;

    KycDocumentHeader(String title, int categoryId) {
        this.title = title;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
