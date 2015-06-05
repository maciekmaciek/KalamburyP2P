package com.kalambury.kalamburyp2p.Components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.kalambury.kalamburyp2p.Activities.GameScreen;
import com.kalambury.kalamburyp2p.Communication.DeviceDetailFragment;
import com.kalambury.kalamburyp2p.Communication.FileTransferService;
import com.kalambury.kalamburyp2p.Communication.Utils;
import com.kalambury.kalamburyp2p.Utils.DrawingObject;
import com.kalambury.kalamburyp2p.R;

import java.util.ArrayList;


/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-07-7.

    Problemy z wydajnością, do poprawy
 */

public class PaintView extends View {

    private int currentColor;
    private int currentSize;
    private Path drawingPath;   //currently drawn Path
    private ArrayList<DrawingObject> drawingPoints; //list of Paths with color and size
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    private Bitmap mBitmap;
    private Canvas  mCanvas;
    private Paint   mBitmapPaint;
    private Paint paint;
    private boolean touchable;


    public PaintView(Context c) {
        super(c);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        initialize();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
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
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (!drawingPoints.isEmpty()) {
            DrawingObject d = drawingPoints.get(drawingPoints.size() - 1);
            paint.setColor(d.getColor());
            paint.setStrokeWidth(d.getSize());
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(d.getPath(), paint);
        }
        paint.setColor(currentColor);
        paint.setStrokeWidth(currentSize);
        canvas.drawPath(drawingPath, paint);

        /*for (DrawingObject d : drawingPoints) {
            paint.setColor(d.getColor());
            paint.setStrokeWidth(d.getSize());
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(d.getPath(), paint);
        }
        */
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        drawingPath.reset();
        drawingPath.moveTo(x, y);
        mX = x;
        mY = y;

        DeviceDetailFragment.send(x, y);
    }



    private void touch_move(float x, float y) {


        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawingPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            DeviceDetailFragment.send(x, y);
        }
    }
    private void touch_up() {
        drawingPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(drawingPath, paint);
        // kill this so we don't double draw
        drawingPoints.add(new DrawingObject(drawingPath, currentColor, currentSize));
        drawingPath.reset();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//drawing and hiding game menus
        if(!touchable)
            return false;

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
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                break;
        }
        invalidate();
        return true;
    }

    public void clear() {
        drawingPoints.clear();
        drawingPath = new Path();
        mCanvas.drawColor(Color.WHITE);
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

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

}
