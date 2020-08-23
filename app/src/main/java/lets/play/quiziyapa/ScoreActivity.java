package lets.play.quiziyapa;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView score;
    private Button done;
    boolean isClicked;
    Animation myAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.s_score);
        done = findViewById(R.id.s_button);

        String str_score = getIntent().getStringExtra("SCORE");
        score.setText(str_score);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    isClicked = true;
                    myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    done.startAnimation(myAnim);

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    Handler h = new Handler();
                    h.postDelayed(r,1500);

                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        isClicked = false;
    }
}