package com.shakticoin.app.api.kyc;

public class FileDescriptor {
    private Integer category_id;
    private Integer doc_type_id;

    public FileDescriptor() {
    }

    public FileDescriptor(Integer category_id, Integer doc_type_id) {
        this.category_id = category_id;
        this.doc_type_id = doc_type_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getDoc_type_id() {
        return doc_type_id;
    }

    public void setDoc_type_id(Integer doc_type_id) {
        this.doc_type_id = doc_type_id;
    }
}
