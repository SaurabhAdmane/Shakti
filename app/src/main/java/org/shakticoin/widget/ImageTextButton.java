package org.shakticoin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import org.shakticoin.R;

public class ImageTextButton extends AppCompatButton {
    private Paint paint;
    private Rect destRect;

    private Drawable drawableLeft;
    private Drawable drawableRight;
    private int drawableLeftPadding;
    private int drawableRightPadding;

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextButton, 0 ,0);
        drawableLeft = typedArray.getDrawable(R.styleable.ImageTextButton_drawableLeft);
        drawableLeftPadding = typedArray.getDimensionPixelSize(R.styleable.ImageTextButton_drawableLeftPadding, 0);
        drawableRight = typedArray.getDrawable(R.styleable.ImageTextButton_drawableRight);
        drawableRightPadding = typedArray.getDimensionPixelSize(R.styleable.ImageTextButton_drawableRightPadding, 0);

        paint = new Paint();
        destRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(drawableLeft != null) {
            Bitmap bitmap = drawableToBitmap(drawableLeft);

            float textWidth = getPaint().measureText((String) getText());
            int left = (int) ((getWidth() / 2f) - (textWidth / 2f) - drawableLeftPadding - bitmap.getWidth() + getPaddingLeft());
            int top = getHeight() / 2 - bitmap.getHeight() / 2 + getPaddingBottom();

            if(left >= bitmap.getWidth()) {
                destRect.set(left, top, left + bitmap.getWidth(), top + bitmap.getHeight());
                canvas.drawBitmap(bitmap, null, destRect, paint);
            }
        }

        if(drawableRight != null) {
            Bitmap bitmap = drawableToBitmap(drawableRight);

            float textWidth = getPaint().measureText((String) getText());
            int left = (int) ((getWidth() / 2f) + (textWidth / 2f) + drawableRightPadding - getPaddingRight());
            int top = getHeight() / 2 - bitmap.getHeight() / 2 + getPaddingBottom();

            if(getWidth() - left >= bitmap.getWidth()) {
                destRect.set(left, top, left + bitmap.getWidth(), top + bitmap.getHeight());
                canvas.drawBitmap(bitmap, null, destRect, paint);
            }
        }

        super.onDraw(canvas);
    }

    private Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public Drawable getDrawableLeft() {
        return drawableLeft;
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableLeft = drawableLeft;
    }

    public Drawable getDrawableRight() {
        return drawableRight;
    }

    public void setDrawableRight(Drawable drawableRight) {
        this.drawableRight = drawableRight;
    }

    public int getDrawableLeftPadding() {
        return drawableLeftPadding;
    }

    public void setDrawableLeftPadding(int drawableLeftPadding) {
        this.drawableLeftPadding = drawableLeftPadding;
    }

    public int getDrawableRightPadding() {
        return drawableRightPadding;
    }

    public void setDrawableRightPadding(int drawableRightPadding) {
        this.drawableRightPadding = drawableRightPadding;
    }

}
