package com.example.quickapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new JumpGame());
    }

    class JumpGame extends View {
        Paint p = new Paint();
        float playerY, velocity, ground;
        float obstacleX, obstacleW = 60;
        float playerX = 120;
        boolean jumping = false;
        boolean gameOver = false;
        int score = 0;

        JumpGame() {
            super(MainActivity.this);
            p.setAntiAlias(true);
            obstacleX = 900;
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);

            float w = getWidth();
            float h = getHeight();
            ground = h - 100;

            if (playerY == 0) playerY = ground - 60;

            // Background
            c.drawColor(Color.rgb(135, 206, 235));

            // Ground
            p.setColor(Color.GREEN);
            c.drawRect(0, ground, w, h, p);

            // Player
            p.setColor(Color.RED);
            c.drawRect(playerX, playerY, playerX + 60,
                    playerY + 60, p);

            // Obstacle
            p.setColor(Color.BLACK);
            c.drawRect(obstacleX, ground - 60,
                    obstacleX + obstacleW, ground, p);

            // Score
            p.setColor(Color.BLACK);
            p.setTextSize(45);
            c.drawText("Score: " + score, 40, 60, p);

            if (!gameOver) {
                velocity += 1.5f;
                playerY += velocity;

                if (playerY >= ground - 60) {
                    playerY = ground - 60;
                    velocity = 0;
                    jumping = false;
                }

                obstacleX -= 10;

                if (obstacleX < -obstacleW) {
                    obstacleX = w + 200;
                    score++;
                }

                // Collision
                if (obstacleX < playerX + 60 &&
                    obstacleX + obstacleW > playerX &&
                    playerY + 60 > ground - 60) {
                    gameOver = true;
                }

                postInvalidateDelayed(16);
            }

            if (gameOver) {
                p.setColor(Color.WHITE);
                p.setTextSize(65);
                c.drawText("GAME OVER", w/2 - 180, h/2, p);
                p.setTextSize(35);
                c.drawText("Tap to Restart", w/2 - 120,
                        h/2 + 70, p);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (gameOver) {
                    gameOver = false;
                    score = 0;
                    obstacleX = getWidth() + 200;
                    playerY = ground - 60;
                    velocity = 0;
                } else if (!jumping) {
                    velocity = -22;
                    jumping = true;
                }
                invalidate();
                return true;
            }
            return true;
        }
    }
}
