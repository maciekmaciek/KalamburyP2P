package com.kalambury.kalamburyp2p.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.kalambury.kalamburyp2p.Activities.GameScreen;
import com.kalambury.kalamburyp2p.Utils.DrawingObject;
import com.kalambury.kalamburyp2p.R;

import java.util.ArrayList;


/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-07-7.

    Problemy z wydajnością, do poprawy
 */

public class PaintView extends SurfaceView implements SurfaceHolder.Callback {

    private int currentColor;
    private int currentSize;
    private Path drawingPath;   //currently drawn Path
    private ArrayList<DrawingObject> drawingPoints; //list of Paths with color and size
    private Paint paint;


    public PaintView(Context context) {
        super(context);
        initialize();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize()   //constructor help
    {
        drawingPath = new Path();
        drawingPoints = new ArrayList<DrawingObject>();
        currentSize = getResources().getInteger(R.integer.init_size) + getResources().getInteger(R.integer.size_difference);
        currentColor = Color.BLACK;
        paint = new Paint(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

    }
    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouchEvent(MotionEvent event) {    //drawing and hiding game menus
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                byte hide = ((GameScreen) getContext()).getMenuVisibility();
                if (hide == 1) {
                    ((GameScreen) getContext()).findViewById(R.id.drawing_settings).setVisibility(View.GONE);
                    ((GameScreen) getContext()).setMenuVisibility((byte) 0);
                } else if (hide == 2) {
                    ((GameScreen) getContext()).findViewById(R.id.history_view).setVisibility(View.GONE);
                    ((GameScreen) getContext()).setMenuVisibility((byte) 0);
                }

                drawingPath.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                drawingPath.lineTo(x, y);
                break;
            default:
                return false;

        }
        drawingPoints.add(new DrawingObject(drawingPath, currentColor, currentSize));
        invalidate();
        return false; //draw
    }

    protected void onDraw(Canvas canvas) {
        for (DrawingObject d : drawingPoints) {
            paint.setColor(d.getColor());
            paint.setStrokeWidth(d.getSize());
            canvas.drawPath(d.getPath(), paint);
        }
        paint.setColor(currentColor);
    }

    public void clear() {
        drawingPoints.clear();
        drawingPath = new Path();
        invalidate();
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
        paint.setColor(currentColor);
        drawingPath = new Path();
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        drawingPath = new Path();
        this.currentSize = currentSize;
    }
}
