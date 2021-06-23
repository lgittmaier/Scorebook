package htlgkr.scorebook;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Hole extends AppCompatActivity {

    private LayoutInflater layoutInflater;

    public static final String filename = "rounds01.csv";

    Switch greenhitSw, fairwayhitSw;
    TextView scoreTv, puttsTv, parTv;
    NumberPicker scorePicker, puttsPicker, parPicker;
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


    public static NotificationManager notificationManager;
    public static final String CHANNEL_ID = "notification_channel1";

    private int notificationId = 99;
    public static boolean notificationAllowed;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holeeditor);

        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        newRound = new NewRound();

        try {
            if (getIntent().getAction().equals("bundleCode")) {
                dateAndTime = getIntent().getExtras().getString("dateAndTime");
                golfclub = getIntent().getExtras().getString("golfclub");
                additionalData = getIntent().getExtras().getString("additionalData");
                holes = Integer.parseInt(getIntent().getExtras().getString("holes"));
            }
        } catch (Exception ignored) {
        }

        parTv = findViewById(R.id.parTv);
        scoreTv = findViewById(R.id.scoreTv);
        puttsTv = findViewById(R.id.puttsTv);

        parPicker = findViewById(R.id.numberPickerPar);
        scorePicker = findViewById(R.id.numberPickerScore);
        puttsPicker = findViewById(R.id.numberPickerPutts);
        parPicker.setMinValue(3);
        parPicker.setMaxValue(5);
        scorePicker.setMinValue(1);
        scorePicker.setMaxValue(12);
        puttsPicker.setMinValue(0);
        puttsPicker.setMaxValue(11);

        holeHeading = findViewById(R.id.holeHeading);
        holeHeading.setText("Hole: " + newRound.holeCounter);

        nextBtn = findViewById(R.id.nextHoleBtn);
        greenhitSw = findViewById(R.id.greenhitSw);
        fairwayhitSw = findViewById(R.id.fairwayhitSw);

        setUpNextBtn(nextBtn);
        setUpScrollItems(puttsPicker);

        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannel";
            String description = "notificationDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void bindViewToAdapter() {
        List<Round> roundList = new ArrayList<>(new HashSet<>(MainActivity.getRounds()));

        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        roundAdapter = new RoundAdapter(roundList, layoutInflater);

        MainActivity.lv.setAdapter(roundAdapter);
    }

    public void clearHole() {
        scoreTv.setText("");
        parTv.setText("");
        puttsTv.setText("");
        greenhitSw.setChecked(false);
        fairwayhitSw.setChecked(false);
    }

    public void writeRoundToCSV(List<Round> roundList) {        //writes into CSV-File

        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < roundList.size(); i++) {
                out.println(roundList.get(i).getName() + ";"
                        + roundList.get(i).getAddress() + ";"
                        + roundList.get(i).getDate() + ";"
                        + roundList.get(i).getPar() + ";"
                        + roundList.get(i).getScore() + ";"
                        + roundList.get(i).getOver() + ";"
                        + roundList.get(i).getPutts() + ";"
                        + roundList.get(i).getFairway() + ";"
                        + roundList.get(i).getGir());
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException exp) {
            Log.d("TAG", exp.getStackTrace().toString());
        }

    }

    public void setUpScrollItems(NumberPicker puttsPicker){  // if your score is 5 your max putts can be 4
        scorePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                puttsPicker.setMaxValue(scorePicker.getValue()-1);
            }
        });
    }

    public void setUpNextBtn(Button nextBtn) {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putts += puttsPicker.getValue();
                par += parPicker.getValue();
                score += scorePicker.getValue();

                if (fairwayhitSw.isChecked()) {
                    fairways++;
                }
                if (greenhitSw.isChecked()) {
                    gir++;
                }


                if (newRound.holeCounter == holes) { // round finished?
                    Round round = new Round(
                            golfclub,
                            additionalData,
                            dateAndTime,
                            par, score, score - par, putts, fairways, gir);
                    MainActivity.addRound(round);

                    writeRoundToCSV(MainActivity.getRounds());

                    bindViewToAdapter();

                    Toast.makeText(getApplicationContext(), "saving round at " + golfclub, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class); // back to main screen
                    startActivity(intent);

                    if (notificationAllowed = true) {
                        android.app.Notification notification = new Notification.Builder(getApplicationContext(), MainActivity.CHANNEL_ID)
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setColor(Color.YELLOW)
                                .setContentTitle("Round finished")
                                .setContentText("at " + golfclub + "; par: " + par + "; your score: " + score)
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setGroup("notificationGroup")
                                .build();
                        notificationManager.notify(notificationId, notification);
                    }

                } else {
                    clearHole();

                    newRound.holeCounter++;
                    holeHeading.setText("Hole: " + newRound.holeCounter);
                }
            }
        });
    }
}
