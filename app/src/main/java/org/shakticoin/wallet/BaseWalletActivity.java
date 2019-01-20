package org.shakticoin.wallet;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.profile.ProfileActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressLint("Registered")
public class BaseWalletActivity extends AppCompatActivity {
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
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onOpenSettings(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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
}
