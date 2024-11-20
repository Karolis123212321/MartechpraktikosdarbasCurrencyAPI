package com.example.martechpraktikosdarbascurrencyapi;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
            // Sukuriame SOAP užklausą
            String soapRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soap:Body>" +
                    "<getCurrentExchangeRate xmlns=\"http://webservices.lb.lt/ExchangeRates\">" +
                    "<Currency>USD</Currency>" +
                    "</getCurrentExchangeRate>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            // Atlikti HTTP POST užklausą
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", "http://webservices.lb.lt/ExchangeRates/getCurrentExchangeRate");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(soapRequest.getBytes());
            outputStream.flush();
            outputStream.close();

            // Gauti ir išanalizuoti atsakymą
            result = Parser.parseSOAPResponse(connection.getInputStream());
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