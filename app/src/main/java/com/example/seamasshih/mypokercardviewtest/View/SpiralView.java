package com.example.seamasshih.mypokercardviewtest.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class SpiralView extends View {
    public SpiralView(Context context ,@Nullable AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    float sr = 0f;
    Path pathSpiral = new Path();
    Paint paintBlue = new Paint();

    private void init(){
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setDuration(100);
//        valueAnimator.setFloatValues(0,1);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                final float r = (float)animation.getAnimatedValue();
//                pathSpiral.addArc(-r*100,-r*100,r*100,r*100,sr*720,r*720);
//                sr = r;
//                invalidate();
//            }
//        });
//        valueAnimator.start();
        int dense = 6;
        float rotate = 360;
        for (int i = 0; i < dense; i++){
            final float r = (float)(i)/dense;
            pathSpiral.addArc(-r*100,-r*100,r*100,r*100,r*rotate,rotate/dense);
        }
        paintBlue.setColor(Color.BLUE);
        paintBlue.setStyle(Paint.Style.STROKE);
        paintBlue.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(500,500);
        canvas.drawPath(pathSpiral,paintBlue);
    }
}
