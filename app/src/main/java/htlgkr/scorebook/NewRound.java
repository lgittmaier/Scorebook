package htlgkr.scorebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class NewRound extends AppCompatActivity {
    private LayoutInflater layoutInflater;

    private Boolean isGpsAllowed = false;

    private MainActivity mainActivity;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private RoundAdapter roundAdapter;
    public static NotificationManager notificationManager;

    int holeCounter = 1;


    public static final String CHANNEL_ID = "notification_channel1";
    public static List<Integer> notifications = new ArrayList<>();

    private int notificationId = 99;
    public static boolean notificationAllowed;

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
        startNewRoundButton = findViewById(R.id.startRoundBtn_newRound);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      // sets the back button

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


        dateAndTime = findViewById(R.id.dateAndTime);
        dateAndTime.setInputType(EditorInfo.TYPE_NULL);
        golfclub = findViewById(R.id.golfclub);
        holes = findViewById(R.id.holes);

        setLocationBtn = findViewById(R.id.setLocationBtn);
        startRoundBtn = findViewById(R.id.startRoundBtn_newRound);


        // onClickListeners
        setDateAndTime(dateAndTime);
        setStartRoundBtn(startRoundBtn);
        setLocationButton(setLocationBtn);

    }

    //notification///////////////////////////////////////////////////////////////////////////////////////
   public void newRoundstarted() {


        if (notificationAllowed = true) {
            android.app.Notification notification = new Notification.Builder(this, MainActivity.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setColor(Color.YELLOW)
                    .setContentTitle("A new round has started!")
                    .setContentText("You can now document your round." )
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setGroup("notificationGroup")
                    .build();
            notificationManager.notify(notificationId, notification);
        }
        else {



        }
    }



    public void setStartRoundBtn(Button startRoundBtn) {

        startRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Hole.class);  //starts new Hole

                intent.setAction("bundleCode");
                Bundle bundle = new Bundle();
                bundle.putString("dateAndTime", dateAndTime.getText().toString());
                bundle.putString("golfclub", golfclub.getText().toString());
                bundle.putString("additionalData", additionalData);
                bundle.putString("holes", holes.getText().toString());

                intent.putExtras(bundle);   // gives the Hole class these values
                startActivity(intent);

                newRoundstarted();



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
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
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

        if (ActivityCompat.checkSelfPermission(this, locationPermission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{locationPermission, networkPermission, coarseLocationPermission}, RQ_ACCESS_PERMISSIONS);
        } else {
            gpsGranted();
        }
    }

    private void gpsGranted() {

        isGpsAllowed = true;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                MyThread myThread = new MyThread(location);     // does the GET-Request
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


    public static void readJson(StringBuilder content) {        // fills field additionalData  /  gets called by MyThread
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


        if (jsonObject != null) {
            try {
                displayName = jsonObject.getString("display_name");
                splittedDisplayName = displayName.split(",");

                houseNumber = splittedDisplayName[0].trim();
                street = splittedDisplayName[1].trim();
                city = splittedDisplayName[3].trim();


            } catch (JSONException e) {
                e.printStackTrace();
            }


            additionalData = street + " " + houseNumber + ", " + city;// format: street houseNumber; postcode city; country; \n lat: latitude  lon: longitude

        }
    }










}
