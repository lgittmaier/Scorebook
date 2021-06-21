package htlgkr.scorebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stats extends AppCompatActivity {

    TextView tv_gcName, tv_address, tv_date, tv_par, tv_score, tv_over, tv_fairways, tv_putts, tv_gir, heading, ba;
    TextView et_gcName, et_address, et_date, et_par, et_score, et_over, et_fairways, et_putts, et_gir;
    private LayoutInflater layoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_gcName = findViewById(R.id.show_stats_gcname);
        tv_address = findViewById(R.id.show_stats_address);
        tv_date = findViewById(R.id.show_stats_date);
        tv_par = findViewById(R.id.show_stats_par);
        tv_score = findViewById(R.id.show_stats_score);
        tv_over = findViewById(R.id.show_stats_over);
        tv_fairways = findViewById(R.id.show_stats_fairways_hit);
        tv_putts = findViewById(R.id.show_stats_putts);
        tv_gir = findViewById(R.id.show_stats_greens_i_r);



        try {  // get bundle from MainActivity

            tv_gcName.append(getIntent().getExtras().getString("name"));
            tv_address.append(getIntent().getExtras().getString("address"));
            tv_date.append(getIntent().getExtras().getString("date"));
            tv_par.append(getIntent().getExtras().getString("par"));
            tv_score.append(getIntent().getExtras().getString("score"));
            tv_over.append(getIntent().getExtras().getString("over"));
            tv_putts.append(getIntent().getExtras().getString("putts"));
            tv_fairways.append(getIntent().getExtras().getString("fairway"));
            tv_gir.append(getIntent().getExtras().getString("gir"));


        } catch (Exception ignored) {
        }

    }


}
