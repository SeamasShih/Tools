package com.example.seamasshih.mypokercardviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.seamasshih.mypokercardviewtest.View.BidButton;

public class MainActivity extends AppCompatActivity {

    BidButton bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bd = findViewById(R.id.ppp);
        bd.setCenterRGB(0,255,255);
        bd.setViewLength(600);
        bd.setN(13);
    }
}
