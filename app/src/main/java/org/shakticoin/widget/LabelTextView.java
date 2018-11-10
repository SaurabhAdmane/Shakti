package org.shakticoin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.shakticoin.R;

/**
 * Additionally to TextView functionality, this implementation erases the border of
 * the corresponding EditText which it is overlapping.
 */
public class LabelTextView extends android.support.v7.widget.AppCompatTextView {
    Paint paint = new Paint();

    public LabelTextView(Context context) {
        super(context);
        init();
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(getResources().getColor(R.color.colorBrand));
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        Float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics);
        paint.setStrokeWidth(strokeWidth);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // paint background using XOR transfer mode and the border color
        int height = getHeight();
        int width = getWidth();
//        canvas.drawLine(0, height/2+1, width, height/2+1, paint);

        super.onDraw(canvas);
    }


}
