package com.shakticoin.app.referral;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.referral.ReferralRepository;
import com.shakticoin.app.api.referral.model.EffortRate;
import com.shakticoin.app.databinding.FragmentEffortChartBinding;
import com.shakticoin.app.util.Debug;

import java.util.ArrayList;
import java.util.List;

import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_EMAIL;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_FACEBOOK;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_INSTAGRAM;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_LINKEDIN;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_PINTEREST;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_PLUS;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_SKYPE;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_TUMBLR;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_TWITTER;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_VK;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_SOURCE_WECHAT;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_STATUS_CONVERTED;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_STATUS_INFLUENCED;
import static com.shakticoin.app.api.referral.model.EffortRate.LEAD_STATUS_PROGRESSING;

public class EffortRatesChartFragment extends Fragment implements View.OnClickListener {
    private FragmentEffortChartBinding binding;

    private EffortChartAdapter chartAdapter;

    private int inactiveTextColor = 0;
    private int activeTextColor = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEffortChartBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        Context context = getContext();
        if (context != null) {
            Resources res = context.getResources();
            inactiveTextColor = res.getColor(R.color.effortRateProcessing);
            activeTextColor = res.getColor(android.R.color.white);
        }

        binding.chart.setHasFixedSize(true);
        binding.chart.setLayoutManager(new LinearLayoutManager(getActivity()));
        chartAdapter = new EffortChartAdapter();
        binding.chart.setAdapter(chartAdapter);

        ReferralRepository repository = new ReferralRepository();
        repository.getEffortRates(new OnCompleteListener<List<EffortRate>>() {
            @Override
            public void onComplete(List<EffortRate> value, Throwable error) {
                if (error != null) {
                    Debug.logException(error);
                    return;
                }

                if (value != null && value.size() > 0) {
                    for (EffortRate effortRate : value) {
                        chartAdapter.add(effortRate);
                    }
                }
            }
        });

        binding.categoryConverted.setOnClickListener(this);
        binding.categoryInfluenced.setOnClickListener(this);
        binding.categoryProcessing.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.categoryConverted:
                if (binding.categoryConverted.isChecked()) {
                    displayAllCategories();
                } else {
                    selectConverted();
                }
                break;
            case R.id.categoryInfluenced:
                if (binding.categoryInfluenced.isChecked()) {
                    displayAllCategories();
                } else {
                    selectInfluenced();
                }
                break;
            case R.id.categoryProcessing:
                if (binding.categoryProcessing.isChecked()) {
                    displayAllCategories();
                } else {
                    selectProcessing();
                }
                break;
        }
    }

    private void selectConverted() {
        binding.categoryConverted.setChecked(true);
        binding.categoryProcessing.setChecked(false);
        binding.categoryInfluenced.setChecked(false);
        binding.categoryConvertedLabel.setTextColor(activeTextColor);
        binding.categoryProcessingLabel.setTextColor(inactiveTextColor);
        binding.categoryInfluencedLabel.setTextColor(inactiveTextColor);
        chartAdapter.setDisplayStage(LEAD_STATUS_CONVERTED);
    }

    private void selectInfluenced() {
        binding.categoryConverted.setChecked(false);
        binding.categoryProcessing.setChecked(false);
        binding.categoryInfluenced.setChecked(true);
        binding.categoryConvertedLabel.setTextColor(inactiveTextColor);
        binding.categoryProcessingLabel.setTextColor(inactiveTextColor);
        binding.categoryInfluencedLabel.setTextColor(activeTextColor);
        chartAdapter.setDisplayStage(LEAD_STATUS_INFLUENCED);
    }

    private void selectProcessing() {
        binding.categoryConverted.setChecked(false);
        binding.categoryProcessing.setChecked(true);
        binding.categoryInfluenced.setChecked(false);
        binding.categoryConvertedLabel.setTextColor(inactiveTextColor);
        binding.categoryProcessingLabel.setTextColor(activeTextColor);
        binding.categoryInfluencedLabel.setTextColor(inactiveTextColor);
        chartAdapter.setDisplayStage(LEAD_STATUS_PROGRESSING);
    }

    private void displayAllCategories() {
        binding.categoryConverted.setChecked(false);
        binding.categoryProcessing.setChecked(false);
        binding.categoryInfluenced.setChecked(false);
        binding.categoryConvertedLabel.setTextColor(activeTextColor);
        binding.categoryProcessingLabel.setTextColor(activeTextColor);
        binding.categoryInfluencedLabel.setTextColor(activeTextColor);
        chartAdapter.setDisplayStage(-1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private EffortChartLine chartLine;
        private View topDivider;
        private View bottomDivider;
        private int displayStage = -1;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            chartLine = itemView.findViewById(R.id.chartLine);
            topDivider = itemView.findViewById(R.id.dividerTop);
            bottomDivider = itemView.findViewById(R.id.dividerBottom);
        }

        public void setIcon(int resId) {
            icon.setImageResource(resId);
        }

        void setEffortRate(EffortRate effortRate) {
            itemView.setTag(effortRate);
            switch (displayStage) {
                case LEAD_STATUS_CONVERTED:
                    chartLine.setConvertedPercent(effortRate.getStagePercent(LEAD_STATUS_CONVERTED));
                    chartLine.setLeadsNumber(effortRate.getStageTotal(LEAD_STATUS_CONVERTED));
                    chartLine.setProcessingPercent(0);
                    chartLine.setInfluencedPercent(0);
                    break;
                case LEAD_STATUS_PROGRESSING:
                    chartLine.setConvertedPercent(0);
                    chartLine.setInfluencedPercent(0);
                    chartLine.setProcessingPercent(effortRate.getStagePercent(LEAD_STATUS_PROGRESSING));
                    chartLine.setLeadsNumber(effortRate.getStageTotal(LEAD_STATUS_PROGRESSING));
                    break;
                case LEAD_STATUS_INFLUENCED:
                    chartLine.setInfluencedPercent(effortRate.getStagePercent(LEAD_STATUS_INFLUENCED));
                    chartLine.setLeadsNumber(effortRate.getStageTotal(LEAD_STATUS_INFLUENCED));
                    chartLine.setConvertedPercent(0);
                    chartLine.setProcessingPercent(0);
                    break;
                default:
                    chartLine.setConvertedPercent(effortRate.getStagePercent(LEAD_STATUS_CONVERTED));
                    chartLine.setProcessingPercent(effortRate.getStagePercent(LEAD_STATUS_PROGRESSING));
                    chartLine.setInfluencedPercent(effortRate.getStagePercent(LEAD_STATUS_INFLUENCED));
                    chartLine.setLeadsNumber(0);
            }
            topDivider.setVisibility(View.VISIBLE);
            bottomDivider.setVisibility(View.VISIBLE);
        }

        void setLineFirst() {
            topDivider.setVisibility(View.INVISIBLE);
        }

        void setLineLast() {
            bottomDivider.setVisibility(View.INVISIBLE);
        }

        void setDisplayStage(int stage) {
            displayStage = stage;
        }
    }

    class EffortChartAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<EffortRate> dataset = new ArrayList<>();
        private int displayStage = -1; // default value - display all

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_effort_chart, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EffortRate effortRate = dataset.get(position);
            switch (effortRate.getLeadSource()) {
                case LEAD_SOURCE_EMAIL:
                    holder.setIcon(R.drawable.ic_media_email);
                    break;
                case LEAD_SOURCE_FACEBOOK:
                    holder.setIcon(R.drawable.ic_media_facebook);
                    break;
                case LEAD_SOURCE_INSTAGRAM:
                    holder.setIcon(R.drawable.ic_media_instagram);
                    break;
                case LEAD_SOURCE_LINKEDIN:
                    holder.setIcon(R.drawable.ic_media_linkedin);
                    break;
                case LEAD_SOURCE_PINTEREST:
                    holder.setIcon(R.drawable.ic_media_pinterest);
                    break;
                case LEAD_SOURCE_PLUS:
                    holder.setIcon(R.drawable.ic_media_plus);
                    break;
                case LEAD_SOURCE_SKYPE:
                    holder.setIcon(R.drawable.ic_media_skype);
                    break;
                case LEAD_SOURCE_TUMBLR:
                    holder.setIcon(R.drawable.ic_media_tumblr);
                    break;
                case LEAD_SOURCE_TWITTER:
                    holder.setIcon(R.drawable.ic_media_twitter);
                    break;
                case LEAD_SOURCE_WECHAT:
                    holder.setIcon(R.drawable.ic_media_wechat);
                    break;
                case LEAD_SOURCE_VK:
                    holder.setIcon(R.drawable.ic_media_vk);
                    break;
                default:
                    holder.setIcon(R.drawable.ic_media_other);
                    break;
            }
            holder.setDisplayStage(displayStage);
            holder.setEffortRate(effortRate);
            if (position == 0) holder.setLineFirst();
            if (position == dataset.size() - 1) holder.setLineLast();
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public void add(@NonNull EffortRate effortRate) {
            dataset.add(effortRate);
            this.notifyItemInserted(dataset.size() - 1);
        }

        void setDisplayStage(int stage) {
            displayStage = stage;
            notifyDataSetChanged();
        }
    }
}
