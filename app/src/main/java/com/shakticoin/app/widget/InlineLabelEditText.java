package com.shakticoin.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.shakticoin.app.R;

public class InlineLabelEditText extends AppCompatEditText {

    // custom attributes
    private int borderColor;

    private Paint borderPaint;
    private int offset = 0;

    private RectF closedBorder;
    private float closedBorderCornerRadius = 0;

    private Point textStart;
    private Point textEnd;

    private Point leftTopStart;
    private RectF leftTopArc;

    private Point leftBottomStart;
    private RectF leftBottomArc;

    private Point rightTopStart;
    private RectF rightTopArc;

    private Point rightBottomStart;
    private RectF rightBottomArc;

    private final Path borderPath = new Path();

    private Float textWidth = 0f;

    public InlineLabelEditText(Context context) {
        super(context);
        init(null);
    }

    public InlineLabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InlineLabelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float singleDpWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f/*1dp*/, metrics);
        closedBorderCornerRadius = singleDpWidth * 3; // 3dp

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.InlineLabelEditText, 0, 0);
        borderColor = a.getColor(R.styleable.InlineLabelEditText_borderColor,
                ContextCompat.getColor(getContext(), android.R.color.white));

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(singleDpWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        // we draw rectangle around the EditText but since the stroke width is a few pixels we
        // must ensure all of them are inside visible area
        offset = Double.valueOf(Math.ceil(singleDpWidth/2)).intValue();
    }

    /**
     * Make a rip in the top edge of the border because TextInputLayout will place a TextView
     * with transparent background over of this area.
     * @param textWidth Pixel width of the text and both horizontal padding values.
     */
    public void setLabelWidth(float textWidth) {
        this.textWidth = textWidth;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (textWidth.compareTo(1f) > 0) {
            drawOpenBorder(canvas);
        } else {
            drawClosedBorder(canvas);
        }
    }

    public void drawOpenBorder(Canvas canvas) {
        // When we enter very long string (with width longer than the field width) the
        // canvas clip bounds grows horizontally and this breaks drawing geometry. We
        // need add a correction to every X value. canvasOffset is that correction.
        // It equals to zero until length of the string entered exceeds field width.
        int canvasOffset = canvas.getClipBounds().right - canvas.getWidth();

        textEnd.x = textStart.x + textWidth.intValue() + canvasOffset;

        borderPath.rewind();
        borderPath.moveTo(textEnd.x, textEnd.y);
        borderPath.lineTo(rightTopStart.x + canvasOffset, rightTopStart.y);
        borderPath.arcTo(offsetRect(rightTopArc, canvasOffset), 270f, 90f);
        borderPath.lineTo(rightBottomStart.x + canvasOffset, rightBottomStart.y);
        borderPath.arcTo(offsetRect(rightBottomArc, canvasOffset), 0f, 90f);
        borderPath.lineTo(leftBottomStart.x + canvasOffset, leftBottomStart.y);
        borderPath.arcTo(offsetRect(leftBottomArc, canvasOffset), 90f, 90f);
        borderPath.lineTo(leftTopStart.x + canvasOffset, leftTopStart.y);
        borderPath.arcTo(offsetRect(leftTopArc, canvasOffset), 180f, 90f);
        borderPath.lineTo(textStart.x + canvasOffset, textStart.y);
        canvas.drawPath(borderPath, borderPaint);
    }

    private void drawClosedBorder(Canvas canvas) {
        canvas.drawRoundRect(closedBorder, closedBorderCornerRadius, closedBorderCornerRadius,  borderPaint);
    }

    private RectF offsetRect(RectF r, float offsetX) {
        return new RectF(r.left + offsetX, r.top, r.right + offsetX, r.bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int radius = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics)).intValue();

        closedBorder = new RectF();

        // calculate left top corner
        Point leftTop = new Point(offset, offset);
        closedBorder.left = leftTop.x;
        closedBorder.top = leftTop.y;
        leftTopStart = new Point(leftTop);
        leftTopStart.offset(0, radius);
        Point leftTopEnd = new Point(leftTop);
        leftTopEnd.offset(radius, 0);
        leftTopArc = new RectF(leftTopStart.x, leftTopEnd.y, leftTopEnd.x, leftTopStart.y);

        // calculate left bottom corner
        Point leftBottom = new Point(offset, h - offset * 2);
        leftBottomStart = new Point(leftBottom);
        leftBottomStart.offset(radius, 0);
        Point leftBottomEnd = new Point(leftBottom);
        leftBottomEnd.offset(0, -radius);
        leftBottomArc = new RectF(leftBottomEnd.x, leftBottomEnd.y, leftBottomStart.x, leftBottomStart.y);

        // calculate right top corner
        Point rightTop = new Point(w - offset * 2, offset);
        rightTopStart = new Point(rightTop);
        rightTopStart.offset(-radius, 0);
        Point rightTopEnd = new Point(rightTop);
        rightTopEnd.offset(0, radius);
        rightTopArc = new RectF(rightTopStart.x, rightTopStart.y, rightTopEnd.x, rightTopEnd.y);

        // calculate right bottom corner
        Point rightBottom = new Point(w - offset * 2, h - offset * 2);
        closedBorder.right = rightBottom.x;
        closedBorder.bottom = rightBottom.y;
        rightBottomStart = new Point(rightBottom);
        rightBottomStart.offset(0, -radius);
        Point rightBottomEnd = new Point(rightBottom);
        rightBottomEnd.offset(-radius, 0);
        rightBottomArc = new RectF(rightBottomEnd.x, rightBottomStart.y, rightBottomStart.x, rightBottomEnd.y);

        // calculate a space for the label
        textStart = new Point(leftTop);
        textStart.offset(Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, metrics)).intValue(), 0);
        textEnd = new Point(textStart);
        textEnd.offset(textWidth.intValue(), 0);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
    }

}
