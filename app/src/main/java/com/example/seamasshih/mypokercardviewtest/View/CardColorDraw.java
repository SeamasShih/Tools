package com.example.seamasshih.mypokercardviewtest.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class CardColorDraw extends View {
    public CardColorDraw(Context context , @Nullable AttributeSet attributeSet){
        super(context,attributeSet);
        initPath();
        initPaint();
    }

    Path pathClubs = new Path();
    Path pathDiamonds = new Path();
    Path pathHearts = new Path();
    Path pathSpades = new Path();
    Paint paintBlack = new Paint();
    Paint paintRed = new Paint();

    int vl = 100;

    private void initPath(){
        float clubR = vl*0.26f;
        float[] clubC1 = {0 , -vl/2+clubR};
        float[] clubC2 = {-vl/2+clubR , vl/2 - vl/8 - clubR};
        float[] clubC3 = {-clubC2[0] , clubC2[1]};
        pathClubs.addCircle(clubC1[0],clubC1[1],clubR, Path.Direction.CCW);
        pathClubs.addCircle(clubC2[0],clubC2[1],clubR, Path.Direction.CCW);
        pathClubs.addCircle(clubC3[0],clubC3[1],clubR, Path.Direction.CCW);
        pathClubs.moveTo(0,0);
        pathClubs.lineTo(-vl/10, vl/2);
        pathClubs.lineTo(vl/10, vl/2);
        pathClubs.close();

        pathDiamonds.moveTo(vl/3,0);
        pathDiamonds.lineTo(0,-vl/2);
        pathDiamonds.lineTo(-vl/3,0);
        pathDiamonds.lineTo(0,vl/2);

        float[] hCtrlR1 = {vl/2,-vl/2 -vl/5};
        float[] hCtrlR2 = {vl/2 + vl/3,vl/4};
        float[] hCtrlL1 = {-hCtrlR1[0],hCtrlR1[1]};
        float[] hCtrlL2 = {-hCtrlR2[0],hCtrlR2[1]};
        pathHearts.moveTo(0,-vl/4);
        pathHearts.cubicTo(hCtrlR1[0],hCtrlR1[1],hCtrlR2[0],hCtrlR2[1],0,vl/2);
        pathHearts.moveTo(0,-vl/4);
        pathHearts.cubicTo(hCtrlL1[0],hCtrlL1[1],hCtrlL2[0],hCtrlL2[1],0,vl/2);

        float[] sCtrlR1 = { vl/4, vl*0.8f};
        float[] sCtrlR2 = { vl,    0};
        float[] sCtrlL1 = {-sCtrlR1[0],sCtrlR1[1]};
        float[] sCtrlL2 = {-sCtrlR2[0],sCtrlR2[1]};
        pathSpades.cubicTo(sCtrlR1[0],sCtrlR1[1],sCtrlR2[0],sCtrlR2[1],0,-vl/2);
        pathSpades.cubicTo(sCtrlL2[0],sCtrlL2[1],sCtrlL1[0],sCtrlL1[1],0,0);
        pathSpades.moveTo(0,-vl/4);
        pathSpades.lineTo(-vl/10, vl/2);
        pathSpades.lineTo(vl/10, vl/2);
        pathSpades.close();
    }
    private void initPaint(){
        paintBlack.setColor(Color.BLACK);
        paintBlack.setAntiAlias(true);
        paintBlack.setStyle(Paint.Style.FILL);
        paintRed.setColor(Color.RED);
        paintRed.setAntiAlias(true);
        paintRed.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(vl/2,vl/2);
        canvas.drawPath(pathSpades,paintBlack);
    }
}
