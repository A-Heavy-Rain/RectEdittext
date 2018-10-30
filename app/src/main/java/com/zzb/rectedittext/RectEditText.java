package com.zzb.rectedittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * @description
 * @author: zzb
 * @date: 2018/10/25 11:27
 **/


@SuppressLint("AppCompatCustomView")
public class RectEditText extends EditText {
    private float startX;//方框开始位置
    private int rectRadius = 10;//方框角度
    private int rectSideLength = 100;//方框边长
    private int dividerWith = 30;//两个方框间距


    private int textLength = 0;//当前文本长度
    private int maxLength = 6;
    private int rectColor = Color.GRAY;
    private int focusedColor = Color.BLUE;
    private int textColor = Color.BLACK;
    private RectF focusedRecF = new RectF();
    private int lineWith=2;


    private Paint rectPaint;
    private Paint focusPaint;
    private Paint textPaint;
    private int position = 0;
    private int textOffset=2;//考虑到字间距需要文本向左一些保持居中


    public RectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setCursorVisible(false);
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    private void initPaint(Context context) {
        rectRadius = dp2px(context, 4);
        dividerWith = dp2px(context, 12);
        focusedColor = context.getResources().getColor(R.color.colorPrimary);
        rectColor = Color.parseColor("#FFCCCCCC");
        textPaint = getPaint(0, Paint.Style.FILL, textColor);
        textPaint.setTextSize(dp2px(context, 24));
        rectPaint = getPaint(lineWith, Paint.Style.STROKE, rectColor);
        focusPaint = getPaint(lineWith, Paint.Style.STROKE, focusedColor);

    }

    private Paint getPaint(int strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectSideLength = h - lineWith;
        startX = (w - (rectSideLength+lineWith) * maxLength - dividerWith * (maxLength - 1)) / 2+lineWith/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRoundRect(canvas);
        drawFocusRect(canvas, position);
        drawText(canvas);
    }


    private void drawRoundRect(Canvas canvas) {
        for (int i = 0; i < maxLength; i++) {
            RectF rect = new RectF(startX + i * (rectSideLength + dividerWith+lineWith), lineWith/2, startX + i * (rectSideLength + dividerWith+lineWith) + rectSideLength, rectSideLength + lineWith/2);
            canvas.drawRoundRect(rect,
                    rectRadius,
                    rectRadius,
                    rectPaint);
        }
    }
    private void drawText(Canvas canvas) {
        for (int i = 0; i < textLength; i++) {
            Rect rect = new Rect();
            textPaint.getTextBounds(String.valueOf(getText().toString().charAt(i)), 0, 1, rect);
//            canvas.drawRect(startX + i * (rectSideLength + dividerWith+lineWith) + (rectSideLength  - rect.width()) / 2,
//                    0,startX + i * (rectSideLength + dividerWith+lineWith) + (rectSideLength  - rect.width()) / 2+rect.width(),rectSideLength+lineWith,textPaint);
//            textPaint.setColor(Color.BLUE);
            canvas.drawText(String.valueOf(getText().toString().charAt(i)), startX + i * (rectSideLength + dividerWith+lineWith) + (rectSideLength  - rect.width()) / 2-textOffset, (rectSideLength + lineWith) / 2 + rect.height() / 2, textPaint);

        }
    }
    private void drawFocusRect(Canvas canvas, int position) {
        if (position > maxLength - 1) {
            return;
        }
        focusedRecF.set(startX + position * (rectSideLength + dividerWith+lineWith), lineWith/2, startX + position * (rectSideLength + dividerWith+lineWith) + rectSideLength, rectSideLength + lineWith/2);
        canvas.drawRoundRect(focusedRecF,
                rectRadius,
                rectRadius,
                focusPaint);
    }



    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.position = start + lengthAfter;
        textLength = text.toString().length();
        invalidate();
        if (completeListener != null && textLength == maxLength) {
            completeListener.OnComplete(getText().toString());
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == selEnd) {
            setSelection(getText().length());
        }
    }


    public void setOnCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public interface OnCompleteListener {
        void OnComplete(String text);
    }

    private OnCompleteListener completeListener;

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
