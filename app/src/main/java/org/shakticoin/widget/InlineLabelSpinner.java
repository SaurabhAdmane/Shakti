package org.shakticoin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.shakticoin.R;
import org.shakticoin.api.country.Country;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InlineLabelSpinner extends AppCompatSpinner {

    private CharSequence hint;
    private int textColorHint;
    private int dropdownMarginTop;
    private int dropdownMarginLeft;
    private int dropdownHeight;

    private PopupWindow popupWindow;
    private ListView listView;

    private AdapterView.OnItemSelectedListener prevListener;
    private OnChoiceListener choiceListener;

    private boolean choiceMade = false;

    private int borderColor;

    private Paint borderPaint;
    private int offset = 0;

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

    private Path borderPath = new Path();

    private float textWidth = 0f;

    public InlineLabelSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public InlineLabelSpinner(Context context, int mode) {
        super(context, mode);
        init(context, null);
    }

    public InlineLabelSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InlineLabelSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public InlineLabelSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(context, attrs);
    }

    @Override
    public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener) {
        prevListener = listener;
    }

    public InlineLabelSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InlineLabelSpinner, 0 ,0);
            dropdownMarginTop = a.getDimensionPixelSize(R.styleable.InlineLabelSpinner_dropdownMarginTop, 0);
            dropdownMarginLeft = a.getDimensionPixelSize(R.styleable.InlineLabelSpinner_dropdownMarginLeft, 0);
            dropdownHeight = a.getDimensionPixelSize(R.styleable.InlineLabelSpinner_dropdownHeight, 800);
            hint = a.getString(R.styleable.InlineLabelSpinner_hint);
            textColorHint = a.getColor(R.styleable.InlineLabelSpinner_textColorHint, 0);
            borderColor = a.getColor(R.styleable.InlineLabelSpinner_spinnerBorderColor,
                    ContextCompat.getColor(getContext(), android.R.color.white));
        }

        // prepare to draw border
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        Float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f/*1dp*/, metrics);

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        // we draw rectangle around the EditText but since the stroke width is a few pixels we
        // must ensure all of them are inside visible area
        offset = Double.valueOf(Math.ceil(strokeWidth/2)).intValue();

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.widget_inlinelnl_spinner_dropdown, null);
        popupWindow = new PopupWindow(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        listView = view.findViewById(R.id.list_view);

        this.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            popupWindow.setHeight(dropdownHeight);
            popupWindow.setWidth(v.getWidth());
            dropdownMarginTop = (v.getHeight() + popupWindow.getHeight())/-2;
        });

        prevListener = getOnItemSelectedListener();
        final InlineLabelSpinner thisSpinner = this;
        super.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!choiceMade) {
                    TextView selectedElement = getSelectedView().findViewById(R.id.tv_selected_element);
                    selectedElement.setHintTextColor(textColorHint);
                    selectedElement.setText("");
                    selectedElement.setHint(hint);
                    if (choiceListener != null) choiceListener.hasSelected(false);
                } else {
                    if (choiceListener != null) choiceListener.hasSelected(true);
                    // call stored listener using reference to the spinner directly because argument "view" is the
                    // spinner's layout
                    if (prevListener != null) prevListener.onItemSelected(parent, thisSpinner, position, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (prevListener != null) prevListener.onNothingSelected(parent);
                if (choiceListener != null) choiceListener.hasSelected(false);
            }
        });
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);

        ArrayList<Country> arrayList = new ArrayList<>();
        for(int i=0; i< adapter.getCount(); i++) {
            arrayList.add((Country) adapter.getItem(i));
        }

        CustomArrayAdapter<Country> arrayAdapter = new CustomArrayAdapter<>(getContext(), arrayList, adapter);
        listView.setAdapter(arrayAdapter);

        final InlineLabelSpinner thisSpinner = this;
        listView.setOnItemClickListener((parent, view, position, id) -> {
            choiceMade = true;
            thisSpinner.setSelection(position);
            thisSpinner.hideDropdown();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        if (popupWindow != null) {
            if (isDropdownOpened()) {
                hideDropdown();
            } else {
                showDropdown();
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textEnd.x = textStart.x + Float.valueOf(textWidth).intValue();

        borderPath.rewind();
        borderPath.moveTo(textEnd.x, textEnd.y);
        borderPath.lineTo(rightTopStart.x, rightTopStart.y);
        borderPath.arcTo(rightTopArc, 270f, 90f);
        borderPath.lineTo(rightBottomStart.x, rightBottomStart.y);
        borderPath.arcTo(rightBottomArc, 0f, 90f);
        borderPath.lineTo(leftBottomStart.x, leftBottomStart.y);
        borderPath.arcTo(leftBottomArc, 90f, 90f);
        borderPath.lineTo(leftTopStart.x, leftTopStart.y);
        borderPath.arcTo(leftTopArc, 180f, 90f);
        borderPath.lineTo(textStart.x, textStart.y);
        canvas.drawPath(borderPath, borderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int radius = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics)).intValue();

        // calculate left corner
        Point leftTop = new Point(offset, offset);
        leftTopStart = new Point(leftTop);
        leftTopStart.offset(0, radius);
        Point leftTopEnd = new Point(leftTop);
        leftTopEnd.offset(radius, 0);
        leftTopArc = new RectF(leftTopStart.x, leftTopEnd.y, leftTopEnd.x, leftTopStart.y);
        leftTopArc.left -= 1f; // TODO: in theory these corrections does not necessary but in practice the arc does not feet well with borders
        leftTopArc.top += 1f;

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
        rightBottomStart = new Point(rightBottom);
        rightBottomStart.offset(0, -radius);
        Point rightBottomEnd = new Point(rightBottom);
        rightBottomEnd.offset(-radius, 0);
        rightBottomArc = new RectF(rightBottomEnd.x, rightBottomStart.y, rightBottomStart.x, rightBottomEnd.y);

        // calculate a space for the label
        textStart = new Point(leftTop);
        textStart.offset(Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, metrics)).intValue(), 0);
        textEnd = new Point(textStart);
        textEnd.offset(Float.valueOf(textWidth).intValue(), 0);
    }

    public boolean isDropdownOpened() {
        return popupWindow.isShowing();
    }

    public void showDropdown() {
        popupWindow.showAsDropDown(this, dropdownMarginLeft, dropdownMarginTop);
    }

    public void hideDropdown() {
        popupWindow.dismiss();
    }

    public void setHint(CharSequence hint) {
        this.hint = hint;
    }

    public CharSequence getHint() {
        return hint;
    }

    /**
     * Return true when the list is initialized (and basically we have a selection) and before
     * a user select one of the items from the drop-down. During this period Spinner displays
     * a hint instead of selected item. */
    public boolean isChoiceMade() {
        return choiceMade;
    }

    /**
     * Make a rip in the top edge of the border because SpinnerLayout will place a TextView
     * with transparent background over of this area.
     * @param textWidth Pixel width of the text and both horizontal padding values.
     */
    public void setLabelWidth(float textWidth) {
        this.textWidth = textWidth;
        invalidate();
    }

    public void add(Object item) {
        CustomArrayAdapter<Country> listAdapter = (CustomArrayAdapter<Country>) listView.getAdapter();
        SpinnerListAdapter<Country> adapter = (SpinnerListAdapter<Country>) getAdapter();
        if (listAdapter != null && adapter != null) {
            listAdapter.add((Country) item);
            adapter.add((Country) item);
        }
    }

    public void addAll(List<?> items) {
        if (items != null && items.size() > 0) {
            CustomArrayAdapter<Country> listAdapter = (CustomArrayAdapter<Country>) listView.getAdapter();
            SpinnerListAdapter<Country> adapter = (SpinnerListAdapter<Country>) getAdapter();
            if (listAdapter != null && adapter != null) {
                listAdapter.addAll((Collection<? extends Country>) items);
                for (Object item : items) {
                    adapter.add((Country) item);
                }
            }
        }
    }

    public void clear() {
        CustomArrayAdapter<Country> listAdapter = (CustomArrayAdapter<Country>) listView.getAdapter();
        SpinnerListAdapter<Country> adapter = (SpinnerListAdapter<Country>) getAdapter();
        if (listAdapter != null && adapter != null) {
            listAdapter.clear();
            adapter.clear();
        }
    }

    public void setOnChoiceListener(OnChoiceListener listener) {
        choiceListener = listener;
    }

    public interface OnChoiceListener {

        void hasSelected(boolean selected);
    }

    private class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        private SpinnerAdapter spinnerAdapter;

        CustomArrayAdapter(Context context, List<T> list, SpinnerAdapter spinnerAdapter) {
            super(context,0, list);
            this.spinnerAdapter = spinnerAdapter;
        }

        @Override
        @NonNull
        public View getView(int position, View covertView, @NonNull ViewGroup root) {
            return  spinnerAdapter.getDropDownView(position, covertView, root);
        }
    }
}
