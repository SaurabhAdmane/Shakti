package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.Transaction;
import com.shakticoin.app.api.wallet.TransferModelResponse;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletHistoryBinding;
import com.shakticoin.app.payment.DialogPaySXE;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.FormatUtil;
import com.shakticoin.app.util.RecyclerViewHeader;
import com.shakticoin.app.util.RecyclerViewItem;
import com.shakticoin.app.widget.DrawerActivity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WalletHistoryActivity extends DrawerActivity {
    private ActivityWalletHistoryBinding binding;
    private WalletRepository repository;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_history);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title), true);

        repository = new WalletRepository();

        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.addItemDecoration(new ItemDecoration(this));
        adapter = new TransactionAdapter(new ArrayList<>());
        binding.list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Activity activity = this;

        getWalletBalance();
        String walletBytes = repository.getExistingWallet();
        if (walletBytes != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            repository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal value, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    binding.balance.setText(FormatUtil.formatCoinAmount(value));
                }
            });

            repository.getTransactions(new OnCompleteListener<List<Transaction>>() {
                @Override
                public void onComplete(List<Transaction> transactions, Throwable error) {
                    if (error != null) {
                        Debug.logException(error);
                        return;
                    }
                    binding.emptyListMsg.setVisibility(transactions.size() == 0 ? View.VISIBLE : View.GONE);
                    adapter.addAll(transactions);
                }
            });
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    public void getWalletBalance() {
        if (Session.getWalletPassphrase() == null) {
            DialogPass.getInstance(new DialogPass.OnPassListener() {
                @Override
                public void onPassEntered(@Nullable String password) {
                    if (!TextUtils.isEmpty(password)) {
                        Session.setWalletPassphrase(password);
                    }
                    getWalletBalance();
                }
            }).show(getSupportFragmentManager(), DialogPass.class.getName());
            return;
        }

        Activity activity = this;

        binding.progressBar.setVisibility(View.VISIBLE);
        String walletBytes = repository.getExistingWallet();
        if (walletBytes == null) {
            // we need to create a new wallet
            repository.createWallet(null, new OnCompleteListener<String>() {
                @Override
                public void onComplete(String walletBytes, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    repository.storeWallet(walletBytes);
                    getWalletBalance();
                }
            });
        } else {
            repository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal balance, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        if (error instanceof SessionException) {
                            Session.setWalletPassphrase(null);
                            getWalletBalance();
                        }
                        return;
                    }
                    binding.balance.setText(FormatUtil.formatCoinAmount(balance));

                    // if we get the balance successfully then we can request transactions
                    repository.getTransactions(new OnCompleteListener<List<Transaction>>() {
                        @Override
                        public void onComplete(List<Transaction> transactions, Throwable error) {
                            if (error != null) {
                                Debug.logException(error);
                                return;
                            }
                            binding.emptyListMsg.setVisibility(transactions.size() == 0 ? View.VISIBLE : View.GONE);
                            adapter.addAll(transactions);
                        }
                    });
                }
            });
        }
    }

    public void onOpenMainWallet(View v) {
        startActivity(new Intent(this, WalletActivity.class));
    }

    public void onOpenWalletAdmin(View v) {
        startActivity(new Intent(this, WalletAdminActivity.class));
    }

    public void onPay(View v) {
        DialogPaySXE.getInstance((payee, amount, message)
                -> makeSxePayment(payee, amount, message)).show(getSupportFragmentManager(), DialogPaySXE.class.getSimpleName());
    }

    public void onReceive(View v) {
        // TODO: for now we are able only to send coins to a wallet address, no mapping ID to address.
        // This rise another problem because we have no way to know what is wallet address is.
        // So, I use this button to advertise his own wallet address to a user that he can share with
        // a payer. But this is temporarily.
        final String[] address = new String[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("You wallet address")
                .setNegativeButton("Close", null)
                .setNeutralButton("Copy to cliboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm =
                                (ClipboardManager) ShaktiApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null && !TextUtils.isEmpty(address[0])) {
                            ClipData clip = ClipData.newPlainText("wallet address", address[0]);
                            cm.setPrimaryClip(clip);
                        }
                    }
                });
        binding.progressBar.setVisibility(View.VISIBLE);
        final Activity activity = this;
        repository.getAddress(new OnCompleteListener<String>() {
            @Override
            public void onComplete(String walletAddress, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null && TextUtils.isEmpty(walletAddress)) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }
                address[0] = walletAddress;
                builder.setMessage(walletAddress);
                builder.create().show();
            }
        });
    }

    public void onShowDetails(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    private void makeSxePayment(@NonNull String payee, @NonNull BigDecimal amount, String message) {
        binding.progressBar.setVisibility(View.VISIBLE);
        final Activity activity = this;
        long toshiAmount = amount.multiply(BigDecimal.valueOf(Constants.TOSHI_FACTOR)).longValue();
        repository.transfer(payee, toshiAmount, message, new OnCompleteListener<TransferModelResponse>() {
            @Override
            public void onComplete(TransferModelResponse response, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionView;
        TextView timeView;
        TextView amountView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.description);
            timeView = itemView.findViewById(R.id.time);
            amountView = itemView.findViewById(R.id.amount);
        }
    }

    class TransactionAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<RecyclerViewItem> dataset;

        NumberFormat currencyFormat;
        DateFormat timeFormat;
        int positiveAmountColor;
        int negativeAmountColor;

        TransactionAdapter(@NonNull List<RecyclerViewItem> dataset) {
            this.dataset = dataset;
            currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            timeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            positiveAmountColor = Color.parseColor("#D8C295");
            negativeAmountColor = Color.parseColor("#FFFFFF");
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecyclerViewItem item = dataset.get(position);
            switch (item.getItemType()) {
                case HEADER:
                    holder.descriptionView.setText(item.toString());
                    break;
                case ITEM:
                    holder.descriptionView.setText(((Transaction)item).getDescription());
                    holder.timeView.setText(timeFormat.format(((Transaction)item).getDate()));
                    BigDecimal transactionAmount = ((Transaction)item).getAmount();
                    holder.amountView.setText(String.format(Locale.US, "%1$+.2f", transactionAmount));
                    holder.amountView.setTextColor(transactionAmount.signum() > 0 ? positiveAmountColor : negativeAmountColor);
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            RecyclerViewItem item = dataset.get(position);
            switch (item.getItemType()) {
                case ITEM:
                    return R.layout.item_transaction;
                case HEADER:
                default:
                    return R.layout.item_transaction_header;
            }
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        /** Sort by date and split into date buckets */
        void addAll(List<Transaction> transactionList) {
            if (transactionList == null) return;

            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();

            Map<String, List<Transaction>> buckets = new HashMap<>();
            for (Transaction transaction : transactionList) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(transaction.getDate().getTime());
                String bucketTitle = months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
                List<Transaction> currentBucket = buckets.get(bucketTitle);
                if (currentBucket == null) {
                    currentBucket = new ArrayList<>();
                    buckets.put(bucketTitle, currentBucket);
                }
                currentBucket.add(transaction);
            }

            List<RecyclerViewItem> finalList = new ArrayList<>();
            for (String bucketTitle : buckets.keySet()) {
                finalList.add(new RecyclerViewHeader(bucketTitle));
                List<Transaction> currentBucket = buckets.get(bucketTitle);
                if (currentBucket != null && currentBucket.size() > 0) {
                    finalList.addAll(currentBucket);
                }
            }
            dataset.addAll(finalList);

            notifyDataSetChanged();
        }
    }

    class ItemDecoration extends RecyclerView.ItemDecoration {
        private Paint paint;
        private final Rect mBounds = new Rect();
        private float lineWidth;

        ItemDecoration(Context context) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1/*1dp*/, metrics);

            paint = new Paint();
            paint.setColor(resources.getColor(R.color.colorGrey3));
            paint.setStrokeWidth(lineWidth);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            c.save();
            int left;
            int right;

            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                c.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            left += (lineWidth/2);
            right -= (lineWidth/2);

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = mBounds.top;
                c.drawLine(left, bottom, right, bottom, paint);
                if (child instanceof LinearLayout) {
                    c.drawLine(left, top, left, bottom, paint);
                    c.drawLine(right, bottom, right, top, paint);
                }
            }
            c.restore();
        }

    }
}
