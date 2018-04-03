package com.example.seamasshih.mypokercardviewtest.View;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by SeamasShih on 2018/3/30.
 */

public class BidButton extends View {
    public BidButton(Context context){
        super(context);
    }
    public BidButton(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    private float viewLength = 500;
    float radius;
    private float centerRate = 0.4f;
    private int n = 4;
    private int[] rgb = {255,255,0};
    private int[] centerRGB = {255,0,0};
    private float startAngle = 25;
    private float sweepAngle;
    private int shadowAlpha = 30;
    Paint mBGPaint = new Paint();
    Paint mBoundPaint = new Paint();
    Paint mHatPaint = new Paint();
    Paint mCenterPaint = new Paint();
    Path[] selectSector = new Path[n];
    Region[] select = new Region[n];
    Path centerCircle = new Path();
    Region center = new Region();
    Region all = new Region(0,0,(int)viewLength,(int)viewLength);
    float cCLength = (float)(4/3*Math.tan(Math.PI/2/n)*radius*1.4);
    private int selecting = -2;
    private boolean isPressed = false;
    private boolean doAnime = false;
    float r = 0;
    private ValueAnimator animatorOpen = new ValueAnimator();
    private ValueAnimator animatorClose = new ValueAnimator();

    private void init(){
        radius = viewLength/2;
        selectSector = null;
        selectSector = new Path[n];
        select = null;
        select = new Region[n];
        sweepAngle = (float)360/(float)n;
        mBGPaint.setColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setAntiAlias(true);
        mCenterPaint.setColor(Color.rgb(centerRGB[0],centerRGB[1],centerRGB[2]));
        mCenterPaint.setStyle(Paint.Style.FILL);
        mCenterPaint.setAntiAlias(true);
        mBoundPaint.setColor(Color.BLACK);
        mBoundPaint.setStyle(Paint.Style.STROKE);
        mBoundPaint.setAntiAlias(true);
        mHatPaint.setColor(Color.argb(shadowAlpha,0,0,0));
        mHatPaint.setStyle(Paint.Style.FILL);
        mHatPaint.setAntiAlias(true);
        all.setEmpty();
        all = new Region(0,0,(int)viewLength,(int)viewLength);
        centerCircle.reset();
        centerCircle.addCircle(viewLength/2,viewLength/2,radius*centerRate, Path.Direction.CW);
        center.setPath(centerCircle,all);
        for (int i = 0; i < selectSector.length; i++) {
            if (selectSector[i] == null)
                selectSector[i] = new Path();
            else
                selectSector[i].reset();
            selectSector[i].arcTo(0,0,viewLength,viewLength,startAngle+sweepAngle*i,sweepAngle,false);
            selectSector[i].lineTo(radius,radius);
            selectSector[i].close();
        }
        for (int i = 0; i < select.length; i++) {
            if (select[i] == null)
                select[i] = new Region();
            else
                select[i].setEmpty();
            select[i].setPath(selectSector[i],all);
            select[i].op(center, Region.Op.DIFFERENCE);
        }
        for (int i = 0; i < selectSector.length; i++) {
            selectSector[i].reset();
            selectSector[i].arcTo(radius-r,radius-r,radius+r,radius+r,startAngle+sweepAngle*i,sweepAngle,false);
            selectSector[i].lineTo(radius,radius);
            selectSector[i].close();
        }
        animatorOpen = ValueAnimator.ofFloat(viewLength*centerRate/2,radius).setDuration(100);
        animatorOpen.setInterpolator(new OvershootInterpolator());
        animatorOpen.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                r = (float)animation.getAnimatedValue();
                for (int i = 0; i < selectSector.length; i++) {
                    selectSector[i].reset();
                    selectSector[i].arcTo(radius-r,radius-r,radius+r,radius+r,startAngle+sweepAngle*i,sweepAngle,false);
                    selectSector[i].lineTo(radius,radius);
                    selectSector[i].close();
                }
                invalidate();
            }
        });
        animatorClose = ValueAnimator.ofFloat(radius,viewLength*centerRate/2).setDuration(50);
        animatorClose.setInterpolator(new LinearInterpolator());
        animatorClose.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                r = (float)animation.getAnimatedValue();
                for (int i = 0; i < selectSector.length; i++) {
                    selectSector[i].reset();
                    selectSector[i].arcTo(radius-r,radius-r,radius+r,radius+r,startAngle+sweepAngle*i,sweepAngle,false);
                    selectSector[i].lineTo(radius,radius);
                    selectSector[i].close();
                }
                invalidate();
            }
        });
    }

    public int getN(){return n;}
    public void setN(int n){
        this.n = n;
        init();
        invalidate();
    }
    public float getStartAngle(){return startAngle;}
    public void setStartAngle(float startAngle){
        this.startAngle = startAngle;
        init();
        invalidate();
    }
    public float getCenterRate(){return centerRate;}
    public void setCenterRate(float centerRate){
        this.centerRate = centerRate;
        centerCircle = new Path();
        centerCircle.addCircle(viewLength/2,viewLength/2,radius*centerRate, Path.Direction.CW);
        invalidate();
    }
    public int[] getRgb(){return rgb;}
    public void setRBG(int r, int g, int b){
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        init();
        invalidate();
    }
    public int[] getCenterRGB(){return centerRGB;}
    public void setCenterRGB(int r, int g, int b){
        centerRGB[0] = r;
        centerRGB[1] = g;
        centerRGB[2] = b;
        init();
        invalidate();
    }
    public int getShadowAlpha(){return shadowAlpha;}
    public void setShadowAlpha(int shadowAlpha){
        this.shadowAlpha = shadowAlpha;
        init();
        invalidate();
    }
    public float getViewLength(){return viewLength;}
    public void setViewLength(float viewLength){
        this.viewLength = viewLength;
        radius = viewLength/2;
        init();
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)viewLength,(int)viewLength);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            isPressed = true;
            animatorOpen.start();
            selecting = -1;
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            for (int i = 0; i < select.length; i++){
                if (select[i].contains((int)event.getX(),(int)event.getY())){
                    selecting = i;
                }
            }
            if (center.contains((int)event.getX(),(int)event.getY())){
                selecting = -1;
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_UP){
            isPressed = false;
            selecting = -2;
            animatorClose.start();
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < selectSector.length; i++) {
            canvas.drawPath(selectSector[i], mBGPaint);
            canvas.drawPath(selectSector[i], mBoundPaint);
        }
        if (selecting > -1 && selecting < select.length)
            canvas.drawPath(selectSector[selecting],mHatPaint);
        canvas.drawPath(centerCircle,mCenterPaint);
        canvas.drawPath(centerCircle, mBoundPaint);
        if (selecting == -1){
            canvas.drawPath(centerCircle,mHatPaint);
        }
    }
}
