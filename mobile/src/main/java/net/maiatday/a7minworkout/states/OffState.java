package net.maiatday.a7minworkout.states;

import android.content.Context;

import net.maiatday.a7minworkout.R;

/**
 * Created by maia on 2017/03/27.
 */

class OffState implements State {
    private final Workout workout;

    public OffState(Workout workout) {
        this.workout = workout;
    }

    @Override
    public int getTitleId() {
        return R.string.off;
    }

    @Override
    public int getImageId() {
        return R.drawable.embrionic; //TODO need an off image
    }

    @Override
    public long getTickMs() {
        return -1;
    }

    @Override
    public long getLengthMs() {
        return -1;
    }

    @Override
    public long getWarningS() {
        return -1;
    }

    @Override
    public boolean hasTimer() {
        return false;
    }

    @Override
    public void onTick(long l) {
        // do nothing, no timer
    }

    @Override
    public void onFinish() {
        // do nothing, no timer
    }

    @Override
    public String getNextString(Context context) {
        return "";
    }
}
