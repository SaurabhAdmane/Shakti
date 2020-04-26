package com.shakticoin.app.profile;

/**
 * Data class that holds information about file item in KYC files list.
 */
class KycDocumentObject {
    private int categoryId;
    private String fileName;
    private int fileType;

    KycDocumentObject(String fileName, int categoryId, int fileType) {
        this.categoryId = categoryId;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    String getFileName() {
        return fileName;
    }
}
