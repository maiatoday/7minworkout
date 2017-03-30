package net.maiatday.a7minworkout.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Remember the streak
 * Created by maia on 2017/03/30.
 */

@RealmClass
public class Streak implements RealmModel {
    int streak;
    Record lastWorkout;

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public Record getLastWorkout() {
        return lastWorkout;
    }

    public void setLastWorkout(Record lastWorkout) {
        this.lastWorkout = lastWorkout;
    }
}
