package com.shakticoin.app.widget;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.shakticoin.app.R;

/**
 * Custom round button that can have either checked or unchecked look. The main behaviour make it
 * similar to RadioButton - if you press it repeatedly it stays checked.
 */
public class CheckableRoundButton extends AppCompatImageButton implements Checkable {
    private static final int[] CHECKED_STATE_SET = { R.attr.is_checked };
    private boolean checkedFlag = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CheckableRoundButton(Context context) {
        super(context);
    }

    public CheckableRoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableRoundButton);

        boolean checked = a.getBoolean(R.styleable.CheckableRoundButton_is_checked, false);
        setChecked(checked);

        a.recycle();
    }

    public CheckableRoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        if (!isEnabled()) return false;
        setChecked(true);
        return super.performClick();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkedFlag != checked) {
            checkedFlag = checked;
            refreshDrawableState();

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checkedFlag);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return checkedFlag;
    }

    @Override
    public void toggle() {
        setChecked(!checkedFlag);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @BindingAdapter("is_checked")
    public static void setButtonState(CheckableRoundButton view, Boolean value) {
        view.setChecked(value);
    }

    @InverseBindingAdapter(attribute = "is_checked", event = "app:is_checkedAttrChanged")
    public static Boolean getButtonState(CheckableRoundButton view) {
        return view.isChecked();
    }

    @BindingAdapter("app:is_checkedAttrChanged")
    public static void setListeners(CheckableRoundButton view, final InverseBindingListener attrChange) {
        view.setOnCheckedChangeListener((buttonView, isChecked) -> attrChange.onChange());
    }

    /**
     * Interface definition for a callback to be invoked when the checked state changed.
     */
    public interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The view whose state has changed.
         * @param isChecked  The new checked state.
         */
        void onCheckedChanged(CheckableRoundButton buttonView, boolean isChecked);
    }
}
