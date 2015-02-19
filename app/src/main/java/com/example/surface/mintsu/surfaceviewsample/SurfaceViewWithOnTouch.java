package com.example.surface.mintsu.surfaceviewsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class SurfaceViewWithOnTouch extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Thread thread;
    private Paint paint = new Paint();
    private int px = 0, py = 0; // 座標
    private int width = 50; // 幅
    private int height = 50; // 高さ
    Random r = new Random();


    public SurfaceViewWithOnTouch(Context context) {
        super(context);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        // サーフェイスホルダー生成
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //サーフェイス生成時にスレッドを開始
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // サーフェイス破棄時にthreadを止める
        thread = null;

    }

    // スレッドの処理
    @Override
    public void run() {
        while(thread != null) {
            Canvas canvas = holder.lockCanvas();
            // ロックされて編集できない状態または、サーフェイスがない場合はnullがかえるらしい
            if (canvas == null) return;
            canvas.drawColor(Color.BLACK);
            canvas.drawRect(px , py, px+width, py+height, paint);
            holder.unlockCanvasAndPost(canvas);
            if (px > getWidth()) px = 0;
            if (py > getHeight()) py = 0;
            px++;
            py++;

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ex = (int) event.getX();
        int ey = (int) event.getY();

        if (ex > px && ex < (px+width) && ey > py && ey < (py+height)) {
            px = r.nextInt(getWidth());
            py = r.nextInt(getHeight());
        }
        return true;
    }
}
