package org.shakticoin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.shakticoin.R;

public class TextInputLayout extends RelativeLayout {

    private Drawable passwordToggleDrawable;
    private Drawable passwordToggleDummyDrawable;
    private Drawable originalEditTextEndDrawable;
    private boolean passwordToggledVisible = false;
    private boolean passwordToggleEnabled = false;

    private boolean validationEnabled = false;
    private Drawable validationOk;
    private Drawable validationFailed;
    private Validator validationListener;
    /** This flag is used to detect when first char is entered AFTER empty state. It's useful
     * for making validation mark invisible when user erase all chars.*/
    private boolean validationFieldEmpty = true;

    private EditText editTextView;
    private TextView labelView;
    private CheckableImageButton passwordToggleView;

    private CharSequence hint;

    private Float extraTopMargin;
    private Float oneDPinPixels;

    private Typeface labelFontTypeface;
    private float labelTextSize;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("InflateParams")
    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        setWillNotDraw(false);
        setAddStatesFromChildren(true);

        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();

        oneDPinPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.getDisplayMetrics());

        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.TextInputLayout, 0, 0);
        labelTextSize = a.getDimensionPixelSize(R.styleable.TextInputLayout_labelTextSize, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            labelFontTypeface = a.getFont(R.styleable.TextInputLayout_labelFontTypeface);
        } else {
            labelFontTypeface = Typeface.create("lato", Typeface.NORMAL);
        }
        passwordToggleDrawable = a.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        passwordToggleEnabled = a.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        validationEnabled = a.getBoolean(R.styleable.TextInputLayout_validationEnabled, false);
        // cannot be activated because the image mark occupies the same space as the toggle button
        if (passwordToggleEnabled) validationEnabled = false;
        if (validationEnabled) {
            int imageDimension = 18 * oneDPinPixels.intValue();
            validationOk = VectorDrawableCompat.create(resources, R.drawable.ic_valid, theme);
            if (validationOk != null) {
                validationOk.setBounds(0, 0, imageDimension, imageDimension);
            }
            validationFailed = VectorDrawableCompat.create(resources, R.drawable.ic_invalid, theme);
            if (validationFailed != null) {
                validationFailed.setBounds(0, 0, imageDimension, imageDimension);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updatePasswordToggleView();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            editTextView = (EditText) child;

            // adjust layout attributes for EditText control
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(params);
            newParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            hint = editTextView.getHint();
            if (hint != null) {
                setLabel();
                // if hint is specified then we need to add some top margin to accommodate the label
                extraTopMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f,
                        getContext().getResources().getDisplayMetrics());
                newParams.setMargins(0, extraTopMargin.intValue(), 0, 0);
            }

            setEditText(newParams);

        } else {
            super.addView(child, index, params);
        }
    }

    private void setLabel() {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        labelView = new TextView(getContext());

        // specify layout attributes
        LayoutParams labelParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Float leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, metrics);
        labelParams.leftMargin=leftMargin.intValue();
        labelParams.setMarginStart(labelParams.leftMargin);
        labelParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        labelView.setLayoutParams(labelParams);

        // adjust appearance
        Float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, metrics);
        labelView.setPadding(padding.intValue(), 0, padding.intValue(), 0);
        labelView.setText(hint);
        labelView.setTextColor(resources.getColor(R.color.colorBrand));
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
        labelView.setTypeface(labelFontTypeface);
        labelView.setVisibility(View.GONE);

        // add the label at the end (most likely no other children at the moment)
        super.addView(labelView, -1, labelParams);
    }

    private void setInputValid(boolean valid) {
        String inputString = editTextView.getText().toString();
        if (!TextUtils.isEmpty(inputString)) {
            final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(editTextView);
            // the input does not contain special validation mark when empty but once user
            // enters first char we must same any image that is possible there and set our mark
            if (inputString.length() == 1 && validationFieldEmpty) {
                originalEditTextEndDrawable = compounds[2];
                validationFieldEmpty = false;
            }

            if (valid) {
                TextViewCompat.setCompoundDrawablesRelative(editTextView, compounds[0], compounds[1],
                        validationOk, compounds[3]);
            } else {
                TextViewCompat.setCompoundDrawablesRelative(editTextView, compounds[0], compounds[1],
                        validationFailed, compounds[3]);
            }
        } else {
            validationFieldEmpty = true;
            final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(editTextView);
            if (compounds[2] != null) {
                TextViewCompat.setCompoundDrawablesRelative(editTextView, compounds[0],
                        compounds[1], null/*originalEditTextEndDrawable*/, compounds[3]);
            }
        }
    }

    /** Install validator. Only make sense if validationEnabled=true */
    public void setValidator(Validator validator) {
        validationListener = validator;
    }

    private void setEditText(RelativeLayout.LayoutParams params) {
        editTextView.setLayoutParams(params);
        // add EditText as the first child
        super.addView(editTextView, 0, params);

        // update label and hint if the field receives focus
        editTextView.setOnFocusChangeListener((v, hasFocus) -> updateLabelState());

        // update label and hint if data are typed in the field
        editTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateLabelState();
                if (validationEnabled && validationListener != null) {
                    setInputValid(validationListener.isValid(editTextView, s.toString()));
                }
            }
        });

        updatePasswordToggleView();
    }

    private void updateLabelState() {
        // just return in cases when we don't have a label
        if (editTextView == null) return;
        if (labelView == null || hint == null) return;

        boolean hasFocus = editTextView.isFocused();
        boolean hasContent = !TextUtils.isEmpty(editTextView.getText().toString());
        if (hasFocus || hasContent) {
            editTextView.setHint(null);
            labelView.setVisibility(View.VISIBLE);
            if (editTextView instanceof InlineLabelEditText) {
                // calculate width of the label TextView and pass it to the EditText
                // in order to change the border
                Paint textPaint = new Paint();
                textPaint.setTextSize(labelTextSize);
                if (labelFontTypeface != null) {
                    textPaint.setTypeface(labelFontTypeface);
                } else {
                    textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                Float textWidth = textPaint.measureText((String) hint);
                int paddingTotal = labelView.getPaddingLeft() + labelView.getPaddingRight();
                textWidth = textWidth + paddingTotal;
                ((InlineLabelEditText) editTextView).setLabelWidth(textWidth);
            }
        } else {
            labelView.setVisibility(View.GONE);
            editTextView.setHint(hint);
            if (editTextView instanceof InlineLabelEditText) {
                ((InlineLabelEditText) editTextView).setLabelWidth(0f);
            }
        }
    }

    private void updatePasswordToggleView() {
        if (editTextView == null) return;

        if (shouldShowPasswordIcon()) {
            if (passwordToggleView == null) {
                passwordToggleView = (CheckableImageButton) LayoutInflater.from(getContext())
                        .inflate(R.layout.widget_passwd_toggle, this, false);
                passwordToggleView.setImageDrawable(passwordToggleDrawable);

                LayoutParams layoutParams = new LayoutParams(passwordToggleView.getLayoutParams());
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.setMargins(0, extraTopMargin.intValue() - oneDPinPixels.intValue() * 2, 0, 0);
                super.addView(passwordToggleView, -1, layoutParams);

                passwordToggleView.setOnClickListener(view -> passwordVisibilityToggleRequested(false));
            }

            if (editTextView != null && ViewCompat.getMinimumHeight(editTextView) <= 0) {
                // we should make sure that the EditText has the same min-height as the password
                // toggle view. This ensure focus works properly, and there is no visual jump
                // if the password toggle is enabled/disabled.
                editTextView.setMinimumHeight(ViewCompat.getMinimumHeight(passwordToggleView));
            }

            passwordToggleView.setVisibility(VISIBLE);
            passwordToggleView.setChecked(passwordToggledVisible);

            // we need to add a dummy drawable as the end compound drawable so that the text is
            // indented and doesn't display below the toggle view
            if (passwordToggleDummyDrawable == null) {
                passwordToggleDummyDrawable = new ColorDrawable();
            }
            passwordToggleDummyDrawable.setBounds(0, 0, passwordToggleView.getMeasuredWidth(), 1);

            final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(editTextView);
            // ctore the user defined end compound drawable so that we can restore it later
            if (compounds[2] != passwordToggleDummyDrawable) {
                originalEditTextEndDrawable = compounds[2];
            }
            TextViewCompat.setCompoundDrawablesRelative(editTextView, compounds[0], compounds[1],
                    passwordToggleDummyDrawable, compounds[3]);

            // copy over the EditText's padding so that we match
            passwordToggleView.setPadding(editTextView.getPaddingLeft(),
                    editTextView.getPaddingTop(), editTextView.getPaddingRight(),
                    editTextView.getPaddingBottom());
        } else {
            if (passwordToggleView != null && passwordToggleView.getVisibility() == VISIBLE) {
                passwordToggleView.setVisibility(View.GONE);
            }

            if (passwordToggleDummyDrawable != null) {
                // Make sure that we remove the dummy end compound drawable if it exists, and then
                // clear it
                final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(editTextView);
                if (compounds[2] == passwordToggleDummyDrawable) {
                    TextViewCompat.setCompoundDrawablesRelative(editTextView, compounds[0],
                            compounds[1], originalEditTextEndDrawable, compounds[3]);
                    passwordToggleDummyDrawable = null;
                }
            }
        }
    }

    private void passwordVisibilityToggleRequested(boolean shouldSkipAnimations) {
        if (passwordToggleEnabled) {
            // Store the current cursor position
            final int selection = editTextView.getSelectionEnd();

            if (hasPasswordTransformation()) {
                editTextView.setTransformationMethod(null);
                passwordToggledVisible = true;
            } else {
                editTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordToggledVisible = false;
            }

            passwordToggleView.setChecked(passwordToggledVisible);
            if (shouldSkipAnimations) {
                passwordToggleView.jumpDrawablesToCurrentState();
            }

            // And restore the cursor position
            editTextView.setSelection(selection);
        }
    }

    private boolean hasPasswordTransformation() {
        return editTextView != null
                && editTextView.getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    private boolean shouldShowPasswordIcon() {
        return passwordToggleEnabled && (hasPasswordTransformation() || passwordToggledVisible);
    }

    public void setPasswordVisibilityToggleEnabled(final boolean enabled) {
        if (passwordToggleEnabled != enabled) {
            passwordToggleEnabled = enabled;

            if (!enabled && passwordToggledVisible && editTextView != null) {
                // if the toggle is no longer enabled, but we remove the PasswordTransformation
                // to make the password visible, add it back
                editTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            // reset the visibility tracking flag
            passwordToggledVisible = false;

            updatePasswordToggleView();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setError(String errorMsg) {
        int[] screenLoc = new int[2];
        editTextView.getLocationOnScreen(screenLoc);
        int fieldWidth = editTextView.getWidth();
        // TODO: or window location? check in multiwindow case

        Point point = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            display.getSize(point);
        }

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.error_callout_up, null);
        TextView textView = v.findViewById(R.id.text);
        textView.setText(errorMsg);

        PopupWindow window = new PopupWindow(v,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true); // focusable is set to make possible closing the callout by clicking outside of the window
        ViewTreeObserver observer = v.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // now we know size of the callout and can position it more precisely
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int calloutHeight = v.getHeight();
                int desiredY = screenLoc[1] - calloutHeight/2;

                int calloutWidth = v.getWidth();
                int desiredX = screenLoc[0] + fieldWidth/2;
                if (calloutWidth > fieldWidth/2) {
                    // screen width - callout width - right margin
                    desiredX = point.x - calloutWidth - 24 * oneDPinPixels.intValue();
                }
                if (desiredX < screenLoc[0]) {
                    desiredX = screenLoc[0];
                }

                window.dismiss();
                window.showAtLocation(editTextView, Gravity.NO_GRAVITY, desiredX, desiredY);
            }
        });

        window.setTouchInterceptor((v1, event) -> {
            window.dismiss();
            return true;
        });

        window.showAtLocation(editTextView, Gravity.NO_GRAVITY, screenLoc[0], screenLoc[1]);
    }

    /**
     * Validator interface that return boolean result - is the EditText content is valid or not.
     * Install it using setValidator.
     */
    public interface Validator {

        boolean isValid(EditText view, String value);
    }
}
