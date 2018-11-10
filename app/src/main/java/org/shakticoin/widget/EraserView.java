package org.shakticoin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import org.shakticoin.R;

public class EraserView extends View {
    Paint paint = new Paint();

    public EraserView(Context context) {
        this(context, null);
    }

    public EraserView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EraserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        TextInputLayout d;
        paint.setColor(getResources().getColor(android.R.color.white));
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics));
        paint.setStyle(Paint.Style.STROKE);
//        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        Float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics);
//        paint.setStrokeWidth(strokeWidth);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path border = new Path();
//        border.setFillType(Path.FillType.WINDING);
        border.moveTo(0+2+50, 0+2);
        border.lineTo(0+2, 0+2);
        border.lineTo(0+2, canvas.getHeight()-2-20);
        RectF leftBottomArc = new RectF(0+2, canvas.getHeight()-2-20, 0+2+20, canvas.getHeight()-2);
        border.arcTo(leftBottomArc, 90, 90, true);
        border.lineTo(canvas.getWidth()-2-2, canvas.getHeight()-2);
        border.lineTo(canvas.getWidth()-2-2, 0+2);
        border.lineTo(0+2+150, 0+2);
//        border.close();
        canvas.drawPath(border, paint);
//        canvas.drawLine(0f, 0f, canvas.getWidth(), canvas.getHeight(), paint);
//        canvas.drawPaint(paint);
        super.onDraw(canvas);
    }

}
