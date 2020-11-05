package com.example.hw8.ui.home;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw8.GPSTracker;
import com.example.hw8.HttpHandler;
import com.example.hw8.R;
import com.example.hw8.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //for speech to text
    private final int REQ_CODE = 100;
    private final int RESULT_OK = -1;

    private EditText editText;
    private String apikey = "e273afec1d1a815c17ea839cc1b9357a";
    private String query;
    private static final String TAG = HttpHandler.class.getSimpleName();

    //for GPS
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    GPSTracker gps;

    //singleton class
    WeatherData weatherData = WeatherData.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_search, container, false);


        //for
        editText = root.findViewById(R.id.input);


        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //onclick for speech to text
        Button speak = root.findViewById(R.id.stt);
        speak.setOnClickListener(new View.OnClickListener() {

            //function taken from speech to text tutorial
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {



                }
            }
        });

        //onclick for Search by Location
        Button searchByLoc = root.findViewById(R.id.search);
        searchByLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                EditText temp = (EditText) root.findViewById(R.id.input);
                query = temp.getText().toString();

                //checks if inputted String is zipcode or city/state
                if(isNumeric(query)){
                    query = "zip=" + query;
                }
                else{
                    query = "q=" + query;
                }


                new GetData().execute();
            }
        } );

        //onclick for Search by GPS
        Button searchByGps = root.findViewById(R.id.gps);
        searchByGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gps = new GPSTracker(getActivity());


                    //If location persmissions are permitted
                    if(gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        //Input query that uses the latitude and longitude of the user
                        query = "lat=" + latitude + "&lon=" + longitude;

                    }else{
                        //Error that prompts user to enable location services
                        gps.showSettingsAlert();
                    }

                    new GetData().execute();

                }
            } );



        return root;
    }





    //Separate class housing functionality for JSONParse, map, and printing to UI
    private class GetData extends AsyncTask<Void, Void, Void>
    {

        //Converts temperature (as a string) from Kelvin to Celsius (outputs string)
        public String toStringCelsius(String temp)
        {
            double num = Double.parseDouble(temp);
            num = Math.round(num - 273.15);

            return String.valueOf(num) + "\u00B0 C";
        }

        //Occurs prior to execution of GetData().execute()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        //Runs in background of onCreate
        //Calls HttpRequest, extracts data from JSON object, and stores in class variables
        @Override
        protected Void doInBackground(Void... voids) {

            //'location' is initialized as either of the following:
            //"zip=xxxxxxxxx"
            //"q=xxxxxxxxx"
            //"lat=xxxxxxx&long=xxxxxxxx"
            String url = "https://api.openweathermap.org/data/2.5/weather?" + query + "&appid=" + apikey;

            //call HttpHandler to return object sh
            //sh contents is saved into a String, jsonStr
            //HttpHandler is directly taken from HW7 tutorials
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            //Logcat print for jsonStr to observe response
            Log.e(TAG, "Response from url: " + jsonStr);

            //will not run if there is no response
            if(jsonStr != null){
                try{
                    //create JSONObject from jsonStr (string from HTTPS request)
                    JSONObject json = new JSONObject(jsonStr);
                    String city = json.getString("name");
                    weatherData.setCity(city);

                    //create object referencing "sys" object from requested JSON
                    //access element 'name' from "sys"
                    JSONObject sys = json.getJSONObject("sys");
                    String country = sys.getString("country");
                    weatherData.setCountry(country);

                    //create object referencing "main" object from requested JSON
                    //access elements as required for app
                    JSONObject main = json.getJSONObject("main");
                    String temperature = toStringCelsius(main.getString("temp")); //in kelvin
                    String feels_like = toStringCelsius(main.getString("feels_like")); //in kelvin
                    String temp_min = toStringCelsius(main.getString("temp_min")); //in kelvin
                    String temp_max = toStringCelsius(main.getString("temp_max")); //in kelvin
                    String pressure = main.getString("pressure") + " mPa";
                    String humidity = main.getString("humidity") + "%";
                    weatherData.setTemperature(temperature);
                    weatherData.setFeels_like(feels_like);
                    weatherData.setTemp_min(temp_min);
                    weatherData.setTemp_max(temp_max);
                    weatherData.setPressure(pressure);
                    weatherData.setHumidity(humidity);

                    //referencing "wind" object
                    //access elements as required
                    JSONObject wind = json.getJSONObject("wind");
                    String windspeed = wind.getString("speed") + " m/s"; //m/s
                    String winddir = wind.getString("deg") + "\u00B0"; //in degrees
                    weatherData.setWindspeed(windspeed);
                    weatherData.setWinddir(winddir);


                    //"weather" object
                    //access elements as required
                    JSONArray weather = json.getJSONArray("weather");
                    JSONObject w = weather.getJSONObject(0);
                    String weathermain = w.getString("main");
                    String description = w.getString("description");
                    weatherData.setWeathermain(weathermain);


                    //see above
                    JSONObject coord = json.getJSONObject("coord");
                    String lon = coord.getString("lon");
                    String lat = coord.getString("lat");
                    weatherData.setLon(lon);
                    weatherData.setLat(lat);

                }
                catch(final JSONException e){

                }
            }

            //function must return null by default
            return null;
        }

        //Handles printing out all info stored in variables to the UI
        @Override
        protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                }

        }








    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                }
                break;
            }
        }
    }
}