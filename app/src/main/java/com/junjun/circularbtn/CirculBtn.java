package com.junjun.circularbtn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CirculBtn extends View {
    private final static float Djwidth = 30;//点击宽度
    private int mWidth;
    private int mHeight;
    private Context mContext;
    private int neiCicl;
    private PieCharViewClick pieCharViewClick;
    private boolean isCheck;


    private boolean isClick;


    public void setPieCharViewClick(PieCharViewClick pieCharViewClick) {
        this.pieCharViewClick = pieCharViewClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
        postInvalidate();
    }

    public void setCheck(boolean check) {//是否有选中状态  默认有false
        isCheck = check;
    }

    /**
     * 圆环宽度
     */
    private float mRoundWidth = 180;

    private Paint piePaint, txtPaint;
    private RectF pieInRectF, pieOutRectF, pieOutTouchRectF;
    private List<Pie> mList;


    //青 蓝 紫  白   粉  红  橙 黄
    private int[] mColors = {0xFF33E0FF,
            0xFF5F66FE, 0xFFC100EA, 0xFFFFFFFF, 0xFFF77C99, 0xFFFF0900, 0xFFFF9A00, 0xFFFFFC19, 0xFF35BF5B};


    private int[] mColorsClose = {0xFFD2D2D2,
            0xFFCACACA, 0xFFC2C2C2, 0xFFFFFFFF, 0xFFFAFAFA, 0xFFF2F2F2, 0xFFEAEAEA, 0xFFE2E2E2, 0xFFDADADA};

    private String[] mIds = {"5", "6", "7", "8", "9", "1", "2", "3", "4"};

    private String[] mName = {"青", "蓝", "紫", "白", "粉", "红", "橙", "黄", "绿"};

    private float downX, downY;

    public CirculBtn(Context context) {
        this(context, null);
    }

    public CirculBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirculBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        Log.i("--------", "==圆形点击初始华");
        isClick = true;
        neiCicl = dip2px(101);
        piePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setStyle(Paint.Style.STROKE);
        txtPaint.setTextAlign(Paint.Align.CENTER);

        pieInRectF = new RectF();
        pieOutRectF = new RectF();
        pieOutTouchRectF = new RectF();

        mList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Pie pie = new Pie();
            pie.setName(mName[i]);
            pie.setColor(mColors[i]);
            pie.setId(mIds[i]);
            mList.add(pie);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();

        //设置绘制外圆的矩形
        pieOutRectF.left = 0 + Djwidth;
        pieOutRectF.top = 0 + Djwidth;
        pieOutRectF.right = mWidth - Djwidth;
        pieOutRectF.bottom = mHeight - Djwidth;

        //设置内圆圈
        pieInRectF.left = pieOutRectF.left + mRoundWidth;
        pieInRectF.top = pieOutRectF.top + mRoundWidth;
        pieInRectF.right = pieOutRectF.right - mRoundWidth;
        pieInRectF.bottom = pieOutRectF.bottom - mRoundWidth;


        //点击的时候最大
        pieOutTouchRectF.left = 0;
        pieOutTouchRectF.top = 0;
        pieOutTouchRectF.right = mWidth;
        pieOutTouchRectF.bottom = mHeight;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPie(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClick) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    for (Pie pie : mList) {
                        pie.setTouch(false);
                        if (pie.isInRegion(downX, downY)) {
                            pie.setTouch(!pie.isTouch());
                            postInvalidate();
                            if (pieCharViewClick != null) {
                                pieCharViewClick.click(pie);
                            }
                            //返回初始状态
                            if (isCheck) {
                                getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (Pie p : mList) {
                                            p.setTouch(false);
                                        }
                                        postInvalidate();
                                    }
                                }, 300);
                            }

                        }
                    }


                    break;
            }

        }
        return true;
    }

    /**
     * 绘制饼图
     */
    private void drawPie(Canvas canvas) {
        int startAngle = 30;//起始角度
        Pie item;
        //根据不同SDK版本采用不同的绘制方案，需要调整paint
        if (Build.VERSION.SDK_INT >= 19) {
            piePaint.setStrokeWidth(3);
            piePaint.setStyle(Paint.Style.FILL);
        } else {
            piePaint.setStrokeWidth(mRoundWidth);
            piePaint.setStyle(Paint.Style.STROKE);
        }

        for (int i = 0; i < mList.size(); i++) {
            item = mList.get(i);
            int sweepAngle = 40;
            //设置颜色，之后绘制类目明细还要使用
            if (isClick) {
                piePaint.setColor(item.getColor());
            } else {
                piePaint.setColor(mColorsClose[i]);
            }

            //绘制弧形
            if (Build.VERSION.SDK_INT >= 19) {
                Path path = null;
                if (item.isTouch()) {
                    path = getArcPath(pieInRectF, pieOutTouchRectF, startAngle, sweepAngle, Path.Op.UNION);
                    canvas.drawPath(path, piePaint);
                    item.setRegion(path);
                } else {
                    path = getArcPath(pieInRectF, pieOutRectF, startAngle, sweepAngle, Path.Op.DIFFERENCE);
                    canvas.drawPath(path, piePaint);
                    item.setRegion(path);
                }

            } else {
                Path path = new Path();
                path.addArc(pieInRectF, startAngle, sweepAngle);
                canvas.drawPath(path, piePaint);
            }
            //计算起始角度
            startAngle += sweepAngle;
        }
    }

    /**
     * 获取绘制弧度所需要的path
     *
     * @param in
     * @param out
     * @param startAngle
     * @param angle
     * @return
     */
    private Path getArcPath(RectF in, RectF out, int startAngle, int angle, Path.Op op) {
        Path path1 = new Path();
        path1.moveTo(in.centerX(), in.centerY());
        // path1.arcTo(in, startAngle, angle);
        path1.addCircle(in.centerX(), in.centerY(), neiCicl / 2, Path.Direction.CCW);

        Path path2 = new Path();
        path2.moveTo(out.centerX(), out.centerY());
        path2.arcTo(out, startAngle, angle);

        Path path = new Path();
        path.op(path2, path1, op);
        return path;
    }


    //设置颜色
    public void setColor(String id) {
        mList.clear();
        for (int i = 0; i < 9; i++) {
            Pie pie = new Pie();
            pie.setName(mName[i]);
            pie.setColor(mColors[i]);
            pie.setId(mIds[i]);
            if (mIds[i].equals(id)) {
                pie.setTouch(true);
            } else {
                pie.setTouch(false);
            }
            mList.add(pie);
        }
        postInvalidate();
    }

    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface PieCharViewClick {
        void click(Pie pie);
    }
}

