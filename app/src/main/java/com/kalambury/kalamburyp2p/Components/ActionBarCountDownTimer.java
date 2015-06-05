package com.kalambury.kalamburyp2p.Components;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;

import com.kalambury.kalamburyp2p.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kuba on 2015-06-04.
 */
public class ActionBarCountDownTimer extends CountDownTimer{

    private long millisToCountDown;
    private MenuItem timer;
    private Status status;

    public ActionBarCountDownTimer(long millisInFuture, long countDownInterval, MenuItem timer){
        super(millisInFuture, countDownInterval);
        this.timer = timer;
        this.status = Status.NOT_STARTED;
    }

    @Override
    public void onTick(long untilFinished) {
        status = Status.STARTED;
        millisToCountDown = untilFinished;
        long minutes = (TimeUnit.MILLISECONDS.toMinutes(millisToCountDown)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisToCountDown)));
        long seconds = (TimeUnit.MILLISECONDS.toSeconds(millisToCountDown)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisToCountDown)));

        String time = (minutes < 10 ? "0"+minutes : minutes) +":"+ (seconds < 10 ? "0"+seconds : seconds);

        timer.setTitle(time);
    }

    @Override
    public void onFinish() {
        status = Status.DONE;
        timer.setTitle(R.string.timer_end);
    }

    public long getMillisToCountDown() {
        return millisToCountDown;
    }

    public boolean isRunning(){
        return status == Status.STARTED;
    }

    private enum Status{
        DONE,
        STARTED,
        NOT_STARTED
    }
}
