package lets.play.quiziyapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {

    private GridView sets_grid;
    private FirebaseFirestore firestore;
    public static int category_id;
    private Dialog loading;
    WebView webView;
    AdView setsAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        try {
            String title = getIntent().getStringExtra("CATEGORY");
            category_id = getIntent().getIntExtra("CATEGORY_ID", 1);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            sets_grid = findViewById(R.id.sets_gridview);
            webView = findViewById(R.id.gridBg);

            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webView.loadUrl("file:///android_asset/splash.html");

            loading = new Dialog(SetsActivity.this);
            loading.setContentView(R.layout.loading_bar);
            loading.setCancelable(false);
            loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.show();

            firestore = FirebaseFirestore.getInstance();
            loadSets();

            setsAd = findViewById(R.id.setsAd);

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            setsAd.loadAd(adRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSets() {
        try {
            firestore.collection("QUIZ").document("CAT " + String.valueOf(category_id))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();

                        if (doc.exists()) {
                            long sets = (long) doc.get("SETS");
                            SetsAdapter adapter = new SetsAdapter((int) sets);
                            sets_grid.setAdapter(adapter);


                        } else {
                            Toast.makeText(getApplicationContext(), "No CAT Document Exists", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    loading.cancel();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            SetsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

