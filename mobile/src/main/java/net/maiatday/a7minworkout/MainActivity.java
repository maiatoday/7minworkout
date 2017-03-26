package net.maiatday.a7minworkout;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean started;
    private CountDownTimer current;

    private CountDownTimer embrionic = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("embrionic: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.creeping);
            current = creeping;
            creeping.start();
        }
    };

    private CountDownTimer creeping = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("creeping: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.crawling);
            current = crawling;
            crawling.start();
        }
    };

    private CountDownTimer crawling = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("crawling: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.standing);
            current = standing;
            standing.start();
        }
    };

    private CountDownTimer standing = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("standing: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.walking);
            current = walking;
            walking.start();
        }
    };

    private CountDownTimer walking = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("walking: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.updown);
            current = updown;
            updown.start();
        }
    };

    private CountDownTimer updown = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("up down: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            vibrator.vibrate(300);
            image.setImageResource(R.drawable.laugh);
            current = laughing;
            laughing.start();
        }
    };

    private CountDownTimer laughing = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            statusText.setText("laughing: " + millisUntilFinished / 1000);
            if (millisUntilFinished == 5000) {
                vibrator.vibrate(100);
            }
        }

        public void onFinish() {
            statusText.setText("done!");
            button.setText("go");
            vibrator.vibrate(300);
        }
    };
    private TextView statusText;
    private Vibrator vibrator;
    private Button button;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.imageView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        statusText = (TextView) findViewById(R.id.status_text);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started) {
                    stopWorkout();
                } else {
                    startWorkout();
                }
            }
        });
    }

    private void stopWorkout() {
        button.setText("go");
        started = false;
        vibrator.vibrate(500);
        current.cancel();
    }

    private void startWorkout() {
        button.setText("stop");
        image.setImageResource(R.drawable.embrionic);
        started = true;
        vibrator.vibrate(500);
        current = embrionic;
        embrionic.start();
    }


}
