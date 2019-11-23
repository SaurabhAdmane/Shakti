package com.shakticoin.app.api.wallet;

import com.shakticoin.app.util.RecyclerViewItem;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The model class that represents a SXE transaction.
 */
public class Transaction implements RecyclerViewItem {
    private String transId;
    private String description;
    private Date date;
    private BigDecimal amount;

    public Transaction() {}

    public Transaction(String transId, String description, Date date, BigDecimal amount) {
        this.transId = transId;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!getTransId().equals(that.getTransId())) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (!getDate().equals(that.getDate())) return false;
        return getAmount().equals(that.getAmount());
    }

    @Override
    public int hashCode() {
        int result = getTransId().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getAmount().hashCode();
        return result;
    }

    @Override
    public TYPE getItemType() {
        return TYPE.ITEM;
    }
}
