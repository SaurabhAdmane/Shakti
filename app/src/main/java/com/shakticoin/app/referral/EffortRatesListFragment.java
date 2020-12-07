package com.shakticoin.app.referral;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.shakticoin.app.R;
import com.shakticoin.app.api.bounty.Referral;
import com.shakticoin.app.databinding.FragmentEffortListBinding;

import java.util.ArrayList;

public class EffortRatesListFragment extends Fragment {
    private FragmentEffortListBinding binding;
    private LeadListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEffortListBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LeadListAdapter();
        binding.list.setAdapter(adapter);

//        ReferralRepository repository = new ReferralRepository();
//        repository.setLifecycleOwner(getViewLifecycleOwner());
//        repository.getReferrals(null, new OnCompleteListener<List<Referral>>() {
//            @Override
//            public void onComplete(List<Referral> referrals, Throwable error) {
//                if (error != null) {
//                    Debug.logException(error);
//                    return;
//                }
//
//                if (referrals != null && referrals.size() > 0) {
//                    for (Referral referral : referrals) {
//                        adapter.add(referral);
//                    }
//                }
//            }
//        });
        return v;
    }

    class LeadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Object> dataset = new ArrayList<>();
        private ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return viewType == R.layout.item_lead ? new ViewHolderLead(v) : new ViewHolderSectionTitle(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Object item = dataset.get(position);
            if (item instanceof Referral && holder instanceof ViewHolderLead) {
                Referral referral = (Referral) item;
                String abbr = null;
                String firstName = referral.getFirstName();
                if (firstName != null) {
                    abbr = firstName.substring(0, 1);
                }
                String lastName = referral.getLastName();
                abbr += lastName.substring(0, 1);

                ((ViewHolderLead) holder).setFullName(referral.toString());
                ((ViewHolderLead) holder).setEmailAddress(referral.getEmail());
                ((ViewHolderLead) holder).setTelecomNumber(referral.getPhone());
                // TODO: at the time of writing back API does not provide a URL to an avatar picture
                Context context = getContext();
                ((ViewHolderLead) holder).generateAvatar(abbr.toUpperCase(),
                        context != null ? getResources().getColor(R.color.avatarDefault) : colorGenerator.getRandomColor());

            } else if (item instanceof String && holder instanceof ViewHolderSectionTitle){
                // this is a string containing first letter for section header
                ((ViewHolderSectionTitle) holder).setTitle((String) item, position == 0);
            }
        }

        @Override
        public int getItemViewType(int position) {
            Object item = dataset.get(position);
            return item instanceof Referral ? R.layout.item_lead : R.layout.item_lead_section;
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public void add(@NonNull Referral referral) {
            String firstLetter = referral.toString().substring(0, 1);

            // referrals added from a sorted list
            int firstSectionPosition = 0;
            for (int i = 0; i < dataset.size(); i++) {
                Object item = dataset.get(i);
                if (item instanceof String && item.equals(firstLetter)) {
                    firstSectionPosition = i;
                    break;
                }

                if (i == dataset.size() - 1) {
                    dataset.add(firstLetter);
                    notifyItemInserted(dataset.size() - 1);
                    firstSectionPosition = dataset.size() - 1;
                    break;
                }
            }

            if (dataset.size() == 0) {
                dataset.add(firstLetter);
                notifyItemInserted(firstSectionPosition);
            }

            if (firstSectionPosition == dataset.size() - 1) {
                dataset.add(referral);
                notifyItemInserted(dataset.size() - 1);
            } else {
                int pos = firstSectionPosition + 1;
                dataset.add(pos, referral);
                notifyItemInserted(pos);
            }
        }
    }

    class ViewHolderLead extends RecyclerView.ViewHolder {
        TextView fullName;
        TextView emailAddress;
        TextView telecomNumber;
        ImageView avatar;

        ViewHolderLead(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            emailAddress = itemView.findViewById(R.id.emailAddress);
            telecomNumber = itemView.findViewById(R.id.telecomNumber);
            avatar = itemView.findViewById(R.id.avatar);
        }

        public void setFullName(String fullName) {
            this.fullName.setText(fullName);
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress.setText(emailAddress);
        }

        public void setTelecomNumber(String telecomNumber) {
            this.telecomNumber.setText(telecomNumber);
        }

        void generateAvatar (String abbreviation, int color) {
            TextDrawable drawable = TextDrawable.builder().buildRound(abbreviation, color);
            avatar.setImageDrawable(drawable);
        }
    }

    class ViewHolderSectionTitle extends RecyclerView.ViewHolder {
        private ImageView image;
        private LinearLayout buttons;

        public ViewHolderSectionTitle(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.avatar);
            buttons = itemView.findViewById(R.id.buttonPanel);
        }

        public void setTitle(@NonNull String title, boolean enableButtons) {
            Context context = getContext();
            TextDrawable drawable;
            if (context != null) {
                drawable = TextDrawable.builder().buildRound(title.substring(0, 1),
                        getContext().getResources().getColor(R.color.avatarSectionBookmark));
            } else {
                drawable = TextDrawable.builder().buildRound(title.substring(0, 1), Color.DKGRAY);
            }
            image.setImageDrawable(drawable);

            buttons.setVisibility(enableButtons ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
