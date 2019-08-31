package com.shakticoin.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shakticoin.app.R;

import java.util.Objects;

public class PanelDialog extends Dialog {
    public PanelDialog(@NonNull Context context) {
        super(context);
    }

    public PanelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PanelDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String text;
        private String mainButtonLabel;
        private Drawable icon;
        private View.OnClickListener mainButtonListener;
        private View.OnClickListener closeButtonListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int resourceId) {
            title = context.getString(resourceId);
            return this;
        }

        public Builder setText(int resourceId) {
            text = context.getString(resourceId);
            return this;
        }

        public Builder setMainButton(int resourceId, View.OnClickListener listener) {
            mainButtonLabel = context.getString(resourceId);
            mainButtonListener = listener;
            return this;
        }

        public Builder setOnCloseListener(View.OnClickListener listener) {
            closeButtonListener = listener;
            return this;
        }

        public Builder setIcon(int resourceId) {
            icon = context.getResources().getDrawable(resourceId);
            return this;
        }

        @NonNull
        public PanelDialog create() {
            PanelDialog dialog = new PanelDialog(context);
            dialog.setContentView(R.layout.dialog_panel);
            Objects.requireNonNull(dialog.getWindow()).setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            ImageView iconView = dialog.findViewById(R.id.icon);
            iconView.setImageDrawable(icon);

            TextView ctrlTitle = dialog.findViewById(R.id.title);
            if (title != null && ctrlTitle != null) ctrlTitle.setText(Html.fromHtml(title));
            TextView ctrlText = dialog.findViewById(R.id.text);
            if (text != null && ctrlText != null) ctrlText.setText(Html.fromHtml(text));

            ImageButton ctrlOnClose = dialog.findViewById(R.id.doClose);
            if (ctrlOnClose != null) {
                ctrlOnClose.setOnClickListener(v -> {
                    if (closeButtonListener != null) {
                        closeButtonListener.onClick(v);
                    } else {
                        dialog.dismiss();
                    }
                });
            }

            Button ctrlMainButton = dialog.findViewById(R.id.doOk);
            if (ctrlMainButton != null) {
                ctrlMainButton.setText(mainButtonLabel);
                if (mainButtonListener != null) {
                    ctrlMainButton.setOnClickListener(mainButtonListener);
                }
            }

            return dialog;
        }
    }
}
