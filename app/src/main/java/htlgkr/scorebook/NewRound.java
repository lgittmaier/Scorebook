package htlgkr.scorebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewRound extends AppCompatActivity implements OnDataReadyListener {
    private LayoutInflater layoutInflater;

    private Boolean isGpsAllowed = false;

    private MainActivity mainActivity;

    ProgressBar progressBar;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private RoundAdapter roundAdapter;
    int holeCounter = 1;


    private Button startNewRoundButton;

    static String additionalData; //address & longitude and latitude

    public static final int RQ_ACCESS_PERMISSIONS = 123;
    Calendar tmpCalendar;


    EditText dateAndTime, golfclub, holes;
    Button setLocationBtn, startRoundBtn;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  // fine location permission
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RQ_ACCESS_PERMISSIONS) {
            if (grantResults.length > 0 && (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            } else {
                gpsGranted();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newround);

        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      // sets the back button


        dateAndTime = findViewById(R.id.dateAndTime);
        dateAndTime.setInputType(EditorInfo.TYPE_NULL);
        golfclub = findViewById(R.id.golfclub);
        holes = findViewById(R.id.holes);

        setLocationBtn = findViewById(R.id.setLocationBtn);
        startRoundBtn = findViewById(R.id.startRoundBtn_newRound);
        startRoundBtn.setEnabled(false);


        // onClickListeners
        setDateAndTime(dateAndTime);
        setStartRoundBtn(startRoundBtn);
        setLocationButton(setLocationBtn);

    }


    public void setStartRoundBtn(Button startRoundBtn) {

        startRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(dateAndTime.getText().toString().equals("") || golfclub.getText().toString().equals("") || holes.getText().toString().equals(""))) {


                    Intent intent = new Intent(getApplicationContext(), Hole.class);  //starts new Hole

                    intent.setAction("bundleCode");
                    Bundle bundle = new Bundle();
                    bundle.putString("dateAndTime", dateAndTime.getText().toString());
                    bundle.putString("golfclub", golfclub.getText().toString());
                    bundle.putString("additionalData", additionalData);
                    bundle.putString("holes", holes.getText().toString());

                    intent.putExtras(bundle);   // gives the Hole class these values
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "fill all fields!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //dateTime//////////////////////////////////////////////////////////////////////////////////////////////

    public void setDateAndTime(EditText dateAndTime) {
        dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateAndTime);
            }
        });
    }


    public void showDateTimeDialog(EditText dateAndTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override       // to set the date
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override       // to set the time
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dateAndTime.setText(sdf.format(calendar.getTime()));
                        tmpCalendar = calendar;
                    }
                };
                new TimePickerDialog(NewRound.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };
        new DatePickerDialog(NewRound.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    //location//////////////////////////////////////////////////////////////////////////////////////////////

    public void setLocationButton(Button setLocationBtn) {

        setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionGPS();
            }
        });
    }

    private void checkPermissionGPS() {         // also checks network
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
        String networkPermission = Manifest.permission.ACCESS_NETWORK_STATE;

        //if (ActivityCompat.checkSelfPermission(this, locationPermission)
        //      != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(this, new String[]{locationPermission, networkPermission, coarseLocationPermission}, RQ_ACCESS_PERMISSIONS);
        //} else {

        gpsGranted();

        //}
    }

    @SuppressLint("MissingPermission")
    private void gpsGranted() {

        isGpsAllowed = true;
        OnDataReadyListener listener = this;

        progressBar = findViewById(R.id.progressbar);  //Progress bar under the setLocation Button
        progressBar.setMin(1);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(15);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                progressBar.setProgress(40);
                MyThread myThread = new MyThread(location, listener);     // does the GET-Request
                myThread.start();
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isGpsAllowed)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void onResume() {
        super.onResume();
        if (isGpsAllowed) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    3000,
                    0,
                    locationListener);
        }
    }


    public void readJson(StringBuilder content) {        // fills field additionalData  /  gets called by MyThread


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(String.valueOf(content));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String street = "";
        String houseNumber = "";
        String city = "";

        String displayName = "";
        String[] splittedDisplayName;

        progressBar.setProgress(75);


        if (jsonObject != null) {
            try {
                displayName = jsonObject.getString("display_name");
                splittedDisplayName = displayName.split(",");

                houseNumber = splittedDisplayName[0].trim();
                street = splittedDisplayName[1].trim();
                city = splittedDisplayName[4].trim();


            } catch (JSONException e) {
                e.printStackTrace();
            }

            additionalData = street + " " + houseNumber + ", " + city;// format: street houseNumber, city

            progressBar.setVisibility(View.INVISIBLE);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    startRoundBtn.setEnabled(true);

                }
            });
        }
    }


    @Override
    public void onReady(StringBuilder content) {
        progressBar.setProgress(60);
        readJson(content);
    }


}
