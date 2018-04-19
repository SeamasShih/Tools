package com.example.seamasshih.mypokercardviewtest.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class PlayTableDraw extends View {
    public PlayTableDraw(Context context, @Nullable AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    Paint paintBackGround = new Paint();
    Paint paintEdge = new Paint();
    Paint paintTable = new Paint();
    Paint paintShadow = new Paint();
    Paint paintBlackLine = new Paint();
    Paint paintWhiteLine = new Paint();
    float widthEdge = 0.01f;
    float widthShadow = 0.004f;
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;
    Rect screen = new Rect();
    RectF inner = new RectF();
    RectF whiteLine = new RectF();
    Path shadow = new Path();
    Matrix table = new Matrix();

    private void init(){
        paintBackGround.setStyle(Paint.Style.FILL);
        paintBackGround.setAntiAlias(true);
        paintBackGround.setColor(Color.rgb(192,192,192));
        paintEdge.setStyle(Paint.Style.FILL);
        paintEdge.setAntiAlias(true);
        paintEdge.setColor(Color.rgb(41,36,33));
        paintTable.setStyle(Paint.Style.FILL);
        paintTable.setAntiAlias(true);
        paintTable.setColor(Color.rgb(34,139,34));
        paintShadow.setStyle(Paint.Style.FILL);
        paintShadow.setAntiAlias(true);
        paintShadow.setColor(Color.BLACK);
        paintShadow.setAlpha(30);
        paintBlackLine.setStyle(Paint.Style.STROKE);
        paintBlackLine.setAntiAlias(true);
        paintBlackLine.setColor(Color.BLACK);
        paintWhiteLine.setStyle(Paint.Style.STROKE);
        paintWhiteLine.setAntiAlias(true);
        paintWhiteLine.setColor(Color.WHITE);
        screen.set(0,0,screenWidth,screenHeight);
        inner.set(screenWidth*widthEdge,screenHeight*widthEdge*2,screenWidth*(1-widthEdge),screenHeight*(1-widthEdge*2));
        whiteLine.set(screenWidth/6,screenHeight/5,screenWidth*5/6,screenHeight*4/5);

        shadow.moveTo(screenWidth*widthEdge                  , screenHeight*widthEdge*2);
        shadow.lineTo(screenWidth*(1-widthEdge)              , screenHeight*widthEdge*2);
        shadow.lineTo(screenWidth*(1-widthEdge)              , screenHeight*(1-widthEdge*2));
        shadow.lineTo(screenWidth*(1-widthEdge-widthShadow)  , screenHeight*(1-widthEdge*2));
        shadow.lineTo(screenWidth*(1-widthEdge-widthShadow)  , screenHeight*(widthEdge*2+widthShadow*2));
        shadow.lineTo(screenWidth*(widthEdge+widthShadow)    , screenHeight*(widthEdge*2+widthShadow*2));
        shadow.lineTo(screenWidth*(widthEdge+widthShadow)    , screenHeight*(1-widthEdge*2));
        shadow.lineTo(screenWidth*(widthEdge)                , screenHeight*(1-widthEdge*2));
        shadow.close();

        float[] src = {
                0,0,
                screenWidth,0,
                screenWidth,screenHeight,
                0,screenHeight
        };
        float[] dst = {
                screenWidth/10,screenHeight/6,
                screenWidth*9/10,screenHeight/6,
                screenWidth*29/30,screenHeight*34/35,
                screenWidth/30,screenHeight*34/35,
        };
        table.setPolyToPoly(src,0,dst,0,src.length>>1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(screen,paintBackGround);
        canvas.concat(table);
        canvas.drawRect(screen,paintEdge);
        canvas.drawRect(inner,paintTable);
        canvas.drawPath(shadow,paintShadow);
        canvas.drawRect(whiteLine,paintWhiteLine);
    }
}
