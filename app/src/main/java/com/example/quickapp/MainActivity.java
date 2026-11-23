package com.example.quickapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
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
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        float joyX, joyY, knobX, knobY, joyR;
        boolean touchingJoy = false;
        boolean[] pressed = new boolean[4];

        GameView() {
            super(MainActivity.this);
        }

        void circle(Canvas c, float x, float y, float r, int color) {
            p.setColor(color);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(x, y, r, p);
        }

        void text(Canvas c, String s, float x, float y, float size, int color) {
            p.setColor(color);
            p.setTextSize(size);
            p.setTextAlign(Paint.Align.CENTER);
            p.setStyle(Paint.Style.FILL);
            c.drawText(s, x, y, p);
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);

            float w = getWidth();
            float h = getHeight();

            c.drawColor(Color.rgb(18, 20, 35));

            text(c, "ARCADE CONTROLLER", w/2, h*0.12f,
                    30, Color.WHITE);

            text(c, "JOYSTICK + 4 BUTTONS", w/2, h*0.18f,
                    18, Color.LTGRAY);

            joyX = w*0.25f;
            joyY = h*0.65f;
            joyR = w*0.13f;

            // Joystick base
            circle(c, joyX, joyY, joyR, Color.rgb(50,50,70));
            circle(c, joyX, joyY, joyR*0.8f, Color.rgb(25,25,40));

            // Joystick knob
            if (!touchingJoy) {
                knobX = joyX;
                knobY = joyY;
            }

            circle(c, knobX, knobY, joyR*0.45f,
                    Color.rgb(0,180,255));

            text(c, "JOYSTICK", joyX, joyY+joyR*1.6f,
                    18, Color.WHITE);

            // Four buttons
            float bx = w*0.72f;
            float by = h*0.60f;
            float br = w*0.075f;

            int[] colors = {
                Color.rgb(255,60,90),
                Color.rgb(0,220,130),
                Color.rgb(255,180,0),
                Color.rgb(140,80,255)
            };

            String[] names = {"A","B","X","Y"};

            float[] x = {bx, bx+w*0.16f, bx, bx+w*0.16f};
            float[] y = {by, by, by+h*0.14f, by+h*0.14f};

            for(int i=0;i<4;i++) {
                circle(c,x[i],y[i],br*1.25f,Color.DKGRAY);
                circle(c,x[i],y[i],br,
                        pressed[i] ? Color.WHITE : colors[i]);
                text(c,names[i],x[i],y[i]+br*0.35f,
                        br,Color.WHITE);
            }

            text(c,"A  B  X  Y",w*0.80f,h*0.85f,
                    22,Color.WHITE);

            text(c,"TOUCH TO PLAY",w/2,h*0.93f,
                    18,Color.rgb(0,220,255));
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            float w = getWidth();
            float h = getHeight();

            float bx = w*0.72f;
            float by = h*0.60f;
            float br = w*0.075f;

            float[] x = {bx, bx+w*0.16f, bx, bx+w*0.16f};
            float[] y = {by, by, by+h*0.14f, by+h*0.14f};

            float tx = e.getX();
            float ty = e.getY();

            if(e.getAction() == MotionEvent.ACTION_DOWN ||
               e.getAction() == MotionEvent.ACTION_MOVE) {

                touchingJoy = tx < w*0.50f && ty > h*0.35f;

                if(touchingJoy) {
                    float dx = tx-joyX;
                    float dy = ty-joyY;
                    float d = (float)Math.sqrt(dx*dx+dy*dy);

                    if(d > joyR*0.45f) {
                        dx = dx/d*joyR*0.45f;
                        dy = dy/d*joyR*0.45f;
                    }

                    knobX = joyX+dx;
                    knobY = joyY+dy;
                }

                for(int i=0;i<4;i++) {
                    float dx = tx-x[i];
                    float dy = ty-y[i];
                    pressed[i] = Math.sqrt(dx*dx+dy*dy) < br*1.5f;
                }

                invalidate();
                return true;
            }

            if(e.getAction() == MotionEvent.ACTION_UP ||
               e.getAction() == MotionEvent.ACTION_CANCEL) {

                touchingJoy = false;
                knobX = joyX;
                knobY = joyY;

                for(int i=0;i<4;i++)
                    pressed[i] = false;

                invalidate();
                return true;
            }

            return true;
        }
    }
}
