package lets.play.quiziyapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceManager;
import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lets.play.quiziyapa.SetsActivity.category_id;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView q, qno, timer;
    private Button op1,op2,op3,op4;
    private List<Question> questionList;
    private CountDownTimer countDown;
    private int qn;
    public int score;
    private FirebaseFirestore firestore;
    private int sno;
    private Dialog loading;
    ProgressBar ProgressBar, ProgressBar1;
    boolean isClicked;
    private AdView adView;

    MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        q = findViewById(R.id.question);
        qno = findViewById(R.id.quest_no);
        timer = findViewById(R.id.countdown);

        ProgressBar = findViewById(R.id.progressbar);
        ProgressBar1 = findViewById(R.id.progressbar1);

        op1 = findViewById(R.id.option);
        op2 = findViewById(R.id.option2);
        op3 = findViewById(R.id.option3);
        op4 = findViewById(R.id.option4);

        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);

        loading = new Dialog(QuestionActivity.this);
        loading.setContentView(R.layout.loading_bar);
        loading.setCancelable(false);

        mediaPlayer = MediaPlayer.create(this, R.raw.overjoyed);

        AudienceNetworkAds.initialize(this);
        AudienceNetworkInitializeHelper.initialize(this);

        adView = new AdView(this, "1351114035092954_1351623515042006", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer=findViewById(R.id.banner_container1);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        try {
            Objects.requireNonNull(loading.getWindow()).setBackgroundDrawableResource(R.drawable.loading_background);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        sno = getIntent().getIntExtra("SETNO",1);
        firestore = FirebaseFirestore.getInstance();

        getQuestionsList();

        q.setMovementMethod(new ScrollingMovementMethod());

    }

    private long mLastClickTime = 0;

    private void getQuestionsList() {
        questionList = new ArrayList<>();

        firestore.collection("QUIZ").document("CAT " + String.valueOf(category_id))
                .collection("SET" + String.valueOf(sno))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot questions = task.getResult();

                    for(QueryDocumentSnapshot doc : questions) {
                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER"))
                        ));
                    }

                    setQuestion();

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                loading.cancel();

            }
        });


    }

    private void setQuestion() {
        timer.setText(String.valueOf(10));

        q.setText(questionList.get(0).getQuestion());
        op1.setText(questionList.get(0).getOptionA());
        op2.setText(questionList.get(0).getOptionB());
        op3.setText(questionList.get(0).getOptionC());
        op4.setText(questionList.get(0).getOptionD());

        qno.setText(String.valueOf(1) + " OUT OF " +String.valueOf(questionList.size()));

        ProgressBar.setVisibility(View.INVISIBLE);
        startTimer();
        ProgressBar1.setVisibility(View.VISIBLE);
        qn = 0;
    }


    private void startTimer() {
        ProgressBar1.setMax(61000);
        countDown = new CountDownTimer(61000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

                ProgressBar1.setProgress((int) (millisUntilFinished));
                timer.setText(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                ProgressBar.setVisibility(View.VISIBLE);
                ProgressBar1.setVisibility(View.GONE);
                try {
                    changeQuestion();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        countDown.start();
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
        mLastClickTime = SystemClock.elapsedRealtime();

        int sop = 0; //select option

        switch (v.getId()) {
            case R.id.option :
                sop = 1;
                break;
            case  R.id.option2 :
                sop = 2;
                break;
            case R.id.option3 :
                sop = 3;
                break;
            case R.id.option4 :
                sop = 4;
                break;
            default:
        }
        countDown.cancel();
        checkAnswer(sop, v);

    }

    private void checkAnswer(int sop, View view) {

        if(sop == questionList.get(qn).getCorrectAns()) {

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else {
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch (questionList.get(qn).getCorrectAns()) {
                case 1:
                    op1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    op2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    op3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    op4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    changeQuestion();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },2000);

    }

    public void changeQuestion() throws IOException {
        if (qn < questionList.size() - 1) {

            qn ++;

            playanim(q,0,0);
            playanim(op1,0,1);
            playanim(op2,0,2);
            playanim(op3,0,3);
            playanim(op4,0,4);


            qno.setText(String.valueOf(qn+1) + " OUT OF " + String.valueOf(questionList.size()));

            timer.setText(String.valueOf(10));
            ProgressBar.setVisibility(View.INVISIBLE);
            startTimer();
            ProgressBar1.setVisibility(View.VISIBLE);


        }
        else {

            Scorelist scorelist =new Scorelist(score,questionList.size());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            Gson gson = new Gson();
            String json = gson.toJson(scorelist.getList());
            String totalQ = gson.toJson(scorelist.getT());

            editor.putString("score", json);
            editor.putString("total", totalQ);
            editor.commit();

            if(!isClicked) {
                isClicked = true;
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("SCORE", "You Scored \n" + String.valueOf(score) + " out of " + String.valueOf(questionList.size()));
                startActivity(intent);
                finish();
            }

        }
    }

    private void playanim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView)view).setText(questionList.get(qn).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(qn).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(qn).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(qn).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(qn).getOptionD());
                                    break;

                            }

                            if(viewNum != 0)
                                ((Button)view).setBackgroundTintList(AppCompatResources.getColorStateList(view.getContext(), R.color.optionButton));
                            /*((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EE9C03")));*/

                            playanim(view,1,viewNum);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        if (isFinishing()) {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDown.cancel();
        ProgressBar1.setVisibility(View.GONE);
        ProgressBar.setVisibility(View.VISIBLE);
    }
}