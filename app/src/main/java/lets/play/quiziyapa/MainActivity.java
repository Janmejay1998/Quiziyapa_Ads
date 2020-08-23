package lets.play.quiziyapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    WebView sakura1;
    LottieAnimationView play;
    LottieAnimationView score;
    LottieAnimationView settings;
    LottieAnimationView about;
    boolean isClicked;
    private AdView adView;
    private InterstitialAd interstitialAd;

    MediaPlayer smp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sakura1 = findViewById(R.id.sakura);
        play = findViewById(R.id.play);
        score = findViewById(R.id.score);
        settings = findViewById(R.id.settings);
        about = findViewById(R.id.about);

        sakura1.getSettings().setJavaScriptEnabled(true);
        sakura1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        sakura1.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        sakura1.loadUrl("file:///android_asset/sakura.html");

        smp = MediaPlayer.create(this, R.raw.lakey);

        AudienceNetworkAds.initialize(this);
        AudienceNetworkInitializeHelper.initialize(this);

        adView = new AdView(this, "1351114035092954_1351117251759299", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer=findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        interstitialAd = new InterstitialAd(this, "1351114035092954_1351192888418402");

        interstitialAd.loadAd();


        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                            return;
                        }
                        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                        if(interstitialAd.isAdInvalidated()) {
                            return;
                        }

                        interstitialAd.show();


                    }
                });

            }
        }, 10, 300, TimeUnit.SECONDS);

        about.setMinAndMaxFrame(0,44);
        about.playAnimation();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.playAnimation();
                if(!isClicked) {
                    isClicked = true;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                            startActivity(intent);

                        }
                    };

                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 2670); // will be delayed for 2.67 seconds
                }
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score.playAnimation();
                if (!isClicked) {
                    isClicked = true;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                            startActivity(intent);

                        }
                    };

                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 4000); // will be delayed for 4 seconds

                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.playAnimation();
                if (!isClicked) {
                    isClicked = true;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                        }
                    };

                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 1000); // will be delayed for 1 seconds

                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.playAnimation();
                if (!isClicked) {
                    isClicked = true;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                            startActivity(intent);
                        }
                    };

                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 1130); // will be delayed for 1.13 seconds

                }
            }
        });

        }

    @Override
    protected void onStart() {
        super.onStart();
        smp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        smp.pause();
        if (isFinishing()) {
            smp.release();
        }
        isClicked = false;
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
}

