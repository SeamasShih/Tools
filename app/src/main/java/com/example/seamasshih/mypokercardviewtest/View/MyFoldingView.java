package com.example.seamasshih.mypokercardviewtest.View;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.seamasshih.mypokercardviewtest.R;


public class MyFoldingView extends View{

    private Bitmap mBitmap;
    private Paint mShadowPaint;
    private Paint mSolidPaint;
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowGradientShader;
    private Matrix mSolidGradientMatrix;
    private LinearGradient mSolidGradientShader;
    private float lengthOfFold;
    private float lengthOfFoldPiece;
    private float heightOfFold;
    private float rateOfFold;
    private Matrix[] mMatrices;
    private float basePress;
    private float baseRate;
    private ValueAnimator open;
    private ValueAnimator close;
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private float drawWidth;
    private float drawHeight;
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;


    private int numOfFold;
    private int maxHeightOfFold;
    private int speedOfFold;
    private int[] colorOfShadow = new int[3];
    private int alphaOfShadow;
    private float animationRate;

    public int getNumOfFold() {
        return numOfFold;
    }
    public void setNumOfFold(int numOfFold){
        this.numOfFold = numOfFold;
        init();
        invalidate();
    }
    public int getMaxHeightOfFold(){
        return maxHeightOfFold;
    }
    public void setMaxHeightOfFold(int maxHeightOfFold){
        this.maxHeightOfFold = maxHeightOfFold;
        init();
        invalidate();
    }
    public int getSpeedOfFold(){
        return speedOfFold;
    }
    public void setSpeedOfFold(int speedOfFold){
        this.speedOfFold = speedOfFold;
        init();
        invalidate();
    }
    public int[] getColorOfShadow(){
        return colorOfShadow;
    }
    public void setColorOfShadow(int r , int g , int b){
        if (r > 255 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) return;
        colorOfShadow[0] = r;
        colorOfShadow[1] = g;
        colorOfShadow[2] = b;
        init();
        invalidate();
    }
    public int getAlphaOfShadow(){
        return alphaOfShadow;
    }
    public void setAlphaOfShadow(int alphaOfShadow){
        if (alphaOfShadow > 255 || alphaOfShadow < 0) return;
        this.alphaOfShadow = alphaOfShadow;
        init();
        invalidate();
    }
    public float getAnimationRate(){
        return animationRate;
    }
    public void setAnimationRate(float animationRate){
        if (animationRate > 0.5) return;
        if (animationRate < 0) return;
        this.animationRate = animationRate;
        init();
        invalidate();
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        init();
        invalidate();
    }

    public MyFoldingView(Context context){
        super(context);
        setting();
        init();
    }
    public MyFoldingView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
        setting();
        init();
    }

    private void setting(){
        rateOfFold = 1;
        animationRate = 0.3f;
        numOfFold = 9;
        maxHeightOfFold = 60;
        speedOfFold = 400;
        for (int i = 0 ; i < colorOfShadow.length ; i++) colorOfShadow[i] = 255;
        alphaOfShadow =(int) (0.6 * 255);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
        lengthOfFoldPiece = mBitmap.getWidth() / numOfFold;
        mMatrices = new Matrix[numOfFold];
        for (int i =  0 ; i < mMatrices.length; i++) mMatrices[i] = new Matrix();
        mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowGradientMatrix = new Matrix();
        mSolidGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
        mSolidGradientMatrix = new Matrix();
        mSolidPaint = new Paint();
        mShadowPaint = new Paint();
        open = new ValueAnimator();
        open.setFloatValues(0,1);
        close = new ValueAnimator();
        close.setFloatValues(1,0);
    }

    private void init(){
        mSolidPaint.reset();
        mSolidPaint.setStyle(Paint.Style.FILL);
        mSolidPaint.setAntiAlias(true);
        mSolidPaint.setShader(mSolidGradientShader);
        mSolidGradientMatrix.setScale(lengthOfFold , 1);
        mSolidGradientShader.setLocalMatrix(mShadowGradientMatrix);
        mSolidPaint.setAlpha((int)(alphaOfShadow * (1-rateOfFold)));

        mShadowPaint.reset();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix.setScale(lengthOfFold , 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        mShadowPaint.setAlpha((int)(alphaOfShadow * (1-rateOfFold)));

        drawWidth = Math.min(screenWidth,mBitmap.getWidth());
        drawHeight = Math.min(screenHeight,mBitmap.getHeight());

        float[] src = new float[8];
        float[] dst = new float[8];
        lengthOfFold = lengthOfFoldPiece * rateOfFold;
        heightOfFold = maxHeightOfFold * (1-rateOfFold);

        for (int i = 0 ; i < mMatrices.length ; i++){
            src[0] = i * lengthOfFoldPiece;
            src[1] = 0;
            src[2] = src[0] + lengthOfFoldPiece;
            src[3] = 0;
            src[4] = src[2];
            src[5] = drawHeight;
            src[6] = src[0];
            src[7] = src[5];

            boolean isEven = (i%2 == 0);

            dst[0] = i * lengthOfFold;
            dst[1] = (isEven? src[1] : src[1] + heightOfFold);
            dst[2] = dst[0] + lengthOfFold;
            dst[3] = (isEven? src[3] + heightOfFold : src[3]);
            dst[4] = dst[2];
            dst[5] = (isEven? src[5] - heightOfFold : src[5]);
            dst[6] = dst[0];
            dst[7] = (isEven? src[7] : src[7] - heightOfFold);

            mMatrices[i].setPolyToPoly(src, 0 , dst , 0 , src.length >> 1);
        }

        open.setDuration(speedOfFold);
        close.setDuration(speedOfFold);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
            basePress = event.getX();
        else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            rateOfFold += (event.getX() - basePress) / drawHeight;
            basePress = event.getX();
            if (rateOfFold > 1)
                rateOfFold = 1;
            else if (rateOfFold < 0)
                rateOfFold = 0;
            init();
            invalidate();
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_UP){
            baseRate = rateOfFold;
            if (rateOfFold >= 1-animationRate) {
                open.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        rateOfFold = (float)animation.getAnimatedValue() * (1 - baseRate) + baseRate;
                        init();
                        invalidate();
                    }
                });
                open.start();
            }
            else if (rateOfFold < animationRate){
                close.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        rateOfFold = (float)animation.getAnimatedValue() * (baseRate);
                        init();
                        invalidate();
                    }
                });
                close.start();
            }
        }
        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rateOfFold == 0) return;
        for (int i = 0; i < mMatrices.length; i++){
            canvas.save();
            canvas.concat(mMatrices[i]);
            canvas.clipRect(i * lengthOfFoldPiece , 0 , (i+1) * lengthOfFoldPiece , drawHeight);
            canvas.drawBitmap(mBitmap,null,new RectF(0,0,drawWidth,drawHeight),null);
            canvas.translate(i * lengthOfFoldPiece , 0);
            if (i%2 == 0)
                canvas.drawRect(0,0,lengthOfFoldPiece,drawHeight,mSolidPaint);
            else
                canvas.drawRect(0,0,lengthOfFoldPiece,drawHeight,mShadowPaint);
            canvas.restore();
        }
    }
}
