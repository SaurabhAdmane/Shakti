package com.shakticoin.app.profile;

/**
 * Data class that hold information about a list item from KYC files. The item can be
 * either a header line of file. Correspondingly, we use KycDocumentHeader and KycDocumentObject
 * classes.
 * @see KycDocumentHeader
 * @see KycDocumentObject
 */
public class KycDocumentItem<T> {
    private T item;

    KycDocumentItem(T item) {
        this.item = item;
    }

    public T get() {
        return item;
    }

    public String getTitle() {
        return item instanceof KycDocumentHeader ?
                ((KycDocumentHeader) item).getTitle() : ((KycDocumentObject) item).getFileName();
    }
}
