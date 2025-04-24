package com.emr.testEmergency;

import android.annotation.SuppressLint;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fb_btn = findViewById(R.id.fb_btn);

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        URL url = new URL("https://test-emergency-8d509-default-rtdb.firebaseio.com/.json"); // Örnek API endpoint'i. JSON formatında is_acitve değerini acil durum olarak alıyor.
                        String response = makeRequest(url);

                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean emergency = jsonObject.getBoolean("isActive"); // Firebase üzerinden örnek veriyi alacak
                                if (emergency){
                                    runOnUiThread(() -> {
                                        setContentView(R.layout.emergency_layout);
                                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                                        List<String> numaralar = new ArrayList<>();
                                        numaralar.add("112"); // Her listenin en başında yer alancak acil durum numarası
                                        numaralar.add("+905312345678"); // Örnek telefon numaraları
                                        numaralar.add("+905312345677");

                                        TextAdapter adapter = new TextAdapter(MainActivity.this, numaralar);
                                        recyclerView.setAdapter(adapter);
                                    });
                                }
                                else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(getApplicationContext(), "Acil durum aktif değil.", Toast.LENGTH_LONG).show();
                                    });
                                }

                            } catch (JSONException e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), "JSON parse hatası: " + e.toString(), Toast.LENGTH_LONG).show();
                                });
                            }
                        }

                    } catch (MalformedURLException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        });
                    }
                }).start();
            }
        });
    }

    public String makeRequest(URL url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            InputStream inputStream = (responseCode == HttpURLConnection.HTTP_OK)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}