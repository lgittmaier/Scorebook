package htlgkr.scorebook;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Hole extends AppCompatActivity {

    private LayoutInflater layoutInflater;

    Switch greenhitSw, fairwayhitSw;
    EditText scoreTv, puttsTv, parTv;
    TextView holeHeading;
    Button nextBtn;
    NewRound newRound;
    RoundAdapter roundAdapter;

    String dateAndTime;
    String golfclub;
    String additionalData;
    int holes;  // holes to play

    int par;
    int score;
    int putts;
    int fairways;  // fairways hit
    int gir;  // greens in regulation


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holeeditor);

        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        newRound = new NewRound();

        try {
            if (getIntent().getAction().equals("bundleCode")){
                dateAndTime = getIntent().getExtras().getString("dateAndTime");
                golfclub = getIntent().getExtras().getString("golfclub");
                additionalData = getIntent().getExtras().getString("additionalData");
                holes = Integer.parseInt(getIntent().getExtras().getString("holes"));
            }
        }catch (Exception ignored){}

        scoreTv = findViewById(R.id.scoreTv);
        puttsTv = findViewById(R.id.puttsTv);

        holeHeading = findViewById(R.id.holeHeading);
        holeHeading.setText("Hole: "+newRound.holeCounter);

        parTv = findViewById(R.id.parTv);
        nextBtn = findViewById(R.id.nextHoleBtn);
        greenhitSw = findViewById(R.id.greenhitSw);
        fairwayhitSw = findViewById(R.id.fairwayhitSw);

        setUpNextBtn(nextBtn);

    }

    public void bindViewToAdapter() {
        List<Round> roundList = new ArrayList<>(new HashSet<>(MainActivity.getRounds()));

        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        roundAdapter = new RoundAdapter(roundList, layoutInflater);

        MainActivity.lv.setAdapter(roundAdapter);
    }

    public void clearHole(){
        scoreTv.setText("");
        parTv.setText("");
        puttsTv.setText("");
        greenhitSw.setChecked(false);
        fairwayhitSw.setChecked(false);
    }



    public void setUpNextBtn(Button nextBtn) {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!(scoreTv.getText().toString().equals("") || scoreTv.getText() == null)
                        && !(parTv.getText().toString().equals("") || parTv.getText() == null)
                        && !(puttsTv.getText().toString().equals("") || puttsTv.getText() == null)) {

                    par += Integer.parseInt(parTv.getText().toString());
                    putts += Integer.parseInt(puttsTv.getText().toString());
                    score += Integer.parseInt(scoreTv.getText().toString());

                    if (fairwayhitSw.isChecked()){
                        fairways ++;
                    }
                    if (greenhitSw.isChecked()){
                        gir ++;
                    }


                    if (newRound.holeCounter == holes){ // round finished?
                        Round round = new Round(
                                golfclub,
                                additionalData,
                                dateAndTime,
                                par, score, score-par, putts, fairways, gir);
                        MainActivity.addRound(round);

                        bindViewToAdapter();

                        Toast.makeText(getApplicationContext(), "saving round at "+golfclub, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class); // back to main screen
                        startActivity(intent);
                    }else{

                        clearHole();

                        newRound.holeCounter ++;
                        holeHeading.setText("Hole: "+newRound.holeCounter);
                    }


                }
            }
        });
    }

}
