package htlgkr.scorebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ListView lv;
    private static LayoutInflater layoutInflater;
    public static RoundAdapter roundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.playedRounds);
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        }
        return super.onOptionsItemSelected(item);

    }
}