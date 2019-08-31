package com.shakticoin.app.payment.stripe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import com.shakticoin.app.BuildConfig;
import com.shakticoin.app.R;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class StripeActivity extends AppCompatActivity {

    public static final String KEY_ORDER_NAME   = "ORDER_NAME";
    public static final String KEY_ORDER_AMOUNT = "ORDER_AMOUNT";
    public static final String KEY_ORDER_ID     = "ORDER_ID";
    public static final String KEY_TOKEN        = "TOKEN";

    private CardInputWidget ctrlCardInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmnt_stripe);

        TextView ctrlOrderName = findViewById(R.id.orderName);
        TextView ctrlOrderAmount = findViewById(R.id.orderAmount);
        ctrlCardInput = findViewById(R.id.card_input_widget);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String orderName = intent.getStringExtra(CommonUtil.prefixed(KEY_ORDER_NAME, this));
        ctrlOrderName.setText(orderName);
        Double orderAmount = intent.getDoubleExtra(CommonUtil.prefixed(KEY_ORDER_AMOUNT, this), 0.0);
        // always format money according to US rules and currency
        NumberFormat formater = NumberFormat.getCurrencyInstance(Locale.US);
        formater.setCurrency(Currency.getInstance("USD"));
        ctrlOrderAmount.setText(formater.format(orderAmount));
    }

    public void onMakePayment(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Card card = ctrlCardInput.getCard();
        if (card == null) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.err_incorrect_card, Toast.LENGTH_SHORT).show();
            return;
        }

        Activity activity = this;

        Stripe stripe = new Stripe(this, getString(R.string.stripe_pub_key));
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                progressBar.setVisibility(View.INVISIBLE);
                Debug.logException(error);
                Toast.makeText(activity,
                        BuildConfig.DEBUG ? error.getMessage() : getString(R.string.err_unexpected),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Token token) {
                progressBar.setVisibility(View.INVISIBLE);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(CommonUtil.prefixed(KEY_TOKEN, activity), token.getId());
                Intent intent = activity.getIntent();
                long orderId = intent.getLongExtra(CommonUtil.prefixed(KEY_ORDER_ID, activity), -1L);
                if (orderId > 0) {
                    resultIntent.putExtra(CommonUtil.prefixed(KEY_ORDER_ID, activity), orderId);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
