package com.haibin.calendarviewproject.meizu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MensesCalendar;
import com.haibin.calendarview.MonthView;
import com.haibin.calendarviewproject.R;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class MeiZuMonthView extends MonthView {

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;

    private Bitmap mBitmapLoveEmpty;
    private Bitmap mBitmapLoveFill;

    public MeiZuMonthView(Context context) {
        super(context);
        isNeedPaintBitmap = true;

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 0);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        //4.0以上硬件加速会导致无效
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));

        //加载图标
        mBitmapLoveEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_love_empty);
        mBitmapLoveFill = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_love_fill);
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
//        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
//
//        canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);
//
//        canvas.drawText(calendar.getScheme(),
//                x + mItemWidth - mPadding - mRadio / 2 - getTextWidth(calendar.getScheme()) / 2,
//                y + mPadding + mSchemeBaseLine, mTextPaint);
    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;

        boolean isInRange = isInRange(calendar);
        Paint paint;
        if (calendar.getMensesState() == MensesCalendar.STATE_PAILUAN_DATE) {
            paint = mPaiLuanDateTextPaint;
        } else {
            paint = calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint;
        }

        canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, paint);

       /* if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }*/
    }

    @Override
    protected void onDrawBackGroundAndBitmap(Canvas canvas, Calendar calendar, int x, int y) {
        int left = x + mItemWidth - mBitmapSize;
        int right = (x + mItemWidth);
        int top = y + mItemHeight - mBitmapSize;
        int bottom = y + mItemHeight;

        //根据经期状态选择颜色
        switch (calendar.getMensesState()) {
            case MensesCalendar.STATE_JINGQI:
                mBackGroundPaint.setColor(getResources().getColor(R.color.calendar_jingqi));
                break;
            case MensesCalendar.STATE_PAILUAN_DURATION:
                mBackGroundPaint.setColor(getResources().getColor(R.color.calendar_pailuan_duration));
                break;
            case MensesCalendar.STATE_PAILUAN_DATE:
                mBackGroundPaint.setColor(getResources().getColor(R.color.calendar_pailuan_date));
                break;
            case MensesCalendar.STATE_NORMAL:
                mBackGroundPaint.setColor(getResources().getColor(R.color.calendar_normal));
                break;
        }
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mBackGroundPaint);
        Rect dest = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBitmapLoveEmpty, null, dest, mBitmapPaint);
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
