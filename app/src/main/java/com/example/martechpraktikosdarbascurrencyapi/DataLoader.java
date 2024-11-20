package com.example.currencyrates;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class DataLoader extends AsyncTask<String, Void, ArrayList<String>> {
    private Context context;
    private ArrayList<String> currencyRates;
    private ArrayAdapter<String> adapter;

    public DataLoader(Context context, ArrayList<String> currencyRates, ArrayAdapter<String> adapter) {
        this.context = context;
        this.currencyRates = currencyRates;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> result = new ArrayList<>();
        try {
            String data = Parser.parseXML(urls[0]);
            result.addAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        currencyRates.clear();
        currencyRates.addAll(result);
        adapter.notifyDataSetChanged();
    }
}