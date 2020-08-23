package lets.play.quiziyapa;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AboutActivity extends AppCompatActivity {

    TextView textView;
    Typeface typeface;
    AdView aboutAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textView = findViewById(R.id.ctext);
        aboutAd = findViewById(R.id.aboutAd);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        aboutAd.loadAd(adRequest);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/TheBomb-7B9gw.ttf");
        textView.setText("'      'ADJ Creations Team Members'      ' \n\n\n\n\t\t\t\t\t\t\t\t\t\tJanmejay Mohanty \n\n\t\t\t\t\t\t\t\t\t\t''Abhishek Barman \n\n\t\t\t\t\t\t\t\t\t\t'  'Dhaval Sharma \n\n\t\t\t\t\t\t\t\t\t\t'       'S Dileep \n\n\t\t\t\t\t\t\t\t\t\t'     'Ankit Garg");
        textView.setTypeface(typeface);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.credit);
        textView.startAnimation(animation);
    }
}