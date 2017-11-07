package cisgvsu.biotowerdefense;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

public class GameSurfaceView extends SurfaceView {

    public GameSurfaceView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameSurfaceView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Find the screen size
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //this.screenHeight = size.y;
        //this.screenWidth = size.x;

        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        // Get the bitmaps that we'll draw
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Bitmap bac = BitmapFactory.decodeResource(getResources(), R.drawable.bacteria);

        // Scale the background
        bg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, false);

        final DrawingThread thread = new DrawingThread(getHolder(), bg, bac, screenWidth, screenHeight);

        // Set up SurfaceHolder for drawing
        SurfaceHolder holder = getHolder();
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
        private int width;
        private int height;
        private Game game;

        //Variables for displaying score
        private Paint paintText;
        private int renderedScore;
        private String renderedScoreString;

        public DrawingThread(SurfaceHolder holder, Bitmap bg, Bitmap bacBmp, int width, int height) {
            this.paintText = new Paint();
            paintText.setTextSize(50);
            paintText.setColor(Color.DKGRAY);

            this.holder = holder;
            this.bg = bg;
            this.bacBmp = bacBmp;
            this.width = width;
            this.height = height;
            this.game = new Game();
            game.startGame();
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
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(bg, 0, 0, null);

                ArrayList<Bacteria> allBacteria = game.getAllBacteria();
                for (Bacteria bac : allBacteria) {
                    if (!bac.isInitialPositionSet()) {
                        bac.setX(this.width + 10);
                        bac.setY(300);
                        bac.setInitialPositionSet(true);
                    }
                    canvas.drawBitmap(bacBmp, bac.getX(), bac.getY(), null);
                    moveBacteria(bac);
                }
                canvas.drawText(getScoreString(), 100, 100, paintText);
                Log.d("BAC", "" + allBacteria.size());
            }
        }

        public void moveBacteria(Bacteria bacteria) {
            if (bacteria.getX() > -100) {
                if ((bacteria.getX() > 925 && bacteria.getY() < 375) || bacteria.getY() > 750) {
                    bacteria.setX(bacteria.getX() - 5);
                } else {
                    bacteria.setY(bacteria.getY() + 5);
                }
            } else {
                bacteria.setOnScreen(false);
            }
        }

        private String getScoreString() {
            //Only create new score string for new scores to help with garbage collector problems
            if (game.getScore() != this.renderedScore || this.renderedScoreString == null) {
                this.renderedScore = game.getScore();
                this.renderedScoreString = "Score: " + game.getScore();
            }
            return renderedScoreString;
        }
    }
}