package net.maiatday.a7minworkout.states;

import android.content.Context;
import android.os.CountDownTimer;

/**
 * Workout context for states. Also acts as presenter responding to VP.Action and reports back to VP.Update
 * Created by maia on 2017/03/27.
 */

public class Workout implements VP.Action {
    private final Context context;
    State offState;
    State prepareState;
    State embrionicState;
    State creepingState;
    State crawlingState;
    State standingState;
    State walkingState;
    State upDownState;
    State laughingState;

    private State state;
    private StateCountDownTimer timer;

    VP.Update update;

    public Workout(Context context, VP.Update update) {
        this.context = context.getApplicationContext();
        offState = new OffState(this);
        prepareState = new PrepareState(this);
        embrionicState = new EmbrionicState(this);
        creepingState = new CreepingState(this);
        crawlingState = new CrawlingState(this);
        standingState = new StandingState(this);
        walkingState = new WalkingState(this);
        upDownState = new UpDownState(this);
        laughingState = new LaughingState(this);
        this.update = update;
        setState(offState);
    }


    public void setState(State state) {
        this.state = state;
        update.setStatus(context.getString(state.getTitleId()));
        update.setStatusImage(state.getImageId());
        if (state.hasTimer()) {
            update.setStarted(true);
            this.timer = new StateCountDownTimer(state);
            timer.start();
        } else {
            update.setStarted(false);
        }
    }

    @Override
    public void start() {
        setState(prepareState);
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        setState(offState);
    }

    @Override
    public boolean running() {
        return state.hasTimer();
    }

    class StateCountDownTimer extends CountDownTimer {
        State currentState;

        public StateCountDownTimer(State currentState) {
            super(currentState.getLengthMs(), state.getTickMs());
            this.currentState = currentState;
        }

        private StateCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long seconds = l / 1000;
            update.setSecondsLeft(seconds);
            if (seconds == currentState.getWarningS()) {
                update.notifyWarning(currentState.getNextString(context));
            }
            currentState.onTick(l);
        }

        @Override
        public void onFinish() {
            update.notifyFinish();
            currentState.onFinish();
        }
    }
}
