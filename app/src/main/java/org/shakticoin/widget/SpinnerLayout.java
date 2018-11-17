package org.shakticoin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.shakticoin.R;

public class SpinnerLayout extends RelativeLayout {

    private InlineLabelSpinner spinnerView;
    private TextView labelView;

    private CharSequence hint;

    private Float oneDPinPixels;

    private Typeface labelFontTypeface;
    private float labelTextSize;

    public SpinnerLayout(Context context) {
        this(context, null);
    }

    public SpinnerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("InflateParams")
    public SpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child instanceof InlineLabelSpinner) {
            spinnerView = (InlineLabelSpinner) child;

            // adjust layout attributes for EditText control
            LayoutParams newParams = new LayoutParams(params);
            newParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            hint = spinnerView.getHint();
            if (hint != null) {
                setLabel();
                // if hint is specified then we need to add some top margin to accommodate the label
                Float extraTopMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f,
                        getContext().getResources().getDisplayMetrics());
                newParams.setMargins(0, extraTopMargin.intValue(), 0, 0);
            }

            setSpinner(newParams);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            labelParams.setMarginStart(labelParams.leftMargin);
        }
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

    private void setSpinner(LayoutParams params) {
        spinnerView.setLayoutParams(params);
        // add Spinner as the first child
        super.addView(spinnerView, 0, params);
        spinnerView.setOnChoiceListener(this::updateLabelState);
    }

    private void updateLabelState(boolean hasSelection) {
        // just return in cases when we don't have a label
        if (spinnerView == null) return;
        if (labelView == null || hint == null) return;

        if (hasSelection) {
            spinnerView.setHint(null);
            labelView.setVisibility(View.VISIBLE);
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
            spinnerView.setLabelWidth(textWidth);
        } else {
            labelView.setVisibility(View.GONE);
            spinnerView.setHint(hint);
            spinnerView.setLabelWidth(0f);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setError(String errorMsg) {
        int[] screenLoc = new int[2];
        spinnerView.getLocationOnScreen(screenLoc);
        int fieldWidth = spinnerView.getWidth();
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
                window.showAtLocation(spinnerView, Gravity.NO_GRAVITY, desiredX, desiredY);
            }
        });

        window.setTouchInterceptor((v1, event) -> {
            window.dismiss();
            return true;
        });

        window.showAtLocation(spinnerView, Gravity.NO_GRAVITY, screenLoc[0], screenLoc[1]);
    }
}
