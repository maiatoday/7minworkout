package net.maiatday.a7minworkout.states;

/**
 * Created by maia on 2017/03/27.
 */

public interface VP {
    interface Action {
        void start();
        void stop();

        boolean running();
    }

    interface Update {
        void setStatus(String status);
        void setStatusImage(int id);
        void setSecondsLeft(long seconds);
        void setStarted(boolean started);
        void notifyWarning(String upNext);
        void notifyFinish();
    }
}
