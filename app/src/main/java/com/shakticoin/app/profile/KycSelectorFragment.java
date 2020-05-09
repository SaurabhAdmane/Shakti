package com.shakticoin.app.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.ProgressBarModel;
import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.databinding.FragmentProfileKycSelectorBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.CheckableRoundButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KycSelectorFragment extends Fragment {
    public static final String TAG = KycSelectorFragment.class.getSimpleName();

    private FragmentProfileKycSelectorBinding binding;
    private KycSelectorViewModel viewModel;
    private ProgressBarModel activityViewModel;

    private ArrayList<KycCategory> kycCategories;
    private SelectorAdapter adapter;

    private KYCRepository kycRepository = new KYCRepository();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(KycSelectorViewModel.class);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof KycActivity) {
                activityViewModel = ViewModelProviders.of(activity).get(KycCommonViewModel.class);
            } else if (activity instanceof ProfileActivity) {
                activityViewModel = ViewModelProviders.of(activity).get(PersonalViewModel.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileKycSelectorBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        binding.selector.setHasFixedSize(true);
        binding.selector.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SelectorAdapter();
        binding.selector.setAdapter(adapter);

        binding.doNext.setOnClickListener(v1 -> onNext());

        if (activityViewModel != null) activityViewModel.getProgressBarTrigger().set(true);
        kycRepository.getKycCategories(new OnCompleteListener<List<KycCategory>>() {
            @Override
            public void onComplete(List<KycCategory> categories, Throwable error) {
                if (activityViewModel != null) activityViewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    Toast.makeText(getContext(), Debug.getFailureMsg(getContext(), error), Toast.LENGTH_LONG).show();
                    return;
                }
                kycCategories = new ArrayList<>(categories);
                if (categories.size() > 0) {
                    adapter = new SelectorAdapter(categories);
                    binding.selector.setAdapter(adapter);
                    viewModel.selectedCategory.setValue(categories.get(0));
                }
            }
        });

        viewModel.selectedCategory.observe(this, kycCategory -> {
            binding.categoryName.setText(kycCategory.getName());
            binding.categoryDescription.setText(kycCategory.getDescription());
            // TODO: must be fixed at some point
            // we select image based on KYC category ID that may be changed
            switch (kycCategory.getId()) {
                case 1:
                    binding.utilityIcon.setVisibility(View.VISIBLE);
                    binding.propertyIcon.setVisibility(View.INVISIBLE);
                    binding.govIcon.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    binding.utilityIcon.setVisibility(View.INVISIBLE);
                    binding.propertyIcon.setVisibility(View.VISIBLE);
                    binding.govIcon.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    binding.utilityIcon.setVisibility(View.INVISIBLE);
                    binding.propertyIcon.setVisibility(View.INVISIBLE);
                    binding.govIcon.setVisibility(View.VISIBLE);
                    break;
            }
        });

        return v;
    }

    private void onNext() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof KycActivity) {
                KycCommonViewModel activityViewModel = ViewModelProviders.of(activity).get(KycCommonViewModel.class);
                activityViewModel.kycCategories = kycCategories;
                activityViewModel.kycCategory.setValue(viewModel.selectedCategory.getValue());
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragment, new KycDoctypeFragment())
                        .addToBackStack(KycDoctypeFragment.class.getSimpleName())
                        .commit();
            } else {
                Intent intent = new Intent(getActivity(), KycActivity.class);
                Context context = ShaktiApplication.getContext();
                intent.putExtra(CommonUtil.prefixed("KYC_CATEGORY", context), viewModel.selectedCategory.getValue());
                intent.putParcelableArrayListExtra(CommonUtil.prefixed("KYC_CATEGORY_LIST", context), kycCategories);
                startActivity(intent);
            }
        }
    }

    class SelectorViewHolder extends RecyclerView.ViewHolder {
        private CheckableRoundButton button;

        SelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.selectorButton);
            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    KycCategory selectedCategory = (KycCategory) buttonView.getTag();
                    viewModel.selectedCategory.setValue(selectedCategory);

                    // when an button is checked we should make unchecked the rest
                    int items = Objects.requireNonNull(binding.selector.getAdapter()).getItemCount();
                    for (int i = 0; i < items; i++) {
                        SelectorViewHolder vh = (SelectorViewHolder) binding.selector.findViewHolderForAdapterPosition(i);
                        if (vh != null) {
                            KycCategory category = (KycCategory) vh.button.getTag();
                            vh.setChecked(selectedCategory.getId().compareTo(category.getId()) == 0);
                        }
                    }
                }
            });
        }

        public void setData(KycCategory category) {
            button.setTag(category);
        }

        void setChecked(boolean checked) {
            button.setChecked(checked);
        }
    }

    class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder> {
        private List<KycCategory> categories;

        SelectorAdapter() {}

        SelectorAdapter(List<KycCategory> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SelectorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SelectorViewHolder holder, int position) {
            holder.setData(categories.get(position));
            KycCategory selectedCategory = viewModel.selectedCategory.getValue();
            if (selectedCategory != null) {
                holder.setChecked(selectedCategory.getId().compareTo(categories.get(position).getId()) == 0);
            }
        }

        @Override
        public int getItemCount() {
            return categories != null ? categories.size() : 0;
        }
    }
}
