package net.maiatday.a7minworkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
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

import net.maiatday.a7minworkout.states.VP;
import net.maiatday.a7minworkout.states.Workout;

import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.imageView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        statusText = (TextView) findViewById(R.id.status_text);
        secondsText = (TextView) findViewById(R.id.seconds_text);
        nextText = (TextView) findViewById(R.id.next_text);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workout.running()) {
                    workout.stop();
                } else {
                    workout.start();
                }
            }
        });
        workout = new Workout(this, this);
        setValuesFromSettings();
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
//            case R.id.action_history:
//                showHistory();
//                return true;
//            case R.id.action_about:
//                showAbout();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTINGS) {
            workout.stop();
            setValuesFromSettings();
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
    }

    private void showAbout() {
        startActivity(AboutActivity.newIntent(this));
    }

    private void showHistory() {

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
    public void notifyWarning(String upNext) {
        if (mustWarn) {
            if (warnTone != null) {
                warnTone.play();
            }
        }
        if (mustWarnVibrate) {
            vibrator.vibrate(100);
        }
        nextText.setText(upNext);
    }

    @Override
    public void notifyFinish() {
        if (mustChange) {
            if (changeTone != null) {
                changeTone.play();
            }
        }
        if (mustChangeVibrate) {
            vibrator.vibrate(300);
        }
    }
}
