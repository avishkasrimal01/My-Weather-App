package com.example.myweather;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivityRR extends AppCompatActivity {

    private static final String TAG = "MainActivityRR";

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_rr);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclevrview);
        items = new ArrayList<>();
        adapter = new MyAdapter(this, items);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);  // Pass activity context

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        String logName = LoginActivity.GlobaluserName; // Get the logged user name

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("WeatherData")
                .whereEqualTo("UserName", logName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String city = document.getString("City");
                            String feelsLike = document.getString("Feels Like");
                            String humidity = document.getString("Humidity");
                            String pressure = document.getString("Pressure");
                            String temperature = document.getString("Temperature");
                            String temperatureMax = document.getString("Temperature Max");
                            String temperatureMin = document.getString("Temperature Min");

                            String description = "Feels Like: " + feelsLike + "\n" +
                                    "Humidity: " + humidity + "\n" +
                                    "Pressure: " + pressure + "\n" +
                                    "Temperature: " + temperature + "\n" +
                                    "Temperature Max: " + temperatureMax + "\n" +
                                    "Temperature Min: " + temperatureMin;

                            items.add(new Item(city, description, R.drawable.fo_days_pic));

                            // Log the added item
                            Log.d(TAG, "Added item: " + city + ", " + description);
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter about data changes
                        // Log the total items count
                        Log.d(TAG, "Total items count: " + items.size());
                    } else {
                        // Handle error
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Firestore query failed: ", e));
    }
}
