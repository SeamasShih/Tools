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
        setRegion();
    }

    private void initial(){
        for (int i = 0; i < myCardImage.length; i++)
            myCardImage[i] = null;
        for (int i = 0; i < cardRegion.length; i++)
            cardRegion[i] = new Region();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        for (int i = 0; i < playedCard.length; i++)
            playedCard[i] = false;
        for (int i = 0; i < mPath.length; i++)
            mPath[i] = new Path();
    }
    private int n = 13;
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
    private Region all = new Region();
    private int radius = viewSideLength[1]*3/4;
    private int r = radius - viewSideLength[1]*2/5;
    private int touching = -1;
    private float sweepAngle = 1.5f;
    private float startAngle = 90 - ((n-1)/2 * sweepAngle);
    private boolean isSel = false;
    float[] fgr = new float[2];
    Paint mPaint = new Paint();
    Path[] mPath = new Path[13];
    boolean[] playedCard = new boolean[13];

    public void setMyCardImage(int orderNumber , int resId){
        myCardImage[orderNumber] = BitmapFactory.decodeResource(this.getResources(),resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewSideLength[0],viewSideLength[1]);
    }

    private void setRegion(){
        cardSideLength[0] = myCardImage[0].getWidth();
        cardSideLength[1] = myCardImage[1].getHeight();
        all.set(0,0,viewSideLength[0],viewSideLength[1]);
        float[] pathPoint = new float[2];
        for (int i = 0 ; i < cardRegion.length ; i++) {
            cardRegion[i].setEmpty();
            mPath[i].reset();
        }
        for (int i = 0 , count = 0; i < cardRegion.length ; i++) {
            if (n == 0) continue;
            while(playedCard[i]) {
                i++;
                if (i == 13) break;
            }
            if (i == 13) break;
            startAngle = 90 - ((n-1)/2 * sweepAngle);
            double theta = Math.PI * (startAngle+sweepAngle*count) / 180;
            count++;
            pathPoint[0] =(float) (rotationCenter[0] - (rotationCenter[1]-r)*Math.cos(theta) - cardSideLength[0]/2*Math.sin(theta));
            pathPoint[1] =(float) (rotationCenter[1] - (rotationCenter[1]-r)*Math.sin(theta) + cardSideLength[0]/2*Math.cos(theta));
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
            all.op(cardRegion[i],Region.Op.DIFFERENCE);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        fgr[0] = event.getX();
        fgr[1] = event.getY();

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            radius -= viewSideLength[1]*2/5;
            Log.d("Seamas"," 141 -  ");
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1]))){
                    touching = i;
                }
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            for (int i = 0; i < cardRegion.length; i ++){
                if (cardRegion[i].contains((int)(fgr[0]),(int)(fgr[1])) && !cardRegion[i].isEmpty()){
                    touching = i;
                    if (isSel) {
                        isSel = false;
                        radius -= viewSideLength[1] * 2 / 5;
                        Log.d("Seamas"," 155 -  ");
                    }
                }
            }
            if (all.contains((int)(fgr[0]),(int)(fgr[1])) && !isSel){
                isSel = true;
                Log.d("Seamas"," 161 +  ");
                radius += viewSideLength[1] * 2 / 5;
            }
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (isSel && touching != -1) {
                playedCard[touching] = true;
                n -= 1;
                setRegion();
            }
            else if(touching != -1) {
                Log.d("Seamas"," 170 + ");
                radius += viewSideLength[1] * 2 / 5;
            }
            isSel = false;
            touching = -1;
        }
        invalidate();
        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (n == 0) return;
        canvas.save();
        canvas.translate(rotationCenter[0], rotationCenter[1]);
        canvas.rotate(-(n+1)/2*sweepAngle);

        if (isSel && touching!=-1){
            for (int i = 0; i < myCardImage.length; i++) {
                canvas.rotate(sweepAngle);
                while(playedCard[i]) {
                    i++;
                    if (i == 13) break;
                }
                if (i == 13) break;
                if (i != touching)
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
            }
            canvas.restore();
            canvas.drawBitmap(myCardImage[touching],fgr[0]-cardSideLength[0]/2,fgr[1]-cardSideLength[1]/2,mPaint);
        }
        else {
            for (int i = 0; i < myCardImage.length; i++) {
                canvas.rotate(sweepAngle);
                while(playedCard[i]) {
                    i++;
                    if (i == 13) break;
                }
                if (i == 13) break;
                if (i == touching)
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius - cardSideLength[1] / 3, new Paint());
                else
                    canvas.drawBitmap(myCardImage[i], -cardSideLength[0] / 2, -rotationCenter[1] + radius, mPaint);
            }
            canvas.restore();
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
