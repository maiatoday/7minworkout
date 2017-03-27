package net.maiatday.a7minworkout.states;

import android.content.Context;

import net.maiatday.a7minworkout.R;

/**
 * Created by maia on 2017/03/27.
 */

class PrepareState implements State {
    private final Workout workout;

    public PrepareState(Workout workout) {
        this.workout = workout;
    }

    @Override
    public int getTitleId() {
        return R.string.prepare;
    }

    @Override
    public int getImageId() {
        return R.drawable.embrionic; //TODO get a prepare icon
    }

    @Override
    public long getTickMs() {
        return TICK_MS;
    }

    @Override
    public long getLengthMs() {
        return LENGTH_MS/2;
    }

    @Override
    public long getWarningS() {
        return WARN_S;
    }

    @Override
    public boolean hasTimer() {
        return true;
    }

    @Override
    public void onTick(long l) {
        // nothing to do
    }

    @Override
    public void onFinish() {
        workout.setState(workout.embrionicState);
    }

    @Override
    public String getNextString(Context context) {
        return "next - "+ context.getString(R.string.embrionic);
    }
}
