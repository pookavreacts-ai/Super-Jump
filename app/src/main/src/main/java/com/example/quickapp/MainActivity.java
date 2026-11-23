package com.example.quickapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView());
    }

    class GameView extends View {
        Paint p = new Paint();
        GameView() { super(MainActivity.this); }
        protected void onDraw(Canvas c) {
            c.drawColor(Color.rgb(100,180,255));
            p.setColor(Color.GREEN);
            c.drawRect(0,getHeight()-100,getWidth(),getHeight(),p);
            p.setColor(Color.MAGENTA);
            c.drawCircle(200,getHeight()-150,50,p);
            p.setColor(Color.WHITE);
            p.setTextSize(50);
            c.drawText("SUPER JUMPER",100,100,p);
            p.setTextSize(35);
            c.drawText("Joystick + 4 Buttons coming!",100,170,p);
        }
    }
}
