package lets.play.quiziyapa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
    ImageView zig;

    public static List<String> catList = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        zig = findViewById(R.id.zig);

        Glide.with(this).load(R.raw.quiziyapa).into(zig);

        firestore = FirebaseFirestore.getInstance();

        SplashActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                loadData();
            }
        });

    }


    private void loadData() {

        catList.clear();

        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists()) {
                        long count = (long)doc.get("COUNT");
                        for (int i = 1; i <= count; i++) {
                            String catName = doc.getString("CAT " + String.valueOf(i));
                            catList.add(catName);
                        }
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                // To close the CurrentActivity, r.g. SplashActivity
                                finish();

                            }
                        };

                        Handler h = new Handler();
                        // The Runnable will be executed after the given delay time
                        h.postDelayed(r,5210); // will be delayed for 5.210 seconds
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Category Document Exists",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    try {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }



}


