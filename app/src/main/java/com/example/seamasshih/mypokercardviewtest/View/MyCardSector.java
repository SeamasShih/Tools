package com.example.seamasshih.mypokercardviewtest.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.seamasshih.mypokercardviewtest.R;

/**
 * Created by SeamasShih on 2018/3/29.
 */

public class MyCardSector extends View {
    public MyCardSector(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        initial();
        setMyCardImage(0, R.drawable.poker_card_01);
        setMyCardImage(1, R.drawable.poker_card_02);
        setMyCardImage(2, R.drawable.poker_card_03);
        setMyCardImage(3, R.drawable.poker_card_04);
        setMyCardImage(4, R.drawable.poker_card_05);
        setMyCardImage(5, R.drawable.poker_card_06);
        setMyCardImage(6, R.drawable.poker_card_07);
        setMyCardImage(7, R.drawable.poker_card_08);
        setMyCardImage(8, R.drawable.poker_card_09);
        setMyCardImage(9, R.drawable.poker_card_10);
        setMyCardImage(10, R.drawable.poker_card_11);
        setMyCardImage(11, R.drawable.poker_card_12);
        setMyCardImage(12, R.drawable.poker_card_13);
    }

    private void initial(){
        for (int i = 0; i < myCardImage.length; i++)
            myCardImage[i] = null;
        for (int i = 0; i < cardRegion.length; i++)
            cardRegion[i] = new Region();
    }

    private int cardAmount = -1;
    private Bitmap[] myCardImage = new Bitmap[13];
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;
    private int[] viewSideLength = {screenWidth,screenHeight/3};
    private int[] cardSideLength = {71 , 96};
    private int[] rotationCenter = {screenWidth/2,4*screenHeight};
    private Region[] cardRegion = new Region[13];
    private int radius = viewSideLength[1]*3/4;
    private int touching = -1;
    private int siteX = -1;
    private int siteY = -1;
    private enum PRESSSTATUS{CARDCLOSE , CARDLONGCLICK}
    private PRESSSTATUS pressStatus = PRESSSTATUS.CARDCLOSE;
    private long firstClickTime;
    private boolean isSel = false;
    float[] fgr = new float[2];

    public void setMyCardImage(int orderNumber , int resId){
        myCardImage[orderNumber] = BitmapFactory.decodeResource(this.getResources(),resId);
    }

    public void setCardAmount(int cardAmount){
        this.cardAmount = cardAmount;
    }
    public int getCardAmount(){
        return cardAmount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewSideLength[0],viewSideLength[1]);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        fgr[0] = event.getX();
        fgr[1] = event.getY();

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            radius -= viewSideLength[1]*2/5;
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1]))){
                    touching = i;
                }
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1]))){
                        touching = i;
                }
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (event.getRawY() < screenHeight/3)
                Log.d("Seamas","GO!");
            radius += viewSideLength[1] * 2 / 5;
            touching = -1;
            firstClickTime = -1;
        }

        invalidate();
        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path[] mPath = new Path[13];
        cardSideLength[0] = myCardImage[0].getWidth();
        cardSideLength[1] = myCardImage[1].getHeight();
        float l = (float) (rotationCenter[1] - radius - cardSideLength[1]) / (float) (rotationCenter[1] - radius);
        float m = (float) (rotationCenter[1] - radius - cardSideLength[1]) * (float) Math.tan(Math.PI * 1.5 / 180);
        float[] pathPoint = new float[2];

        canvas.save();

        canvas.translate(rotationCenter[0], rotationCenter[1]);
        canvas.rotate(-10.5f);

        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);

//        if (isSel){
//            for (int i = 0; i < myCardImage.length; i++) {
//                canvas.rotate(1.5f);
//                if (i != touching)
//                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
//            }
//            canvas.restore();
//            canvas.drawBitmap(myCardImage[touching], fgr[0] - cardSideLength[0]/2 , fgr[1] - cardSideLength[1]/2 , mPaint);
//        }
        for (int i = 0; i < myCardImage.length; i++) {
            canvas.rotate(1.5f);
            if (i == touching) {
                canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius - cardSideLength[1] / 3, new Paint());
            } else
                canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
        }
        canvas.restore();

        for (int i = 0 ; i < cardRegion.length ; i++) {
            mPath[i] = new Path();
            double theta = Math.PI * (81+1.5*i) / 180;
            pathPoint[0] =(float) (rotationCenter[0] - (rotationCenter[1]-radius)*Math.cos(theta) - cardSideLength[0]/2*Math.sin(theta));
            pathPoint[1] =(float) (rotationCenter[1] - (rotationCenter[1]-radius)*Math.sin(theta) + cardSideLength[0]/2*Math.cos(theta));
            mPath[i].moveTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] += cardSideLength[1]*Math.cos(theta);
            pathPoint[1] += cardSideLength[1]*Math.sin(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] += cardSideLength[0]*Math.sin(theta);
            pathPoint[1] -= cardSideLength[0]*Math.cos(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            pathPoint[0] -= cardSideLength[1]*Math.cos(theta);
            pathPoint[1] -= cardSideLength[1]*Math.sin(theta);
            mPath[i].lineTo(pathPoint[0], pathPoint[1]);
            mPath[i].close();
            cardRegion[i].setPath(mPath[i],new Region(0,0,screenWidth,screenHeight));
            if (i > 0)
                cardRegion[i-1].op(cardRegion[i],Region.Op.DIFFERENCE);
        }
    }
    private void drawRegion(Canvas canvas,Region rgn,Paint paint)
    {
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();

        while (iter.next(r)) {
            canvas.drawRect(r, paint);
        }
    }

}
