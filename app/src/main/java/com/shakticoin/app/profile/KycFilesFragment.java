package com.shakticoin.app.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.databinding.FragmentKycFilesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class KycFilesFragment extends Fragment {
    private FragmentKycFilesBinding binding;
    private File imagesDir;

    private KycCommonViewModel viewModel;

    private KycFilesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesDir = ShaktiApplication.getContext().getExternalFilesDir("kyc");
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(KycCommonViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycFilesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        binding.list.setHasFixedSize(false);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KycFilesAdapter();
        binding.list.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        getList();
    }

    /**
     * <p>Images prepared for upload are stored in external private storage directory in
     * folder "kyc". Each KYC category has own directory with numeric name that equals to
     * the corresponding category ID.</p>
     *
     * <p>Each file has name that follow:<br/>
     * doc_{documentTypeId}_{timestamp}.jpg</p>
     */
    private void getList() {
        if (viewModel.kycCategories != null && viewModel.kycCategories.size() > 0) {
            for (KycCategory category : viewModel.kycCategories) {
                File categoryDir = new File(imagesDir, category.getId().toString());
                if (categoryDir.exists()) {
                    adapter.addHeader(category.getName(), category.getId());
                    String[] files = categoryDir.list();
                    if (files != null) {
                        for (String fileName : files) {
                            String[] parts = fileName.split("_");
                            int documentTypeId = 0;
                            if (TextUtils.isDigitsOnly(parts[1])) {
                                documentTypeId = Integer.valueOf(parts[1]);
                            }
                            adapter.addItem(fileName, category.getId(), documentTypeId);
                        }
                    }
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }

    class KycFilesAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<KycDocumentItem> data = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            KycDocumentItem item = data.get(position);
            holder.setText(item.getTitle());
        }

        @Override
        public int getItemViewType(int position) {
            KycDocumentItem item = data.get(position);
            return item.get() instanceof  KycDocumentHeader ?
                    R.layout.item_kyc_files_header : R.layout.item_kyc_files_file;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        void addHeader(String categoryTitle, int categoryId) {
            KycDocumentHeader documentHeader = new KycDocumentHeader(categoryTitle, categoryId);
            data.add(new KycDocumentItem<>(documentHeader));
            notifyItemInserted(data.size()-1);
        }

        void addItem(String fileName, int categoryId, int documentTypeId) {
            KycDocumentObject document = new KycDocumentObject(fileName, categoryId, documentTypeId);
            data.add(new KycDocumentItem<>(document));
            notifyItemInserted(data.size()-1);
        }

        void clear() {
            data.clear();
            notifyDataSetChanged();
        }
    }
}
