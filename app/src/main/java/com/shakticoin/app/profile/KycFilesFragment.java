package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.databinding.FragmentKycFilesBinding;

import java.io.File;
import java.io.FilenameFilter;
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

        // if this variable become true then we need to update the list and reset it back to false
        viewModel.updateList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    adapter = new KycFilesAdapter();
                    binding.list.setAdapter(adapter);
                    ((ObservableBoolean) sender).set(false);
                }
            }
        });

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
                    String categoryName = category.getName().replace("\n", " ");
                    adapter.addHeader(categoryName, category.getId());
                    String[] files = categoryDir.list();
                    if (files != null) {
                        for (String fileName : files) {
                            String[] parts = fileName.split("_");
                            String documentTypeId = "";
                            // documentTypeId may contains underscores too, so we need to build it
                            // from parts except first and last.
                            if (parts.length > 2) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < parts.length-1; i++) {
                                    if (sb.length() > 0) sb.append("_");
                                    sb.append(parts[i]);
                                }
                                documentTypeId = sb.toString();
                            }
                            adapter.addItem(fileName, category.getId(), documentTypeId);
                        }
                    }
                }
            }
        }
    }

    private void removeFile(@NonNull KycDocumentObject doc, int position) {
        File categoryDir = new File(imagesDir, Integer.toString(doc.getCategoryId()));
        if (categoryDir.exists()) {
            File[] files = categoryDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.equals(doc.getFileName());
                }
            });
            if (files != null) {
                for (File file : files) file.delete();
                adapter.removeItem(position);
                // if the category is empty after deleting the last file we better to delete
                // the category too
                File[] remainingFiles = categoryDir.listFiles();
                if (remainingFiles != null && remainingFiles.length == 0) {
                    if (categoryDir.delete() && position > 0) {
                        // basically, if we remove last file in category then the header must have
                        // an index equals to position - 1
                        adapter.removeItem(position - 1);
                    }
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageButton action;
        private KycDocumentObject doc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            action = itemView.findViewById(R.id.action);
            if (action != null) {
                action.setOnClickListener(v -> {
                    if (doc != null) {
                        removeFile(doc, getAdapterPosition());
                    }
                });
            }
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public void setDocument(KycDocumentObject doc) {
            this.doc = doc;
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
            Object obj = item.get();
            if (obj instanceof KycDocumentObject) {
                holder.setDocument((KycDocumentObject) obj);
            }
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

        void addItem(String fileName, int categoryId, String documentTypeId) {
            KycDocumentObject document = new KycDocumentObject(fileName, categoryId, documentTypeId);
            data.add(new KycDocumentItem<>(document));
            notifyItemInserted(data.size()-1);
        }

        void removeItem(int position) {
            data.remove(position);
            notifyItemRemoved(position);
        }

        void clear() {
            data.clear();
            notifyDataSetChanged();
        }
    }
}
