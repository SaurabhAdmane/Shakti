package com.shakticoin.app.profile;

/**
 * Data class that holds information about file item in KYC files list.
 */
class KycDocumentObject {
    private int categoryId;
    private String fileName;
    private String fileType;

    KycDocumentObject(String fileName, int categoryId, String fileType) {
        this.categoryId = categoryId;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    String getFileName() {
        return fileName;
    }
}
