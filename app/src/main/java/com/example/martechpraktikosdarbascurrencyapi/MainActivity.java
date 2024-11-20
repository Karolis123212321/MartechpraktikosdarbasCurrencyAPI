package com.example.martechpraktikosdarbascurrencyapi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.util.Log;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> currencyRates;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        currencyRates = new ArrayList<>();

        // Inicijuojame adapterį ir priskiriame jį ListView elementui
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyRates);
        listView.setAdapter(adapter);

        // Paleidžiame duomenų nuskaitymo užduotį
        Log.d(TAG, "Pradedame duomenų nuskaitymo užduotį");
        DataLoader dataLoader = new DataLoader(this, currencyRates, adapter);
        dataLoader.execute("https://www.floatrates.com/daily/eur.xml");
    }
}