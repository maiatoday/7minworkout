package net.maiatday.a7minworkout.states;

import android.content.Context;

/**
 * State interface
 * Created by maia on 2017/03/27.
 */

public interface State {
    long TICK_MS = 1000;
    long LENGTH_MS = 60000;
    long WARN_S = 5;

    int getTitleId();
    int getImageId();
    long getTickMs();
    long getLengthMs();
    long getWarningS();
    boolean hasTimer();
    void onTick(long l);
    void onFinish();

    String getNextString(Context context);
}
