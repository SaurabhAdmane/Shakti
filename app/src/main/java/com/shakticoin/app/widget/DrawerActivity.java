package com.shakticoin.app.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.NodeOperatorModel;
import com.shakticoin.app.api.license.SubscribedLicenseModel;
import com.shakticoin.app.feats.ParticipantsActivity;
import com.shakticoin.app.miner.MiningLicenseActivity;
import com.shakticoin.app.miner.UpgradeMinerActivity;
import com.shakticoin.app.profile.CompanySummaryActivity;
import com.shakticoin.app.profile.FamilyTreeActivity;
import com.shakticoin.app.referral.MyReferralsActivity;
import com.shakticoin.app.registration.BonusBountyActivity;
import com.shakticoin.app.settings.SettingsActivity;
import com.shakticoin.app.tour.WelcomeTourActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.wallet.WalletActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressLint("Registered")
public abstract class DrawerActivity extends AppCompatActivity {
    private DrawerActivityViewModel viewModel;
    private RecyclerView notifications;
    private DrawerActivity.NotificationAdapter adapter;

    private View leftDrawer;
    private View rightDrawer;
    private View mainFragment;

    private View menuItemMinerExpanded;
    private View menuItemMinerIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DrawerActivityViewModel.class);
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

        // We must close both left and right drawers if user clicks inside a space between
        // drawers. All views inside this area are children of mainFragment and we are trying
        // to install a listener at the root of this hierarchy.
        View rootChild = ((ViewGroup) mainFragment).getChildAt(0);
        if (rootChild != null) {
            rootChild.setOnClickListener(this::onTapOutsideDrawers);
        }

        TextView toolbarTitle = root.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(title);

        // setup list of notifications
        View emptyMessage = findViewById(R.id.empty);
        notifications = findViewById(R.id.notificationList);
        notifications.setHasFixedSize(false);
        notifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrawerActivity.NotificationAdapter();
        notifications.setAdapter(adapter);
        if (adapter.getItemCount() == 0) {
            emptyMessage.setVisibility(View.VISIBLE);
        }

        viewModel.notifications.observe(this, notifications -> adapter.notifyDataSetChanged());

        // process expendable menu item
        menuItemMinerIcon = findViewById(R.id.minerDropdownIcon);
        menuItemMinerExpanded = findViewById(R.id.minerMenuExpanded);
        viewModel.isMinerExpanded.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean state = ((ObservableBoolean) sender).get();
                if (state) {
                    menuItemMinerExpanded.setVisibility(View.VISIBLE);
                    menuItemMinerIcon.setScaleY(-1f);
                } else {
                    menuItemMinerIcon.setScaleY(1f);
                    menuItemMinerExpanded.setVisibility(View.GONE);
                }
            }
        });

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
                case 5:
                    drawerIconId = R.id.homeMenuIcon;
                    drawerTextId = R.id.homeMenuText;
                    break;
                case 6:
                    drawerIconId = R.id.familyTreeMenuIcon;
                    drawerTextId = R.id.familyTreeMenuText;
                    break;
                case 7:
                    drawerIconId = R.id.companyMenuIcon;
                    drawerTextId = R.id.companyMenuText;
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

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
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
            DrawerActivityViewModel.Notification notification =
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
            List<DrawerActivityViewModel.Notification> list = viewModel.notifications.getValue();
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
                    closeRightDrawer();
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
            if (Math.abs(rightDrawer.getTranslationX() - openedWidth) < 0.1 && leftDrawer != null) {
                closeLeftDrawer();
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

    private void closeLeftDrawer() {
        ViewGroup.LayoutParams layoutParams = leftDrawer.getLayoutParams();
        layoutParams.width = 0;
        leftDrawer.setLayoutParams(layoutParams);
        mainFragment.setTranslationX(0);
    }

    private void closeRightDrawer() {
        int containerWidth = getResources().getDimensionPixelOffset(R.dimen.notifications_list_width);
        rightDrawer.setTranslationX(containerWidth);
    }

    private void closeDrawers() {
        closeLeftDrawer();
        closeRightDrawer();
    }

    public void onTapOutsideDrawers(View v) {
        closeDrawers();
    }

    public void onOpenReferrals(View v) {
        Intent intent = new Intent(this, MyReferralsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        closeDrawers();
    }

    public void onSelectLicense(View v) {
        // Open UpgradeMinerActivity if node operator owns M101 only.
        // Otherwise call license selector.
        Activity activity = this;
        LicenseRepository repository = new LicenseRepository();
        repository.getNodeOperator(new OnCompleteListener<NodeOperatorModel>() {
            @Override
            public void onComplete(NodeOperatorModel value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }

                SubscribedLicenseModel subscription = CommonUtil.getActiveSubscription(value.getSubscribedLicenses());
                if (subscription != null && "M101Y".equals(subscription.getPlanCode())) {
                    Intent intent = new Intent(activity, UpgradeMinerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, MiningLicenseActivity.class);
                    startActivity(intent);
                }
            }
        });

        closeDrawers();
    }

    public void onOpenSettings(View v) {
        mainFragment.getMeasuredHeight();
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        closeDrawers();
    }

    public void onExpandMiner(View v) {
        viewModel.isMinerExpanded.set(!viewModel.isMinerExpanded.get());
    }

    public void onOpenPoE(View v) {
        Intent intent = new Intent(this, ParticipantsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        closeDrawers();
    }

    public void onOpenBonusBounty(View v) {
        Intent intent = new Intent(this, BonusBountyActivity.class);
        startActivity(intent);
        closeDrawers();
    }

    public void onOpenVault(View v) {
        if (!(this instanceof WalletActivity)) {
            Intent intent = new Intent(this, WalletActivity.class);
            startActivity(intent);
        }
        closeDrawers();
    }

    public void onOpenWallet(View v) {
        if (!(this instanceof WalletActivity)){
            Intent intent = new Intent(this, WalletActivity.class);
            startActivity(intent);
        }
        closeDrawers();
    }

    public void onOpenHome(View v) {
        Intent intent = new Intent(this, WelcomeTourActivity.class);
        intent.putExtra(CommonUtil.prefixed("finalDestination"), WalletActivity.class.getName());
        startActivity(intent);
        closeDrawers();
    }

    public void onOpenFamilyTree(View v) {
        Intent intent = new Intent(this, FamilyTreeActivity.class);
        startActivity(intent);
        closeDrawers();
    }

    public void onOpenCompany(View v) {
        Intent intent = new Intent(this, CompanySummaryActivity.class);
        startActivity(intent);
        closeDrawers();
    }

    public void onNotImplemented(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    abstract protected int getCurrentDrawerSelection();
}
