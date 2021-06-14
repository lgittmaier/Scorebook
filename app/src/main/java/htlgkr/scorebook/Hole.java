package htlgkr.scorebook;


import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class Hole extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private TextView tvShowNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShowNumbers = findViewById(R.id.tvShowNumbers);
        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(23);
        numberPicker.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        tvShowNumbers.setText("Old Value = " + i + " New Value = " + i1);
    }
}
