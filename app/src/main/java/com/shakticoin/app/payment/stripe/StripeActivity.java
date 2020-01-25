package com.shakticoin.app.payment.stripe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityPmntStripeBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class StripeActivity extends BaseWalletActivity {

    public static final String KEY_ORDER_NAME   = "ORDER_NAME";
    public static final String KEY_ORDER_AMOUNT = "ORDER_AMOUNT";
    public static final String KEY_ORDER_ID     = "ORDER_ID";
    public static final String KEY_TOKEN        = "TOKEN";

    private ActivityPmntStripeBinding binding;

    private int period = -1;
    private String packageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pmnt_stripe);
        binding.setLifecycleOwner(this);

        super.onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar), true);

        Intent intent = getIntent();
        period = intent.getIntExtra(CommonUtil.prefixed("period", this), -1);
        packageName = intent.getStringExtra(CommonUtil.prefixed("packageName", this));
        String orderName = intent.getStringExtra(CommonUtil.prefixed(KEY_ORDER_NAME, this));
        // TODO: test stuff - remove when data is available
        orderName = getString(R.string.pmnt_stripe_order_name, "M101");
        binding.orderName.setText(packageName);
        Double orderAmount = intent.getDoubleExtra(CommonUtil.prefixed(KEY_ORDER_AMOUNT, this), 0.0);
        // always format money according to US rules and currency
        NumberFormat formater = NumberFormat.getCurrencyInstance(Locale.US);
        formater.setCurrency(Currency.getInstance("USD"));
        switch (period) {
            case 1:
                binding.orderPeriod.setText(R.string.pmnt_opts_weekly);
                binding.orderAmount.setText(formater.format(orderAmount) + "/" + getString(R.string.pmnt_opts_week));
                break;
            case 2:
                binding.orderPeriod.setText(R.string.pmnt_opts_monthly);
                binding.orderAmount.setText(formater.format(orderAmount) + "/" + getString(R.string.pmnt_opts_month));
                break;
            case 3:
                binding.orderPeriod.setText(R.string.pmnt_opts_annual);
                binding.orderAmount.setText(formater.format(orderAmount) + "/" + getString(R.string.pmnt_opts_year));
                break;
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onMakePayment(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
//        Card card = binding.cardInputWidget.getCard();
//        if (card == null) {
//            binding.progressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(this, R.string.err_incorrect_card, Toast.LENGTH_SHORT).show();
//            return;
//        }

        Activity activity = this;
        setResult(RESULT_OK);
        finish();

//        Stripe stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
//        stripe.createToken(card, new ApiResultCallback<Token>() {
//            @Override
//            public void onSuccess(Token token) {
//                binding.progressBar.setVisibility(View.INVISIBLE);
//
//                Map<String, Object> params = new HashMap<>();
//                params.put("amount", 999);
//                params.put("currency", "usd");
//                params.put("description", "Example charge");
//                params.put("source", token.getId());

//                Intent resultIntent = new Intent();
//                resultIntent.putExtra(CommonUtil.prefixed(KEY_TOKEN, activity), token.getId());
//                Intent intent = activity.getIntent();
//                long orderId = intent.getLongExtra(CommonUtil.prefixed(KEY_ORDER_ID, activity), -1L);
//                if (orderId > 0) {
//                    resultIntent.putExtra(CommonUtil.prefixed(KEY_ORDER_ID, activity), orderId);
//                }
//                setResult(RESULT_OK, resultIntent);
//                finish();
//            }

//            @Override
//            public void onError(@NotNull Exception e) {
//                binding.progressBar.setVisibility(View.INVISIBLE);
//                Debug.logException(e);
//                Toast.makeText(activity,
//                        BuildConfig.DEBUG ? e.getMessage() : getString(R.string.err_unexpected),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
