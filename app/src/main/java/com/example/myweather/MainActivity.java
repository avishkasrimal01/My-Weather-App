package com.example.myweather;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GetWeather.OnWeatherDataFetchedListener {

    TextView cityName;
    Button search, deletebtn, next;
    TextView show;
    String url;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        deletebtn = findViewById(R.id.deletebtn);
        show = findViewById(R.id.weather);
        next = findViewById(R.id.next);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = cityName.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a city name", Toast.LENGTH_SHORT).show();
                    return;
                }
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=91565a1f5a92be129a150e49758c3aaa";
                new GetWeather(MainActivity.this).execute(url);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName.setText("");
                show.setText("");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivityRR.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onWeatherDataFetched(String data) {
        if (data == null) {
            show.setText("Cannot retrieve weather data. Please check your network connection and city name.");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject mainObject = jsonObject.getJSONObject("main");

            StringBuilder weatherInfo = new StringBuilder();
            weatherInfo.append("Temperature: ").append(mainObject.getString("temp")).append("\n\n");
            weatherInfo.append("Feels Like: ").append(mainObject.getString("feels_like")).append("\n\n");
            weatherInfo.append("Temperature Max: ").append(mainObject.getString("temp_max")).append("\n\n");
            weatherInfo.append("Temperature Min: ").append(mainObject.getString("temp_min")).append("\n\n");
            weatherInfo.append("Pressure: ").append(mainObject.getString("pressure")).append("\n\n");
            weatherInfo.append("Humidity: ").append(mainObject.getString("humidity")).append("\n\n");

            show.setText(weatherInfo.toString());

            addRecord(data, city);

        } catch (Exception e) {
            e.printStackTrace();
            show.setText("Error parsing weather data. Please try again.");
        }
    }

    protected void addRecord(String dataReturned, String city) {
        String LogName = LoginActivity.GlobaluserName;

        if (dataReturned == null) {
            show.setText("Cannot retrieve weather data. Please check your network connection and city name.");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(dataReturned);
            JSONObject mainObject = jsonObject.getJSONObject("main");

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("UserName", LogName);
            data.put("City", city);
            data.put("Temperature", mainObject.getString("temp"));
            data.put("Feels Like", mainObject.getString("feels_like"));
            data.put("Temperature Max", mainObject.getString("temp_max"));
            data.put("Temperature Min", mainObject.getString("temp_min"));
            data.put("Pressure", mainObject.getString("pressure"));
            data.put("Humidity", mainObject.getString("humidity"));

            db.collection("WeatherData").document()
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            show.setText("Error parsing weather data. Please try again.");
        }
    }
}
