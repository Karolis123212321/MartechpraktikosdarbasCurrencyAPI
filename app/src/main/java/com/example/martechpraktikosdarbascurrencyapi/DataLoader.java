package com.example.martechpraktikosdarbascurrencyapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader extends AsyncTask<String, Void, ArrayList<String>> {
    private Context context;
    private ArrayList<String> currencyRates;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "DataLoader";

    public DataLoader(Context context, ArrayList<String> currencyRates, ArrayAdapter<String> adapter) {
        this.context = context;
        this.currencyRates = currencyRates;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> result = new ArrayList<>();
        try {
            Log.d(TAG, "Pradedame XML duomenų užklausą");

            // Atlikti HTTP GET užklausą
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                // Gauti ir išanalizuoti atsakymą
                result = Parser.parseXMLResponse(inputStream);
                Log.d(TAG, "Atsakymo analizė baigta, gautų elementų skaičius: " + result.size());
            } else {
                Log.e(TAG, "Klaida vykdant užklausą, atsakymo kodas: " + responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Klaida vykdant užklausą", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        if (result.isEmpty()) {
            Log.w(TAG, "Nėra gautų valiutos kursų rezultatų");
        } else {
            Log.d(TAG, "Atnaujiname ListView su gautais rezultatais");
        }
        currencyRates.clear();
        currencyRates.addAll(result);
        adapter.notifyDataSetChanged();
    }
}