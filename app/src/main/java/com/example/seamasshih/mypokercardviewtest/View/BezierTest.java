package com.example.seamasshih.mypokercardviewtest.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BezierTest extends View {
    public BezierTest(Context context,@Nullable AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }
    Point[] points = new Point[4];
    Paint paintPoint = new Paint();
    Paint paintLine = new Paint();
    Path[] pathsPoint = new Path[4];
    Region[] regionsPoint = new Region[4];
    final Path pathBezier = new Path();
    float pointRadius = 60;
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;
    int target;

    private void init(){
        for (int i = 0 ; i < points.length ; i++){
            points[i] = new Point();
        }
        points[0].x = screenWidth/4;
        points[0].y = screenHeight*3/4;
        points[1].x = screenWidth/4;
        points[1].y = screenHeight/4;
        points[2].x = screenWidth*3/4;
        points[2].y = screenHeight/4;
        points[3].x = screenWidth*3/4;
        points[3].y = screenHeight*3/4;
        paintPoint.setAntiAlias(true);
        paintPoint.setColor(Color.RED);
        paintPoint.setStyle(Paint.Style.FILL);
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.GRAY);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(3);
        pathBezier.moveTo(points[0].x,points[0].y);
        pathBezier.cubicTo(points[1].x,points[1].y,points[2].x,points[2].y,points[3].x,points[3].y);
        for (int i = 0 ; i < pathsPoint.length ; i++){
            regionsPoint[i] = new Region();
            pathsPoint[i] = new Path();
        }
        setPointRegion();
    }
    private void setPointRegion(){
        for (int i = 0 ; i < pathsPoint.length ; i++){
            pathsPoint[i].reset();
        }
        pathsPoint[0].addCircle(points[0].x,points[0].y,pointRadius, Path.Direction.CCW);
        pathsPoint[1].addCircle(points[1].x,points[1].y,pointRadius, Path.Direction.CCW);
        pathsPoint[2].addCircle(points[2].x,points[2].y,pointRadius, Path.Direction.CCW);
        pathsPoint[3].addCircle(points[3].x,points[3].y,pointRadius, Path.Direction.CCW);
        for (int i = 0 ; i < regionsPoint.length ; i++){
            regionsPoint[i].setEmpty();
            regionsPoint[i].setPath(pathsPoint[i] , new Region(0,0,screenWidth,screenHeight));
        }
        pathBezier.reset();
        pathBezier.moveTo(points[0].x,points[0].y);
        pathBezier.cubicTo(points[1].x,points[1].y,points[2].x,points[2].y,points[3].x,points[3].y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                for (int i = 0 ; i < regionsPoint.length ; i++){
                    if (regionsPoint[i].contains((int)x,(int)y)){
                        target = i;
                    }
                }
                if (target == -1) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                points[target].x = (int)x;
                points[target].y = (int)y;
                setPointRegion();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                target = -1;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(points[0].x,points[0].y,pointRadius,paintPoint);
        canvas.drawCircle(points[1].x,points[1].y,pointRadius,paintPoint);
        canvas.drawCircle(points[2].x,points[2].y,pointRadius,paintPoint);
        canvas.drawCircle(points[3].x,points[3].y,pointRadius,paintPoint);
        canvas.drawPath(pathBezier,paintLine);
    }
}
