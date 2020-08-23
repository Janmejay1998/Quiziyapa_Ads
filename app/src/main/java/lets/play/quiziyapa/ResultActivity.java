package lets.play.quiziyapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    public static String a;
    public static String t;
    public static String k;
    TextView textView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView = findViewById(R.id.textView);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Arizonia-Regular.ttf");
        textView.setText("\t Remarks \t\t\t\t\t\t\t\t\t\t Score");
        textView.setTypeface(typeface);

        try {
            Toolbar toolbar = findViewById(R.id.resultBar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String str = mPrefs.getString("score","null");
        String total = mPrefs.getString("total","null");

        a = str;
        t = total;

        Scorelist scorelist =new Scorelist(RESULT());
        scorelist.TOTAL(t);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        MyListAdapter adapter = new MyListAdapter(scorelist.getCj(),scorelist.getN());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public List<String> RESULT() {

        char[] ch = new char[a.length()];
        List<String> cj = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {
            ch[i] = a.charAt(i);
            if(Character.isDigit(ch[i])){
                cj.add(String.valueOf(ch[i]));
            }
        }
        return cj;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ResultActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}