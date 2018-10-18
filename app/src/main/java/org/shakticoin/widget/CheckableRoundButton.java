package org.shakticoin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;

import org.shakticoin.R;

/**
 * Custom round button that can have either checked or unchecked look. The main behaviour make it
 * similar to RadioButton - if you press it repeatedly it stays checked.
 */
public class CheckableRoundButton extends android.support.v7.widget.AppCompatImageButton implements Checkable {
    private static final int[] CHECKED_STATE_SET = { R.attr.is_checked };
    private boolean checkedFlag = false;

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
}
