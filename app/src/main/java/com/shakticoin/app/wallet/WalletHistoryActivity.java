package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.onboard.OnboardRepository;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.Transaction;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletHistoryBinding;
import com.shakticoin.app.room.AppDatabase;
import com.shakticoin.app.room.LockStatusDao;
import com.shakticoin.app.room.entity.LockStatus;
import com.shakticoin.app.util.Debug;
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
    private WalletRepository walletRepository;
    private OnboardRepository onboardRepository = new OnboardRepository();
    private KYCRepository kycRepository = new KYCRepository();
    private TransactionAdapter adapter;
    private WalletModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_history);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(WalletModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title), true);

        walletRepository = new WalletRepository();

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

        String walletBytes = walletRepository.getExistingWallet();
        if (walletBytes != null) {
            viewModel.isProgressBarActive.set(true);
            walletRepository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal value, Throwable error) {
                    viewModel.isProgressBarActive.set(false);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    viewModel.balance.set(value);
                }
            });

            walletRepository.getTransactions(new OnCompleteListener<List<Transaction>>() {
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

        // check wallet lock status and display action buttons if unlocked
        new CheckWalletLocked(getSupportFragmentManager()).execute();
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

        viewModel.isProgressBarActive.set(true);
        String walletBytes = walletRepository.getExistingWallet();
        if (walletBytes == null) {
            // we need to create a new wallet
            onboardRepository.createWallet(null, new OnCompleteListener<String>() {
                @Override
                public void onComplete(String walletBytes, Throwable error) {
                    viewModel.isProgressBarActive.set(false);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    walletRepository.storeWallet(walletBytes);
                    getWalletBalance();
                }
            });
        } else {
            walletRepository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal balance, Throwable error) {
                    viewModel.isProgressBarActive.set(false);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        if (error instanceof SessionException) {
                            Session.setWalletPassphrase(null);
                            getWalletBalance();
                        }
                        return;
                    }
                    viewModel.balance.set(balance);

                    // if we get the balance successfully then we can request transactions
                    walletRepository.getTransactions(new OnCompleteListener<List<Transaction>>() {
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

    public void onShowDetails(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
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

    static class CheckWalletLocked extends AsyncTask<Void, Void, Boolean> {
        FragmentManager fragmentManager;

        CheckWalletLocked(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LockStatusDao lockStatuses = AppDatabase.getDatabase(ShaktiApplication.getContext()).lockStatusDao();
            LockStatus status = lockStatuses.getWalletKYCLockStatus();
            return status != null && "UNLOCKED".equals(status.getStatus());
        }

        @Override
        protected void onPostExecute(Boolean unlocked) {
            if (unlocked) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.wallet_actions, new WalletActionsFragment())
                        .commit();
            } else {
                new KYCRepository().isWalletUnlocked(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean unlocked, Throwable error) {
                        fragmentManager
                                .beginTransaction()
                                .add(R.id.wallet_actions, unlocked ?
                                        new WalletActionsFragment() :
                                        new KycVerificationRequiredFragment())
                                .commit();
                    }
                });
            }
        }
    }

}
