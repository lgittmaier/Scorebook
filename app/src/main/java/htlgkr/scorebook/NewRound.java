package htlgkr.scorebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NewRound extends AppCompatActivity {
    private LayoutInflater layoutInflater;

     static List<Round> rounds = new ArrayList<>();

    public static List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newround);

        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      // sets the back button

    }

}
