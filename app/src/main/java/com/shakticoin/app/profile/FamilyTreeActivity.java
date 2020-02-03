package com.shakticoin.app.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.user.FamilyMember;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.util.ArrayList;
import java.util.List;

public class FamilyTreeActivity extends BaseWalletActivity implements DialogInterface.OnDismissListener {
    private UserRepository userRepository = new UserRepository();

    private FamilyMembersAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_tree);

        onInitView(findViewById(R.id.container), getString(R.string.family_title), true);

        RecyclerView members = findViewById(R.id.membersList);
        progressBar = findViewById(R.id.progressBar);

        members.setHasFixedSize(true);
        members.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FamilyMembersAdapter();
        members.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        final Activity activity = this;
        progressBar.setVisibility(View.VISIBLE);
        userRepository.getFamilyMembers(new OnCompleteListener<List<FamilyMember>>() {
            @Override
            public void onComplete(List<FamilyMember> value, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(activity));
                    }
                    return;
                }
                adapter.clear();
                adapter.addAll(value);
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 6;
    }

    public void onAddFamilyMember(View v) {
        DialogAddFamilyMember addMemberDialog = DialogAddFamilyMember.getInstance();
        addMemberDialog.show(getSupportFragmentManager(), DialogAddFamilyMember.class.getSimpleName());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        updateList();
    }

    class FamilyMembersAdapter extends RecyclerView.Adapter<FamilyMembersAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView emailAddress;
            private TextView phoneNumber;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                emailAddress = itemView.findViewById(R.id.emailAddress);
                phoneNumber = itemView.findViewById(R.id.phoneNumber);
            }

            public void setName(String name) {
                this.name.setText(name);
            }

            public void setEmailAddress(String emailAddress) {
                this.emailAddress.setText(emailAddress);
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber.setText(phoneNumber);
            }
        }

        private ArrayList<FamilyMember> dataset = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family_tree, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FamilyMember item = dataset.get(position);
            StringBuilder sb = new StringBuilder();
            String firstName = item.getFirst_name();
            if (firstName != null) sb.append(firstName);
            String lastName = item.getLast_name();
            if (lastName != null) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(lastName);
            }
            holder.setName(sb.toString());
            holder.setEmailAddress(item.getEmail());
            holder.setPhoneNumber(item.getPhone());
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public void add(FamilyMember item) {
            dataset.add(item);
            notifyItemInserted(dataset.size()-1);
        }

        void addAll(List<FamilyMember> items) {
            if (items != null && items.size() > 0) {
                for (FamilyMember item : items) {
                    add(item);
                }
            }
        }

        void clear() {
            if (dataset.size() > 0) {
                dataset.clear();
                notifyDataSetChanged();
            }
        }
    }
}
