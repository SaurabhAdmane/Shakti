package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.api.kyc.KycDocType;
import com.shakticoin.app.databinding.FragmentKycDoctypeBinding;

import java.util.List;

public class KycDoctypeFragment extends Fragment {
    private FragmentKycDoctypeBinding binding;
    private PersonalViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKycDoctypeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(requireActivity()).get(PersonalViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        binding.documentType.setHasFixedSize(false);
        binding.documentType.setLayoutManager(new LinearLayoutManager(getContext()));
        KycCategory kycCategory = viewModel.selectedCategory.getValue();
        if (kycCategory != null) {
            binding.label.setText(kycCategory.getName().replace("\n", " "));
            List<KycDocType> docTypes = kycCategory.getDoc_types();
            binding.documentType.setAdapter(new DocumentTypeAdapter(docTypes));
        }

        // FIXME: temporarily disabled because not implemented
        binding.selectUpload.setEnabled(false);
        binding.selectSkip.setEnabled(false);

        return v;
    }

    class DocumentTypeAdapter extends RecyclerView.Adapter<DocumentTypeAdapter.DocumentTypeViewHolder> {
        private List<KycDocType> docTypeList;
        private int selectedPosition = -1;

        DocumentTypeAdapter(List<KycDocType> data) {
            docTypeList = data;
        }

        @NonNull
        @Override
        public DocumentTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DocumentTypeViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_type, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DocumentTypeViewHolder holder, int position) {
            KycDocType docType = docTypeList.get(position);
            holder.text.setText(docType.getName());
            holder.checkbox.setChecked(selectedPosition == position);
        }

        @Override
        public int getItemCount() {
            return docTypeList != null ? docTypeList.size() : 0;
        }

        class DocumentTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView text;
            public CheckBox checkbox;

            DocumentTypeViewHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.text);
                checkbox = itemView.findViewById(R.id.checkbox);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                selectedPosition = getAdapterPosition();
                viewModel.kycDocumentType = docTypeList.get(selectedPosition);
                notifyDataSetChanged();
            }
        }
    }
}
