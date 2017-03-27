package net.maiatday.a7minworkout;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.maiatday.a7minworkout.states.VP;
import net.maiatday.a7minworkout.states.Workout;

public class MainActivity extends AppCompatActivity implements VP.Update {

    private TextView statusText;
    private Vibrator vibrator;
    private Button button;
    private ImageView image;
    private TextView secondsText;

    VP.Action workout;
    private TextView nextText;

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
        vibrator.vibrate(100);
        nextText.setText(upNext);
    }

    @Override
    public void notifyFinish() {
        vibrator.vibrate(300);
    }
}
