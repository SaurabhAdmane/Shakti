package org.shakticoin.wallet;

import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityWalletBinding;
import org.shakticoin.registration.SignInActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WalletActivity extends AppCompatActivity {
    private ActivityWalletBinding binding;
    private WalletModel viewModel;

    private RecyclerView notifications;
    private NotificationAdapter notificationsAdapter;

    @Override
    public void onBackPressed() {
        // This activity has been started as a root of new task and back stack is cleared. Pressing
        // Back finish the application but we don't want this and launch login screen instead.
        // This behaviour may conflict with back navigation through the fragments in future versions.
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(WalletModel.class);

        // setup list of notifications
        notifications = findViewById(R.id.notificationList);
        notifications.setHasFixedSize(false);
        notifications.setLayoutManager(new LinearLayoutManager(this));
        notificationsAdapter = new NotificationAdapter();
        notifications.setAdapter(notificationsAdapter);

        viewModel.notifications.observe(this, notifications -> notificationsAdapter.notifyDataSetChanged());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MainFragment(), MainFragment.class.getSimpleName())
                .commit();
    }

    public void onDrawer(View v) {
        View drawer = findViewById(R.id.drawerContainer);
        if (drawer != null) {
            int openedWidth = getResources().getDimensionPixelSize(R.dimen.drawer_width);
            int currentWidth = drawer.getMeasuredWidth();
            // we need close the notification messages before open drawer because main content will
            // be shifted over it
            if (currentWidth == 0) {
                View notificationsContainer = findViewById(R.id.notificationContainer);
                float currentOffset = notificationsContainer.getTranslationX();
                if (Math.abs(currentOffset) < 0.1) {
                    int containerWidth = getResources().getDimensionPixelOffset(R.dimen.notifications_list_width);
                    notificationsContainer.setTranslationX(containerWidth);
                }
            }
            ValueAnimator anim = ValueAnimator.ofInt(currentWidth, currentWidth == 0 ? openedWidth : 0);
            anim.addUpdateListener(animation -> {
                int newWidth = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
                layoutParams.width = newWidth;
                drawer.setLayoutParams(layoutParams);
                // move main fragment to the same direction
                binding.mainFragment.setTranslationX(newWidth);
            });
            anim.setDuration(250);
            anim.start();
        }
    }

    public void onNotifications(View v) {
        View notificationsContainer = findViewById(R.id.notificationContainer);
        if (notificationsContainer != null) {
            int openedWidth = getResources().getDimensionPixelSize(R.dimen.notifications_list_width);
            int currentWidth = notificationsContainer.getMeasuredWidth();
            // we need close the drawer before open notification messages because main content will
            // be shifted over it
            if (Math.abs(notificationsContainer.getTranslationX() - openedWidth) < 0.1) {
                View drawer = findViewById(R.id.drawerContainer);
                ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
                layoutParams.width = 0;
                drawer.setLayoutParams(layoutParams);
            }

            int startPosition = notificationsContainer.getTranslationX() == 0 ? 0 : currentWidth;
            int endPosition = notificationsContainer.getTranslationX() == 0 ? currentWidth : 0;

            ValueAnimator anim = ValueAnimator.ofInt(startPosition, endPosition);
            anim.addUpdateListener(animation -> {
                int newWidth = (int) animation.getAnimatedValue();
                notificationsContainer.setTranslationX(newWidth);
                // move main fragment to the same direction
                binding.mainFragment.setTranslationX((openedWidth - newWidth) * -1);
            });
            anim.setDuration(500);
            anim.start();
        }
    }

    public void onOpenReferrals(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onOpenSettings(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onOpenMiner(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onOpenVault(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onOpenWallet(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView message;
        public TextView date;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

        @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationViewHolder viewHolder, int position) {
            WalletModel.Notification notification =
                    Objects.requireNonNull(viewModel.notifications.getValue()).get(position);
            if (notification != null) {
                viewHolder.title.setText(notification.getTitle());
                viewHolder.message.setText(notification.getMessage());
                Date date = notification.getDate();
                DateFormat fmt = DateFormat.getDateInstance(DateFormat.SHORT);
                viewHolder.date.setText(fmt.format(date));
                if (notification.isRead()) {
                    viewHolder.title.setTextColor(getResources().getColor(R.color.colorReadMessage));
                    viewHolder.message.setTextColor(getResources().getColor(R.color.colorReadMessage));
                    viewHolder.date.setTextColor(getResources().getColor(R.color.colorReadMessage));
                }
            }
        }

        @Override
        public int getItemCount() {
            List<WalletModel.Notification> list = viewModel.notifications.getValue();
            return list != null ? list.size() : 0;
        }
    }
}
