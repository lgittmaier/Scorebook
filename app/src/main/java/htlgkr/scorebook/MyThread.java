package htlgkr.scorebook;


import android.location.Location;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class MyThread extends Thread {

    Location location;
    public OnDataReadyListener listener;
    public MyThread(Location location, OnDataReadyListener listener){
        this.location = location;
        this.listener = listener;
    }


    public void run(){

        // GET Request
        double lat = location == null ? -1 : location.getLatitude();
        double lon = location == null ? -1 : location.getLongitude();
        StringBuilder content=null;

        URL url = null;
        try {
            url = new URL("https://eu1.locationiq.com/v1/reverse.php?key=pk.39c88f15e145d67ecb4c4e81033dc1d2&lat=" + lat + "&lon=" + lon + "&format=json");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

        listener.onReady(content);
        //NewRound.readJson(content);
    }
}
