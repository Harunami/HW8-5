package com.example.hw8.ui.table;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw8.R;
import com.example.hw8.WeatherData;

import java.util.Locale;

public class TableFragment extends Fragment {

    WeatherData weatherData = WeatherData.getInstance();
    TextToSpeech t1;
    Button b1;

    private TableViewModel tableViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tableViewModel =
                ViewModelProviders.of(this).get(TableViewModel.class);
        View root = inflater.inflate(R.layout.fragment_table, container, false);

        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        b1=(Button)root.findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "Temperature at " + weatherData.getCity() + weatherData.getCountry() + weatherData.getTemperature();
                Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        //takes data from singleton class WeatherData and outputs all results using textView

        String temp;
        TextView textView = root.findViewById(R.id.dispLoc);
        temp = weatherData.getCity() + ", " + weatherData.getCountry();
        textView.setText(temp); //Display for example: Miami, US

        textView = root.findViewById(R.id.dispTemp);
        temp = weatherData.getTemperature();
        textView.setText(temp);

        textView = root.findViewById(R.id.dispTemp2);
        temp = weatherData.getWeathermain() + " :: Low of " + weatherData.getTemp_min() + " to high " + weatherData.getTemp_max();
        textView.setText(temp);

        textView = root.findViewById(R.id.feelslike);
        temp = weatherData.getFeels_like();
        textView.setText(temp);

        textView = root.findViewById(R.id.windspeed);
        temp = weatherData.getWindspeed();
        textView.setText(temp);

        textView = root.findViewById(R.id.winddir);
        temp = weatherData.getWinddir();
        textView.setText(temp);

        textView = root.findViewById(R.id.humidity);
        temp = weatherData.getHumidity();
        textView.setText(temp);

        textView = root.findViewById(R.id.latitude);
        temp = weatherData.getLat();
        textView.setText(temp);

        textView = root.findViewById(R.id.longitude);
        temp = weatherData.getLon();
        textView.setText(temp);

        textView = root.findViewById(R.id.pressure);
        temp = weatherData.getPressure();
        textView.setText(temp);

        return root;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}