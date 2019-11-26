package com.shakticoin.app.api.wallet;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.OnCompleteListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletRepository {

    /**
     * Returns list of transactions on an account
     * // TODO: we should request transactions for a particular wallet but for now it's not clear
     */
    public void getTransactions(@NonNull OnCompleteListener<List<Transaction>> listener) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction("00001", "Transaction Name", new Date(119, 8, 1, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00002", "Transaction Name", new Date(119, 8, 1, 11, 25, 30), new BigDecimal("-12.9")));
        transactionList.add(new Transaction("00003", "Transaction Name", new Date(119, 8, 4, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00004", "Transaction Name", new Date(119, 8, 11, 11, 23, 30), new BigDecimal("-12.5")));
        transactionList.add(new Transaction("00005", "Transaction Name", new Date(119, 8, 12, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00006", "Transaction Name", new Date(119, 8, 15, 11, 23, 30), new BigDecimal("-12.5")));
        transactionList.add(new Transaction("00007", "Transaction Name", new Date(119, 8, 22, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00008", "Transaction Name", new Date(119, 8, 25, 11, 23, 30), new BigDecimal("-12.5")));
        transactionList.add(new Transaction("00009", "Transaction Name", new Date(119, 9, 2, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00010", "Transaction Name", new Date(119, 9, 22, 11, 23, 30), new BigDecimal("-12.5")));
        transactionList.add(new Transaction("00011", "Transaction Name", new Date(119, 9, 23, 11, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00012", "Transaction Name", new Date(119, 9, 23, 13, 23, 30), new BigDecimal("12.5")));
        transactionList.add(new Transaction("00013", "Transaction Name", new Date(119, 9, 23, 115, 23, 30), new BigDecimal("12.5")));

        listener.onComplete(transactionList, null);
    }
}
