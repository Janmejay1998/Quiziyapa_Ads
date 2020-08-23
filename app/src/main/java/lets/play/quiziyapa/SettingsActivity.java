package lets.play.quiziyapa;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        Preference t,s,e,share;
        SeekBarPreference seek;
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        int max;
        AudioManager audioManager;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            t = findPreference("theme");
            s = findPreference("sound");
            seek = findPreference("sound_manage");
            e = findPreference("email");
            share = findPreference("share");


            try {

                audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE) ;
                max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                t.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (((Boolean) newValue)) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                        }
                        else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                        }
                        return true;

                    }
                });

                s.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (((Boolean) newValue)) {

                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);

                        }
                        else {

                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0, 0);

                        }
                        return true;
                    }
                });

                seek.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                seek.setValue(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

                seek.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (Integer) newValue, 0);
                        return true;
                    }
                });

                e.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String [] email = {"adjcreations16@gmail.com"};
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));                            // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, email);
                        intent.putExtra(Intent.EXTRA_TEXT, "Send Your Queries");
                        startActivity(Intent.createChooser(intent, "Send Email"));
                        return false;
                    }
                });

                share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=lets.play.quiziyapa" );
                        startActivity(Intent.createChooser(sendIntent, "Share Link"));
                        return false;
                    }
                });
                Toast.makeText(getContext(),"Version :" + versionName +" "+ versionCode,Toast.LENGTH_SHORT).show();


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }
}