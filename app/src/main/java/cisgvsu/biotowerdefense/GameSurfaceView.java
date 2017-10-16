package cisgvsu.biotowerdefense;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView {

    private SurfaceHolder holder;
    private Bitmap bac;
    private Bitmap bg;
    private int screenWidth;
    private int screenHeight;
    private Bacteria bacteria;

    public GameSurfaceView(Context context) {
        super(context);
        // Find the screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        this.screenHeight = displayMetrics.heightPixels;
        this.screenWidth = displayMetrics.widthPixels;

        // Get the bitmaps that we'll draw
        this.bg = BitmapFactory.decodeResource(getResources(), R.drawable.tower_defense_bg);
        this.bac = BitmapFactory.decodeResource(getResources(), R.drawable.bacteria);

        // Scale the background
        this.bg = Bitmap.createScaledBitmap(this.bg, this.screenWidth, this.screenHeight, false);

        // Make a bacteria object
        this.bacteria = new Bacteria(BacteriaType.pneumonia, 10);
        this.bacteria.setX(this.screenWidth);
        this.bacteria.setY(300);

        final DrawingThread thread = new DrawingThread(getHolder(), this.bg, this.bac,
                this.bacteria, this.screenWidth, this.screenHeight);

        // Set up SurfaceHolder for drawing
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                thread.setRunnable(false);
                while(true) {
                    try {
                        thread.join();
                    } catch(InterruptedException ie) {
                        //Try again and again and again
                    }
                    break;
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                thread.setRunnable(true);
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
        });
    }

    class DrawingThread extends Thread {
        private SurfaceHolder holder;
        private Canvas canvas;
        private boolean run = false;
        private Bitmap bg;
        private Bitmap bacBmp;
        private Bacteria bac;
        private int width;
        private int height;

        public DrawingThread(SurfaceHolder holder, Bitmap bg, Bitmap bacBmp, Bacteria bac, int width, int height) {
            this.holder = holder;
            this.bg = bg;
            this.bacBmp = bacBmp;
            this.bac = bac;
            this.width = width;
            this.height = height;
        }

        public void setRunnable(boolean run) {
            this.run = run;
        }

        @Override
        public void run() {
            while (run) {
                canvas = null;
                try {
                    canvas = holder.lockCanvas(null);

                    synchronized (holder) {
                        draw(canvas);
                    }

                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        /**
         * Draw the background and the bacteria.
         * @param canvas
         */
        public void draw(Canvas canvas) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(bg, 0, 0, null);
            canvas.drawBitmap(bacBmp, bac.getX(), bac.getY(), null);
            moveBacteria(bac);
        }

        public void moveBacteria(Bacteria bacteria) {
            if (bacteria.getX() > -100) {
                if ((bacteria.getX() > 925 && bacteria.getY() < 350) || bacteria.getY() > 750) {
                    bacteria.setX(bacteria.getX() - 10);
                } else {
                    bacteria.setY(bacteria.getY() + 10);
                }
            } else {
                bacteria.setX(this.width);
                bacteria.setY(300);
            }
        }
    }

}

