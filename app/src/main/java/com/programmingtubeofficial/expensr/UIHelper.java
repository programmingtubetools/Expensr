package com.programmingtubeofficial.expensr;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIHelper {
    public static void updateUI(Context context, FirebaseUser user){

    }

    public static double getConversionRate(CurrencyType from, CurrencyType to){
        String exchangeApi = "https://finance.yahoo.com/quote/" + from.quote+to.quote + "%3DX?p=" + from.quote+to.quote + "%3DX";
        try {
            URL exchangeRateApi = new URL(exchangeApi);
            HttpURLConnection connection = (HttpURLConnection) exchangeRateApi.openConnection();
            connection.setRequestMethod("GET");
            String readLine = null;
            int responseCode = connection.getResponseCode();
            Log.d("RespCode", "getConversionRate: " + String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                String rawResponse = response.toString();
                Pattern p = Pattern.compile("data-test=\"qsp-price\" (.*?) value=\"(.*?)\"");
                Matcher m = p.matcher(rawResponse);
                m.find();

                String firstGp = m.group();
                p = Pattern.compile("value=\"(.*?)\"");
                m = p.matcher(firstGp);
                m.find();

                double conversionRate = Double.parseDouble(m.group().replace("value=","").replaceAll("\"", ""));
                return conversionRate;
            } else {
                throw new Exception("Error in API Call");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }
}
