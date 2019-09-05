package com.shakticoin.app.referral;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.referral.ReferralRepository;
import com.shakticoin.app.api.referral.model.EffortRate;
import com.shakticoin.app.databinding.FragmentEffortChartBinding;
import com.shakticoin.app.util.Debug;

import java.util.ArrayList;
import java.util.List;

public class EffortRatesChartFragment extends Fragment {
    private FragmentEffortChartBinding binding;

    private EffortChartAdapter chartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEffortChartBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

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

        return v;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private EffortChartLine chartLine;
        private View topDivider;
        private View bottomDivider;

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
            chartLine.setConvertedPercent(effortRate.getStagePercent(EffortRate.LEAD_STATUS_CONVERTED));
            chartLine.setProcessingPercent(effortRate.getStagePercent(EffortRate.LEAD_STATUS_PROGRESSING));
            chartLine.setInfluencedPercent(effortRate.getStagePercent(EffortRate.LEAD_STATUS_INFLUENCED));
            topDivider.setVisibility(View.VISIBLE);
            bottomDivider.setVisibility(View.VISIBLE);
        }

        void setLineFirst() {
            topDivider.setVisibility(View.INVISIBLE);
        }

        void setLineLast() {
            bottomDivider.setVisibility(View.INVISIBLE);
        }
    }

    class EffortChartAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<EffortRate> dataset = new ArrayList<>();

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
                case EffortRate.LEAD_SOURCE_EMAIL:
                    holder.setIcon(R.drawable.ic_media_email);
                    break;
                case EffortRate.LEAD_SOURCE_FACEBOOK:
                    holder.setIcon(R.drawable.ic_media_facebook);
                    break;
                case EffortRate.LEAD_SOURCE_INSTAGRAM:
                    holder.setIcon(R.drawable.ic_media_instagram);
                    break;
                case EffortRate.LEAD_SOURCE_LINKEDIN:
                    holder.setIcon(R.drawable.ic_media_linkedin);
                    break;
                case EffortRate.LEAD_SOURCE_PINTEREST:
                    holder.setIcon(R.drawable.ic_media_pinterest);
                    break;
                case EffortRate.LEAD_SOURCE_PLUS:
                    holder.setIcon(R.drawable.ic_media_plus);
                    break;
                case EffortRate.LEAD_SOURCE_SKYPE:
                    holder.setIcon(R.drawable.ic_media_skype);
                    break;
                case EffortRate.LEAD_SOURCE_TUMBLR:
                    holder.setIcon(R.drawable.ic_media_tumblr);
                    break;
                case EffortRate.LEAD_SOURCE_TWITTER:
                    holder.setIcon(R.drawable.ic_media_twitter);
                    break;
                case EffortRate.LEAD_SOURCE_WECHAT:
                    holder.setIcon(R.drawable.ic_media_wechat);
                    break;
                case EffortRate.LEAD_SOURCE_VK:
                    holder.setIcon(R.drawable.ic_media_vk);
                    break;
                default:
                    holder.setIcon(R.drawable.ic_media_other);
                    break;
            }
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
    }
}
