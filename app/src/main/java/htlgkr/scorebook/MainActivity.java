package htlgkr.scorebook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static ListView lv;
    private static LayoutInflater layoutInflater;
    public static RoundAdapter roundAdapter;


    private SharedPreferences prefs1;
    private SharedPreferences prefs2;
    private SharedPreferences prefs3;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener1;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener2;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.playedRounds);
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        bindViewToAdapter();


        // preferences
        prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChanged(prefs1, "darkmode");
        preferenceChanged(prefs2, "allowNotifications");
        preferencesChangeListener1 = (SharedPreferences, key) -> preferenceChanged(prefs1, "darkmode");

        preferencesChangeListener2 = (SharedPreferences, key) -> preferenceChanged(prefs2, "allowNotifications");
        prefs1.registerOnSharedPreferenceChangeListener(preferencesChangeListener1);

        prefs2.registerOnSharedPreferenceChangeListener(preferencesChangeListener3);

        // register the context menu
        registerForContextMenu(lv);


        bindViewToAdapter();
    }

    public void bindViewToAdapter() {
        List<Round> roundList = new ArrayList<>(new HashSet<>(getRounds()));

        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        roundAdapter = new RoundAdapter(roundList, layoutInflater);

        lv.setAdapter(roundAdapter);
    }

    static List<Round> rounds = new ArrayList<>();

    public static List<Round> getRounds() {
        return rounds;
    }

    public static void addRound(Round round) {
        rounds.add(round);
    }


    //action bar options//////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //sets the menu (plus)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //if plus is selected
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, NewRound.class);  //starts NewRound
            startActivity(intent);


            Toast.makeText(this, "add Round", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.menu_preferences) {

            Intent intent = new Intent(this, Settings.class);  //starts Settings
            startActivity(intent);


            Toast.makeText(this, "show settings", Toast.LENGTH_SHORT).show();
        }



        return super.onOptionsItemSelected(item);

    }





    //preferences////////////////////////////////////////////////////////////////////////////////////////
    private void preferenceChanged(SharedPreferences prefs, String key) {
        Map<String, ?> allEntries = prefs.getAll();
        String sValue = "";

        if (key.equals("darkmode")) {
            if (allEntries.get(key) instanceof String) {
                sValue = prefs.getString(key, "");
            } else if (allEntries.get(key) instanceof Boolean) {
                sValue = String.valueOf(prefs.getBoolean(key, false));
            }

            if (sValue.equals("true")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }



        } else if (key.equals("allowNotifications")) {
            if (allEntries.get(key) instanceof String) {
                sValue = prefs.getString(key, "");
            } else if (allEntries.get(key) instanceof Boolean) {
                sValue = String.valueOf(prefs.getBoolean(key, false));
            }

         /*   if (sValue.equals("true")) {
                notificationAllowed = true;
            } else {
                notificationManager.cancelAll();
                notificationAllowed = false;
            }

          */
        }
    }


}