package com.shakticoin.app.wallet;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shakticoin.app.referral.MyReferralsActivity;
import com.shakticoin.app.settings.SettingsActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.miner.BecomeMinerActivity;
import com.shakticoin.app.vault.VaultAdvantageActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressLint("Registered")
public abstract class BaseWalletActivity extends AppCompatActivity {
    private BaseWalletViewModel viewModel;
    private RecyclerView notifications;
    private BaseWalletActivity.NotificationAdapter adapter;

    private View leftDrawer;
    private View rightDrawer;
    private View mainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BaseWalletViewModel.class);
    }

    protected void onInitView(View root, String title, boolean hasBackNavButton) {
        if (hasBackNavButton) {
            View arrow = root.findViewById(R.id.goParent);
            View burger = root.findViewById(R.id.drawer);
            burger.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
        } else {
            leftDrawer = root.findViewById(R.id.drawerContainer);
        }
        rightDrawer = root.findViewById(R.id.notificationContainer);
        mainFragment = root.findViewById(R.id.mainFragment);

        TextView toolbarTitle = root.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(title);

        // setup list of notifications
        notifications = findViewById(R.id.notificationList);
        notifications.setHasFixedSize(false);
        notifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseWalletActivity.NotificationAdapter();
        notifications.setAdapter(adapter);

        viewModel.notifications.observe(this, notifications -> adapter.notifyDataSetChanged());

        // set active drawer button
        if (leftDrawer != null) {
            int drawerIconId = -1;
            int drawerTextId = -1;
            switch (getCurrentDrawerSelection()) {
                case 0:
                    drawerIconId = R.id.walletMenuIcon;
                    drawerTextId = R.id.walletMenuText;
                    break;
                case 1:
                    drawerIconId = R.id.vaultMenuIcon;
                    drawerTextId = R.id.vaultMenuText;
                    break;
                case 2:
                    drawerIconId = R.id.minerMenuIcon;
                    drawerTextId = R.id.minerMenuText;
                    break;
                case 3:
                    drawerIconId = R.id.referralsMenuIcon;
                    drawerTextId = R.id.referralsMenuText;
                    break;
                case 4:
                    drawerIconId = R.id.settingsMenuIcon;
                    drawerTextId = R.id.settingsMenuText;
                    break;
            }
            int selectedButtonColor = ContextCompat.getColor(this, R.color.drawerButtonSelected);
            ImageView drawerIcon = leftDrawer.findViewById(drawerIconId);
            if (drawerIcon != null) {
                ImageViewCompat.setImageTintList(drawerIcon, ColorStateList.valueOf(selectedButtonColor));
            }
            TextView drawerText = leftDrawer.findViewById(drawerTextId);
            if (drawerText != null) {
                drawerText.setTextColor(selectedButtonColor);
            }
        }
    }

    protected void onInitView(View root, String title) {
        onInitView(root, title, false);
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
            BaseWalletViewModel.Notification notification =
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
            List<BaseWalletViewModel.Notification> list = viewModel.notifications.getValue();
            return list != null ? list.size() : 0;
        }
    }

    public void onDrawer(View v) {
        if (leftDrawer != null) {
            int openedWidth = getResources().getDimensionPixelSize(R.dimen.drawer_width);
            int currentWidth = leftDrawer.getMeasuredWidth();
            // we need close the notification messages before open drawer because main content will
            // be shifted over it
            if (currentWidth == 0) {
                float currentOffset = rightDrawer.getTranslationX();
                if (Math.abs(currentOffset) < 0.1) {
                    int containerWidth = getResources().getDimensionPixelOffset(R.dimen.notifications_list_width);
                    rightDrawer.setTranslationX(containerWidth);
                }
            }
            ValueAnimator anim = ValueAnimator.ofInt(currentWidth, currentWidth == 0 ? openedWidth : 0);
            anim.addUpdateListener(animation -> {
                int newWidth = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = leftDrawer.getLayoutParams();
                layoutParams.width = newWidth;
                leftDrawer.setLayoutParams(layoutParams);
                // move main fragment to the same direction
                mainFragment.setTranslationX(newWidth);
            });
            anim.setDuration(250);
            anim.start();
        }
    }

    public void onGoParent(View v) {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void onNotifications(View v) {
        if (rightDrawer != null) {
            int openedWidth = getResources().getDimensionPixelSize(R.dimen.notifications_list_width);
            int currentWidth = rightDrawer.getMeasuredWidth();
            // we need close the drawer before open notification messages because main content will
            // be shifted over it
            if (Math.abs(rightDrawer.getTranslationX() - openedWidth) < 0.1) {
                View drawer = findViewById(R.id.drawerContainer);
                ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
                layoutParams.width = 0;
                drawer.setLayoutParams(layoutParams);
            }

            int startPosition = rightDrawer.getTranslationX() == 0 ? 0 : currentWidth;
            int endPosition = rightDrawer.getTranslationX() == 0 ? currentWidth : 0;

            ValueAnimator anim = ValueAnimator.ofInt(startPosition, endPosition);
            anim.addUpdateListener(animation -> {
                int newWidth = (int) animation.getAnimatedValue();
                rightDrawer.setTranslationX(newWidth);
                // move main fragment to the same direction
                mainFragment.setTranslationX((openedWidth - newWidth) * -1);
            });
            anim.setDuration(500);
            anim.start();
        }
    }

    public void onOpenReferrals(View v) {
        Intent intent = new Intent(this, MyReferralsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onOpenSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onOpenMiner(View v) {
        Intent intent = new Intent(this, BecomeMinerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onOpenVault(View v) {
        Intent intent = new Intent(this, VaultAdvantageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onOpenWallet(View v) {
        Intent intent = new Intent(this, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    abstract protected int getCurrentDrawerSelection();
}
