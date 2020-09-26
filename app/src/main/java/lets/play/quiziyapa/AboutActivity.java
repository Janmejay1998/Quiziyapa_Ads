package lets.play.quiziyapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AboutActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView, textView1, textView2, textView3, textView4, textView5;
    RelativeLayout relativeLayout;
    boolean isClicked;
    Typeface typeface;
    AdView aboutAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        relativeLayout = findViewById(R.id.about);

        imageView = findViewById(R.id.company);

        textView = findViewById(R.id.ctext);
        textView1 = findViewById(R.id.ctext1);
        textView2 = findViewById(R.id.ctext2);
        textView3 = findViewById(R.id.ctext3);
        textView4 = findViewById(R.id.ctext4);
        textView5 = findViewById(R.id.ctext5);

        aboutAd = findViewById(R.id.aboutAd);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        aboutAd.loadAd(adRequest);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    isClicked = true;

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        typeface = Typeface.createFromAsset(getAssets(), "fonts/TheBomb-7B9gw.ttf");
        /*textView.setText("'      'ADJ Creations Team Members'      ' \n\n\n\n\t\t\t\t\t\t\t\t\t\tJanmejay Mohanty \n\n\t\t\t\t\t\t\t\t\t\t''Abhishek Barman \n\n\t\t\t\t\t\t\t\t\t\t'  'Dhaval Sharma \n\n\t\t\t\t\t\t\t\t\t\t'       'S Dileep \n\n\t\t\t\t\t\t\t\t\t\t'     'Ankit Garg");*/
        textView.setTypeface(typeface);
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);
        textView3.setTypeface(typeface);
        textView4.setTypeface(typeface);
        textView5.setTypeface(typeface);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.credit);

        imageView.startAnimation(animation);

        textView.startAnimation(animation);
        textView1.startAnimation(animation);
        textView2.startAnimation(animation);
        textView3.startAnimation(animation);
        textView4.startAnimation(animation);
        textView5.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isClicked = false;
    }
}

