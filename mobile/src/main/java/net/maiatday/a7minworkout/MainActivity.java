package net.maiatday.a7minworkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.maiatday.a7minworkout.model.Record;
import net.maiatday.a7minworkout.model.Streak;
import net.maiatday.a7minworkout.states.VP;
import net.maiatday.a7minworkout.states.Workout;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

public class MainActivity extends AppCompatActivity implements VP.Update {

    private static final int REQUEST_SETTINGS = 1;
    private TextView statusText;
    private Vibrator vibrator;
    private Button button;
    private ImageView image;
    private TextView secondsText;

    VP.Action workout;
    private TextView nextText;

    boolean mustWarn = false;
    boolean mustWarnVibrate = false;
    Ringtone warnTone;

    boolean mustChange = false;
    boolean mustChangeVibrate = false;
    Ringtone changeTone;
    private FirebaseAnalytics analytics;
    private boolean mustTrackStreak;
    private boolean mustTrackHistory;

    private TextView streakText;
    private TextView streakLabel;

    Realm realm;
    Streak streak;
    private RealmChangeListener<Streak> streakListener = new RealmChangeListener<Streak>() {
        @Override
        public void onChange(Streak element) {
            showStreakUI(element.getStreak());
        }
    };

    private void showStreakUI(int streakVal) {
        if (mustTrackHistory && mustTrackStreak) {
            //Show streak
            streakLabel.setVisibility(View.VISIBLE);
            streakText.setVisibility(View.VISIBLE);
            streakText.setText(String.valueOf(streakVal));
        } else {
            streakLabel.setVisibility(View.GONE);
            streakText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(this);
        setupRealm();
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.imageView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        statusText = (TextView) findViewById(R.id.status_text);
        secondsText = (TextView) findViewById(R.id.seconds_text);
        nextText = (TextView) findViewById(R.id.next_text);
        streakText = (TextView) findViewById(R.id.streak_text);
        streakLabel = (TextView) findViewById(R.id.streak_label);
        button = (Button) findViewById(R.id.button_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workout.running()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "workout_stop");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "workout");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "none");
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    workout.stop();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "workout_start");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "workout");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "none");
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    workout.start();
                }
            }
        });
        workout = new Workout(this, this);
        setValuesFromSettings();
        showStreakUI(streak.getStreak());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_history:
                showHistory();
                return true;
            case R.id.action_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTINGS) {
            workout.stop();
            setValuesFromSettings();
            showStreakUI(streak.getStreak());
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setValuesFromSettings() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        mustChange = prefs.getBoolean("notifications_change", true);
        mustChangeVibrate = prefs.getBoolean("notifications_change_vibrate", true);
        String ringtoneChange = prefs.getString("notifications_change_ringtone", "content://settings/system/notification_sound");
        if (TextUtils.isEmpty(ringtoneChange)) {
            changeTone = null;
        } else {
            Uri uri = Uri.parse(ringtoneChange);
            changeTone = RingtoneManager.getRingtone(this, uri);
        }
        mustWarn = prefs.getBoolean("notifications_warn", true);
        mustWarnVibrate = prefs.getBoolean("notifications_warn_vibrate", true);
        String ringtoneWarn = prefs.getString("notifications_warn_ringtone", "content://settings/system/notification_sound");
        if (TextUtils.isEmpty(ringtoneWarn)) {
            warnTone = null;
        } else {
            Uri uri = Uri.parse(ringtoneWarn);
            warnTone = RingtoneManager.getRingtone(this, uri);
        }
        mustTrackStreak = prefs.getBoolean("record_streak", true);
        mustTrackHistory = prefs.getBoolean("record_history", true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmObject.removeAllChangeListeners(streak);
        realm.close();
    }

    private void showAbout() {
        startActivity(AboutActivity.newIntent(this));
    }

    private void showHistory() {
        workout.stop();
        startActivity(HistoryActivity.newIntent(this));
    }

    private void showSettings() {
        workout.stop();
        startActivityForResult(SettingsActivity.newIntent(this), REQUEST_SETTINGS);
    }


    @Override
    public void setStatus(String status) {
        statusText.setText(status);
    }

    @Override
    public void setStatusImage(int id) {
        image.setImageResource(id);
    }

    @Override
    public void setSecondsLeft(long seconds) {
        secondsText.setText(String.valueOf(seconds));
    }

    @Override
    public void setStarted(boolean started) {
        nextText.setText("");
        if (started) {
            button.setText(R.string.stop);
        } else {
            button.setText(R.string.go);
        }

    }

    @Override
    public void onWarn(String upNext) {
        if (mustWarn) {
            if (warnTone != null) {
                warnTone.play();
            }
            if (mustWarnVibrate) {
                vibrator.vibrate(100);
            }
        }
        nextText.setText(upNext);
    }

    @Override
    public void onChange() {
        if (mustChange) {
            if (changeTone != null) {
                changeTone.play();
            }
            if (mustChangeVibrate) {
                vibrator.vibrate(300);
            }
        }
    }

    @Override
    public void onComplete() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "workout_complete");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "workout");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "none");
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        if (mustTrackHistory) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    String uuid = UUID.randomUUID().toString();
                    Record r = realm.createObject(Record.class, uuid);
                    r.setTimeStamp(new Date());
                    Streak s = realm.where(Streak.class).findFirst();
                    if (mustTrackStreak) {
                        Record previous = s.getLastWorkout();
                        if (previous != null) {
                            long diff = previous.getTimeStamp().getTime() - r.getTimeStamp().getTime();
                            long diffSeconds = diff / 1000 % 60;
                            if (diffSeconds < 48 * 60 * 60) {
                                s.setStreak(s.getStreak() + 1);
                            } else {
                                s.setStreak(0);
                            }
                        } else {
                            s.setStreak(1);
                        }
                    }
                    s.setLastWorkout(r);
                }
            });
        }

    }

    private void setupRealm() {
        realm = Realm.getDefaultInstance();
        streak = realm.where(Streak.class).findFirst();
        if (streak == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Streak s = realm.createObject(Streak.class);
                    s.setStreak(0);
                }
            });
        }
        streak = realm.where(Streak.class).findFirst();
        RealmObject.addChangeListener(streak, streakListener);

    }
}
