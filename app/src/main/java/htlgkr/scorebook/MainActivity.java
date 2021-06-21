package htlgkr.scorebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    public static ListView lv;
    private static LayoutInflater layoutInflater;
    public static RoundAdapter roundAdapter;


    public static final String CHANNEL_ID = "notification_channel1";
    public static List<Integer> notifications = new ArrayList<>();
    private NotificationManager notificationManager;
    private int notificationId = 99;
    public static boolean notificationAllowed;

    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";

    public Bundle statsBundle;

    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;

    private SharedPreferences prefs1;
    private SharedPreferences prefs2;
    private SharedPreferences prefs3;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener1;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.playedRounds);
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        loadRoundsFromCSV(); // loads rounds from the csv file

        // preferences
        prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChanged(prefs1, "darkmode");
        preferenceChanged(prefs2, "allowNotifications");
        preferencesChangeListener1 = (SharedPreferences, key) -> preferenceChanged(prefs1, "darkmode");
        preferencesChangeListener2 = (SharedPreferences, key) -> preferenceChanged(prefs2, "allowNotifications");
        prefs1.registerOnSharedPreferenceChangeListener(preferencesChangeListener1);
        prefs2.registerOnSharedPreferenceChangeListener(preferencesChangeListener2);


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

    public void loadRoundsFromCSV() {
        rounds.clear();
        List<Round> toDos = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(Hole.filename);

            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = in.readLine()) != null) {
                String[] tmp = line.split(";");
                Round r = null;


                if (tmp[0] == null || tmp[0].equals("") || tmp[0].equals(" ") || tmp[1] == null || tmp[1].equals("") || tmp[1].equals(" ")) {

                } else {
                    r = new Round(tmp[0], tmp[1], tmp[2], Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]),
                            Integer.parseInt(tmp[5]), Integer.parseInt(tmp[6]), Integer.parseInt(tmp[7]), Integer.parseInt(tmp[8]));
                    addRound(r);
                }


            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
        } else if (item.getItemId() == R.id.menu_preferences) {

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

            if (sValue.equals("true")) {

                notificationAllowed = true;
            } else {

                notificationAllowed = false;
            }


        }
    }

    //context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //show statistics
        if (item.getItemId() == R.id.showStats) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            if (info != null) {
                int pos = info.position;
                Round currentRound = (Round) lv.getAdapter().getItem(pos);

                Intent intent = new Intent(getApplicationContext(), Stats.class);  //starts Stats-Acivity

                Bundle bundle = new Bundle();
                bundle.putString("name", currentRound.getName());
                bundle.putString("address", currentRound.getAddress());
                bundle.putString("date", currentRound.getDate());
                bundle.putString("par", String.valueOf(currentRound.getPar()));
                bundle.putString("score", String.valueOf(currentRound.getScore()));
                bundle.putString("over", String.valueOf(currentRound.getScore()-currentRound.getPar()));
                bundle.putString("putts", String.valueOf(currentRound.getPutts()));
                bundle.putString("fairway", String.valueOf(currentRound.getFairway()));
                bundle.putString("gir", String.valueOf(currentRound.getGir()));
                intent.putExtras(bundle);
                startActivity(intent);

            }
            return true;

            //delete round
        } else if (item.getItemId() == R.id.deleteRound) {

            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if (info != null) {
                int pos = info.position;
                Round currentRound = (Round) lv.getAdapter().getItem(pos);
                rounds.remove(currentRound);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Round at " + currentRound.getName()+ " on "+ currentRound.getDate() + " deleted !");
                alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
            bindViewToAdapter();
            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}